package com.example.apiSample.controller

import com.example.apiSample.Requests.PostMessageRequest
import com.example.apiSample.model.Message
import com.example.apiSample.service.MessageService
import com.example.apiSample.service.UserService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


@RestController
class MessageController(private val messageService: MessageService, private val userService: UserService){ // , private val firebaseGateway: FirebaseGateway) {
    @GetMapping(
            value = ["/messages/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getMessage(@PathVariable("id" ) messageId: Long): Message {
        return messageService.getMessageFromId(messageId)
    }

    @PutMapping(
            value = ["/messages/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun updateMessage(@RequestHeader(value="Token", required=true) token: String, @PathVariable("id") messageId: Long, @RequestBody request: PostMessageRequest): Message {
//        val uid = firebaseGateway.verifyIdToken(token) ?: throw UnauthorizedException("")
//        val user = userService.findByUid(uid)
        return messageService.updateMessage(1L, messageId, request.text)
    }

    @DeleteMapping(
            value = ["/messages/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun deleteMessage(@RequestHeader(value="Token", required=true) token: String, @PathVariable("id") messageId: Long, @RequestBody request: PostMessageRequest): String {
//        val uid = firebaseGateway.verifyIdToken(token) ?: throw UnauthorizedException("")
//        val user = userService.findByUid(uid)
        var res = messageService.deleteMessage(1L, messageId)
        return "{\"result\": " + res.toString() + "}"
    }
}
