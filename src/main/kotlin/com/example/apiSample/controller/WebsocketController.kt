package com.example.apiSample.controller

import com.example.apiSample.firebase.AuthGateway
import com.example.apiSample.model.*
import com.example.apiSample.service.MessageService
import com.example.apiSample.service.RoomService
import com.example.apiSample.service.UserService
import io.swagger.annotations.Api
import netscape.security.ForbiddenTargetException
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import java.util.concurrent.TimeUnit
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import java.sql.Timestamp
import org.springframework.web.socket.messaging.SessionSubscribeEvent
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.stereotype.Service


// TODO: Websocketではクライアントからの送信は実装しないので、このコントローラも後で消す
@Controller
@Api(value = "api",description = "websocketに関するAPIです。")
class WebsocketController(private val roomService: RoomService,
                     private val userService: UserService,
                     private val messageService: MessageService,
                     private val authGateway: AuthGateway) {
    @MessageMapping("/hello")
    @SendTo("/topic/greetings") // メッセージの宛先を指定
    fun greeting(message: Message): Message {
        TimeUnit.SECONDS.sleep(3)
        return Message(2, 1, 1, "test", message.user_name, Timestamp(1L), Timestamp(1L))
    }
}

@EnableWebSocketMessageBroker // WebSocketのメッセージブローカーのBean定義を有効化する
@Configuration
class WebSocketConfig(private val userService: UserService, private val auth: AuthGateway, private val roomService: RoomService) : WebSocketMessageBrokerConfigurer {

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/hello") // WebSocketのエンドポイント (接続時に指定するエンドポイント)を指定
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.setApplicationDestinationPrefixes("/app") // アプリケーション(Controller)でハンドリングするエンドポイントのプレフィックス
        registry.enableSimpleBroker("/topic", "/queue") // Topic(Pub-Sub)とQueue(P2P)を有効化 >>> メッセージブローカーがハンドリングする
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(CustomChannelIntercepter(auth, userService, roomService))
    }
}

// TODO: subscribe出来たことを確認するために追加しているだけで、本番ではクラスごと削除
@Service
class SomeSubscribeListener @Autowired
constructor(private val template: SimpMessagingTemplate) {

    @EventListener
    fun handleSubscribeEvent(event: SessionSubscribeEvent) {
        val des = (event.message.headers["nativeHeaders"] as Map<*, *>)["destination"].toString().replace("[", "").replace("]", "")
        template.convertAndSend("/topic/greetings", "{\"user_name\": \"ON_SUBSCRIBE FOR %s\"}".format(des))
    }
}

class CustomChannelIntercepter(private val auth: AuthGateway, private val userService: UserService, private val roomService: RoomService): ChannelInterceptor {

    val messagesReg = Regex("^/topic/rooms/([0-9]+)/messages$")
    val roomsReg = Regex("^/topic/users/([0-9]+)/rooms$")

    override fun preSend(message: org.springframework.messaging.Message<*>, channel: MessageChannel): org.springframework.messaging.Message<*>? {
        val headerAccessor: StompHeaderAccessor = StompHeaderAccessor.wrap(message)
        if (StompCommand.SUBSCRIBE.equals(headerAccessor.command)) {
            val token = getTokenFromMessage(message)
            val user = getUserFromToken(token)
            val destination = getDestinationFromMessage(message)

            // rooms auth start
            val roomsMatch = roomsReg.find(destination)
            if (roomsMatch != null)  {
                val userId = roomsMatch.groupValues[1].toLong()
                if (userId != user.id) {
                    throw ForbiddenTargetException("you don't have permission for user id %d".format(userId))
                }
            }
            // rooms auth end

            // messages auth start
            val messagesMatch = messagesReg.find(destination)
            if (messagesMatch != null) {
                // [0]には全体が入り、[1]にキャプチャしたルームidが入る
                val roomId = messagesMatch.groupValues[1].toLong()
                if (!roomService.isUserExist(user.id, roomId)){
                    throw ForbiddenTargetException("you don't have permission for room id %d".format(roomId))
                }
            }
            // messages auth end
        }
        return message
    }

    fun getTokenFromMessage(message: org.springframework.messaging.Message<*>): String {
        val headers = message.headers["nativeHeaders"] as Map<*, *>
        return headers["Token"].toString().replace("[", "").replace("]", "")
    }

    fun getDestinationFromMessage(message: org.springframework.messaging.Message<*>): String {
        val headers = message.headers["nativeHeaders"] as Map<*, *>
        return headers["destination"].toString().replace("[", "").replace("]", "")
    }

    fun getUserFromToken(token: String): User {
        val uid = auth.verifyIdToken(token) ?: throw UnauthorizedException("Your token is invalid.")
        return userService.findByUid(uid)
    }
}
