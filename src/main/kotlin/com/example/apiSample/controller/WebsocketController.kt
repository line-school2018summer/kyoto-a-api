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
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import java.sql.Timestamp
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.authorization.AuthenticatedReactiveAuthorizationManager.authenticated
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter


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
class WebSocketConfig : WebSocketMessageBrokerConfigurer, AbstractSecurityWebSocketMessageBrokerConfigurer() { // AbstractWebSocketMessageBrokerConfigurerを継承しWebSocket関連のBean定義をカスタマイズする

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/hello").withSockJS() // WebSocketのエンドポイント (接続時に指定するエンドポイント)を指定
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.setApplicationDestinationPrefixes("/app") // アプリケーション(Controller)でハンドリングするエンドポイントのプレフィックス
        registry.enableSimpleBroker("/topic", "/queue") // Topic(Pub-Sub)とQueue(P2P)を有効化 >>> メッセージブローカーがハンドリングする
    }

    override fun configureInbound(message: MessageSecurityMetadataSourceRegistry) {
        message
                .nullDestMatcher().permitAll()
                .simpDestMatchers("/app/**").permitAll()
                .simpSubscribeDestMatchers("/topic/" + "**").permitAll()
        .anyMessage().permitAll()
    }
}

//class CustomUserDetailsService: UserDetailsService {
//    override fun loadUserByUsername(username: String?): UserDetails {
//        return User("foo", "password", listOf(SimpleGrantedAuthority("admin")))
//    }
//}

@EnableWebSecurity
class MySecurityConfig: WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/my-login.jsp").permitAll()
                .anyRequest().permitAll()
    }
}