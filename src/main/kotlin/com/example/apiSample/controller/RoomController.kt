package com.example.apiSample.controller

import com.example.apiSample.firebase.AuthGateway
import com.example.apiSample.model.UserList
import com.example.apiSample.model.Room
import com.example.apiSample.model.RoomList
import com.example.apiSample.service.RoomService
import com.example.apiSample.service.UserService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

data class PostRoomRequest(
        val name: String,
        val userIds: ArrayList<Long>
)

@RestController
class RoomController(private val roomService: RoomService,
                     private val userService: UserService,
                     private val authGateway: AuthGateway) {
    @GetMapping(
            value = ["/rooms/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getRoom(@PathVariable("id" ) roomId: Long): Room {
        return roomService.getRoomFromId(roomId)
    }

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

    @GetMapping(
            value = ["/rooms/{id}/members"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getMembers(@PathVariable("id" ) roomId: Long): ArrayList<UserList> {
        return roomService.getMembers(roomId)
    }

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

}