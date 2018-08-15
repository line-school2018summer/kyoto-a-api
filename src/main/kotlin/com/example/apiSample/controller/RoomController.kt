package com.example.apiSample.controller

import com.example.apiSample.Requests.PostMessageRequest
import com.example.apiSample.model.Message
import com.example.apiSample.model.MessageList
import com.example.apiSample.service.MessageService
import com.example.apiSample.service.UserService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


@RestController
class RoomController(private val messageService: MessageService, private val userService: UserService) {
    @GetMapping(
            value = ["/rooms/{id}/messages"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getTalks(@PathVariable("id" ) roomId: Long, @RequestParam since_id: String, @RequestParam limit: String): ArrayList<MessageList> {
        val messages: ArrayList<MessageList> = messageService.getTalksFromRoomId(roomId, since_id.toLongOrNull() ?: 0, limit.toIntOrNull() ?: 50)
        return messages
    }

    @PostMapping(
            value = ["/rooms/{id}/messages"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun createTalk(@RequestHeader(value="Token", required=true) token: String, @PathVariable("id") roomId: Long, @RequestBody request: PostMessageRequest): Message {
        val auth = FirebaseGateway()
        auth.authInit()
        val uid = auth.verifyIdToken(token) ?: throw UnauthorizedException("")
        val user = userService.findByUid(uid)
        val message: Message = messageService.createTalk(roomId, user.id, request.text)
        return message
    }
}
