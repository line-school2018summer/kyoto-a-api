package com.example.apiSample.controller

import com.example.apiSample.Requests.PostMessageRequest
import com.example.apiSample.firebase.AuthGateway
import com.example.apiSample.model.Message
import com.example.apiSample.service.MessageService
import com.example.apiSample.service.RoomService
import com.example.apiSample.service.UserService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


@RestController
@Api(value = "api",description = "メッセージに関するAPIです。")
class MessageController(private val messageService: MessageService, private val userService: UserService, private val authGateway: AuthGateway, private val roomService: RoomService) {

    @ApiOperation(value = "idに対応したメッセージを取得します。")
    @GetMapping(
            value = ["/messages/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getMessage(@RequestHeader(value="Token", required=true) token: String, @PathVariable("id" ) messageId: Long): Message {
        val uid = authGateway.verifyIdToken(token) ?: throw UnauthorizedException("invalid token")
        val user = userService.findByUid(uid)
        val message = messageService.getMessageFromId(messageId)
        if (!roomService.isUserExist(user.id, message.room_id)) {
            throw BadRequestException("has no permission")
        }
        return message
    }

    @ApiOperation(value = "idに対応したメッセージを変更します。")
    @PutMapping(
            value = ["/messages/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun updateMessage(@RequestHeader(value="Token", required=true) token: String, @PathVariable("id") messageId: Long, @RequestBody request: PostMessageRequest): Message {
        val uid = authGateway.verifyIdToken(token) ?: throw UnauthorizedException("invalid token")
        val user = userService.findByUid(uid)
        return messageService.updateMessage(user.id, messageId, request.text)
    }

    @ApiOperation(value = "idに対応したメッセージを削除します。")
    @DeleteMapping(
            value = ["/messages/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun deleteMessage(@RequestHeader(value="Token", required=true) token: String, @PathVariable("id") messageId: Long): String {
        val uid = authGateway.verifyIdToken(token) ?: throw UnauthorizedException("invalid token")
        val user = userService.findByUid(uid)
        var res = messageService.deleteMessage(user.id, messageId)
        return "{\"result\": " + res.toString() + "}"
    }
}
