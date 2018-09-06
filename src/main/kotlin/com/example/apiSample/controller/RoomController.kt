package com.example.apiSample.controller

import com.example.apiSample.Requests.PostMessageRequest
import com.example.apiSample.firebase.AuthGateway
import com.example.apiSample.model.Message
import com.example.apiSample.service.MessageService
import com.example.apiSample.model.UserList
import com.example.apiSample.model.Room
import com.example.apiSample.service.RoomService
import com.example.apiSample.service.UserService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiModelProperty
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

data class PostRoomRequest(
        @ApiModelProperty(example="海外旅行ぐる", position=0)
        val name: String,

        @ApiModelProperty(example="[1,2]", position=1)
        val userIds: ArrayList<Long>
)

@RestController
@Api(value = "api",description = "ルームに関するAPIです。")
class RoomController(private val roomService: RoomService,
                     private val userService: UserService,
                     private val messageService: MessageService,
                     private val authGateway: AuthGateway) {

    @ApiOperation(value = "idに対応したルームの情報を取得します。")
    @GetMapping(
            value = ["/rooms/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getRoom(@PathVariable("id" ) roomId: Long): Room {
        return roomService.getRoomFromId(roomId)
    }

    @ApiOperation(value = "ログインしているユーザーが所属しているルームの一覧を取得します。")
    @GetMapping(
            value = ["/rooms"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getRooms(@RequestHeader(value="Token", required=true)token: String): ArrayList<Room> {
        val uid = authGateway.verifyIdToken(token) ?: throw UnauthorizedException("invalid token")
        val user = userService.findByUid(uid)
        val Rooms: ArrayList<Room> = roomService.getRoomsFromUserId(user.id)
        return Rooms
    }

    @ApiOperation(value = "ルームを作成します。")
    @PostMapping(
            value = ["/rooms"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun createRoom(@RequestHeader(value="Token", required=true)token: String, @RequestBody request: PostRoomRequest): Room {
        val uid = authGateway.verifyIdToken(token) ?: throw UnauthorizedException("invalid token")
        val user = userService.findByUid(uid)
        if (!request.userIds.contains(user.id)) {
            throw BadRequestException("You must join group")
        }
        val room = roomService.createRoom(request.name)
        request.userIds.forEach {
            roomService.addMember(it, room.id)
        }
        return roomService.getRoomFromId(room.id)
    }

    @ApiOperation(value = "idに対応するルームの名前を変更します。")
    @PutMapping(
            value = ["/rooms/{id}/name"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun updateRoom(@RequestHeader(value="Token", required=true)token: String, @PathVariable("id") roomId: Long, @RequestBody request: PostRoomRequest): Room {
        val uid = authGateway.verifyIdToken(token) ?: throw UnauthorizedException("invalid token")
        val user = userService.findByUid(uid)
        if (!roomService.isUserExist(user.id, roomId)) {
            throw BadRequestException("has no permission")
        }
        roomService.updateRoom(roomId, request.name)
        return roomService.getRoomFromId(roomId)
    }

    @ApiOperation(value = "idに対応するルームのユーザーを取得します。")
    @GetMapping(
            value = ["/rooms/{id}/members"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getMembers(@PathVariable("id" ) roomId: Long): ArrayList<UserList> {
        return roomService.getMembers(roomId)
    }

    @ApiOperation(value = "idに対応するルームのユーザーを変更します。")
    @PutMapping(
            value = ["/rooms/{id}/members"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun updateMembers(@RequestHeader(value="Token", required=true)token: String, @PathVariable("id") roomId: Long, @RequestBody request: PostRoomRequest): Room {
        val uid = authGateway.verifyIdToken(token) ?: throw UnauthorizedException("invalid token")
        val user = userService.findByUid(uid)
        if (!roomService.isUserExist(user.id, roomId)) {
            throw BadRequestException("has no permission")
        }
        val members: ArrayList<UserList> = roomService.getMembers(roomId)
        members.forEach{
            if (!request.userIds.contains(it.id)){
                roomService.removeMember(it.id, roomId)
            }
        }
        request.userIds.forEach {
            if (roomService.getUserRoom(it, roomId).isEmpty()) {
                roomService.addMember(it, roomId)
            }
        }
        return roomService.getRoomFromId(roomId)
    }

    @ApiOperation(value = "idに対応するルームのメッセージを取得します。")
    @GetMapping(
            value = ["/rooms/{id}/messages"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getMessages(@RequestHeader(value="Token", required=true) token: String, @PathVariable("id" ) roomId: Long, @RequestParam(required = false) since_id: String?, @RequestParam(required = false) limit: String?): ArrayList<Message> {
        val uid = authGateway.verifyIdToken(token) ?: throw UnauthorizedException("invalid token")
        val user = userService.findByUid(uid)
        if (!roomService.isUserExist(user.id, roomId)){
            throw BadRequestException("has no permission")
        }
        val messages: ArrayList<Message> = messageService.getMessagesFromRoomId(roomId, (since_id?.toLongOrNull()) ?: 0, (limit?.toIntOrNull()) ?: 50)
        return messages
    }

    @ApiOperation(value = "idに対応するルームにメッセージを追加します。")
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
