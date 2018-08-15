package com.example.apiSample.controller

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
                     private val auth: FirebaseGateway) {
    init {
        auth.authInit()
    }

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
    fun getRooms(@RequestHeader(value="Token", required=true)token: String): ArrayList<RoomList> {
        val uid = auth.verifyIdToken(token) ?: throw UnauthorizedException("Your token is invalid.")
        val user = userService.findByUid(uid)
        val Rooms: ArrayList<RoomList> = roomService.getRoomsFromUserId(user.id)
        return Rooms
    }

    @PostMapping(
            value = ["/rooms"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun createRoom(@RequestBody request: PostRoomRequest): Room {
        val Room: Room = roomService.createRoom(request.name)
        request.userIds.forEach {
            roomService.createUserRoom(it, Room.id)
        }
        return Room
    }

    @PutMapping(
            value = ["/rooms/{id}/name"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun updateRoom(@PathVariable("id") roomId: Long, @RequestBody request: PostRoomRequest): Room {
        return roomService.updateRoom(roomId, request.name)
    }

    @PutMapping(
            value = ["/rooms/{id}/members"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun createUserRoom(@PathVariable("id") roomId: Long, @RequestBody request: PostRoomRequest): Room {
        request.userIds.forEach {
            roomService.createUserRoom(it, roomId)
        }
        return roomService.getRoomFromId(roomId)
    }

    @DeleteMapping(
            value = ["/rooms/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun deleteUserRoom(@PathVariable("id") roomId: Long, @RequestBody request: PostRoomRequest): Room {
        request.userIds.forEach {
            roomService.deleteUserRoom(it, roomId)
        }
        return roomService.getRoomFromId(roomId)
    }
}