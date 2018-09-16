package com.example.apiSample.controller

import com.example.apiSample.Requests.PostMessageRequest
import com.example.apiSample.firebase.AuthGateway
import com.example.apiSample.model.*
import com.example.apiSample.service.MessageService
import com.example.apiSample.service.RoomService
import com.example.apiSample.service.UserService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiModelProperty
import io.swagger.annotations.ApiOperation
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import java.util.concurrent.TimeUnit
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import java.sql.Timestamp


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
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/hello") // WebSocketのエンドポイント (接続時に指定するエンドポイント)を指定
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.setApplicationDestinationPrefixes("/app") // アプリケーション(Controller)でハンドリングするエンドポイントのプレフィックス
        registry.enableSimpleBroker("/topic", "/queue") // Topic(Pub-Sub)とQueue(P2P)を有効化 >>> メッセージブローカーがハンドリングする
    }
}
