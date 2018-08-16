package com.example.apiSample.controller

import com.example.apiSample.Requests.PostMessageRequest
import com.example.apiSample.model.Message
import com.example.apiSample.model.MessageList
import com.example.apiSample.service.MessageService
import com.example.apiSample.service.UserService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


@RestController
class RoomController(private val messageService: MessageService, private val userService: UserService, private val firebaseGateway: FirebaseGateway) {
    @GetMapping(
            value = ["/rooms/{id}/messages"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getMessages(@PathVariable("id" ) roomId: Long, @RequestParam(required = false) since_id: String?, @RequestParam(required = false) limit: String?): ArrayList<MessageList> {
        val messages: ArrayList<MessageList> = messageService.getMessagesFromRoomId(roomId, (since_id?.toLongOrNull()) ?: 0, (limit?.toIntOrNull()) ?: 50)
        return messages
    }

    @PostMapping(
            value = ["/rooms/{id}/messages"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun createMessage(@RequestHeader(value="Token", required=true) token: String, @PathVariable("id") roomId: Long, @RequestBody request: PostMessageRequest): Message {
        val uid = firebaseGateway.verifyIdToken(token) ?: throw UnauthorizedException("")
        val user = userService.findByUid(uid)
        val message: Message = messageService.createMessage(roomId, 1L, request.text)
        return message
    }
}
