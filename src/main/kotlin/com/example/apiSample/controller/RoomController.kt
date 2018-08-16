package com.example.apiSample.controller

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
                     private val userService: UserService) {
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
//        val uid = auth.verifyIdToken(token) ?: throw UnauthorizedException("Your token is invalid.")
//        val user = userService.findByUid(uid)
        val Rooms: ArrayList<RoomList> = roomService.getRoomsFromUserId(1)
        return Rooms
    }

    @PostMapping(
            value = ["/rooms"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun createRoom(@RequestBody request: PostRoomRequest): Room {
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
    fun updateRoom(@PathVariable("id") roomId: Long, @RequestBody request: PostRoomRequest): Room {
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
    fun updateMembers(@PathVariable("id") roomId: Long, @RequestBody request: PostRoomRequest): Room {
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

//    @PutMapping(
//            value = ["/rooms/{id}/members"],
//            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
//    )
//    fun addMembers(@PathVariable("id") roomId: Long, @RequestBody request: PostRoomRequest): Room {
//        request.userIds.forEach {
//            roomService.addMember(it, roomId)
//        }
//        return roomService.getRoomFromId(roomId)
//    }
//
//    @DeleteMapping(
//            value = ["/rooms/{id}/members"],
//            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
//    )
//    fun removeMembers(@PathVariable("id") roomId: Long, @RequestBody request: PostRoomRequest): Room {
//        request.userIds.forEach {
//            roomService.removeMember(it, roomId)
//        }
//        return roomService.getRoomFromId(roomId)
//    }
}