package com.example.apiSample.controller

import com.example.apiSample.Requests.PostMessageRequest
import com.example.apiSample.model.Message
import com.example.apiSample.service.MessageService
import com.example.apiSample.service.UserService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


@RestController
class MessageController(private val messageService: MessageService, private val userService: UserService) {
    @GetMapping(
            value = ["/messages/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getTalk(@PathVariable("id" ) talkId: Long): Message {
        return messageService.getTalkFromId(talkId)
    }

    @PutMapping(
            value = ["/messages/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun updateTalk(@RequestHeader(value="Token", required=true) token: String, @PathVariable("id") talkId: Long, @RequestBody request: PostMessageRequest): Message {
        val auth = FirebaseGateway()
        auth.authInit()
        val uid = auth.verifyIdToken(token) ?: throw UnauthorizedException("")
        val user = userService.findByUid(uid)
        return messageService.updateTalk(user.id, talkId, request.text)
    }

    @DeleteMapping(
            value = ["/messages/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun deleteTalk(@RequestHeader(value="Token", required=true) token: String, @PathVariable("id") talkId: Long, @RequestBody request: PostMessageRequest): Boolean {
        val auth = FirebaseGateway()
        auth.authInit()
        val uid = auth.verifyIdToken(token) ?: throw UnauthorizedException("")
        val user = userService.findByUid(uid)
        return messageService.deleteTalk(user.id, talkId)
    }
}
