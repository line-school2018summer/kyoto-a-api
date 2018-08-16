package com.example.apiSample.controller

import com.example.apiSample.Requests.PostMessageRequest
import com.example.apiSample.firebase.AuthGateway
import com.example.apiSample.model.Message
import com.example.apiSample.model.MessageList
import com.example.apiSample.service.MessageService
import com.example.apiSample.service.UserService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


@RestController
class RoomController(private val messageService: MessageService, private val userService: UserService, private val authGateway: AuthGateway, private val roomService: RoomService) {
    @GetMapping(
            value = ["/rooms/{id}/messages"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getMessages(@RequestHeader(value="Token", required=true) token: String, @PathVariable("id" ) roomId: Long, @RequestParam(required = false) since_id: String?, @RequestParam(required = false) limit: String?): ArrayList<MessageList> {
        val uid = authGateway.verifyIdToken(token) ?: throw UnauthorizedException("invalid token")
        val user = userService.findByUid(uid)
        if (!roomService.isUserExist(user.id, roomId)){
            throw BadRequestException("has no permission")
        }
        val messages: ArrayList<MessageList> = messageService.getMessagesFromRoomId(roomId, (since_id?.toLongOrNull()) ?: 0, (limit?.toIntOrNull()) ?: 50)
        return messages
    }

    @PostMapping(
            value = ["/rooms/{id}/messages"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun createMessage(@RequestHeader(value="Token", required=true) token: String, @PathVariable("id") roomId: Long, @RequestBody request: PostMessageRequest): Message {
        val uid = authGateway.verifyIdToken(token) ?: throw UnauthorizedException("invalid token")
        val user = userService.findByUid(uid)
        if (!roomService.isUserExist(user.id, roomId)) {
            throw BadRequestException("has no permission")
        }
        val message: Message = messageService.createMessage(roomId, user.id, request.text)
        return message
    }
}
