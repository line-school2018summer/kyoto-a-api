package com.example.apiSample.controller

import com.example.apiSample.model.Room
import com.example.apiSample.model.RoomList
import com.example.apiSample.service.RoomService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

data class PostCreateRequest(
        val name: String,
        val userIds: ArrayList<Long>
)

@RestController
class RoomController(private val roomService: RoomService) {
    @GetMapping(
            value = ["/rooms/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getRoom(@PathVariable("id" ) roomId: Long): Room {
        var Room: Room = roomService.getRoomFromId(roomId)
        return Room
    }

    @GetMapping(
            value = ["/rooms"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getRooms(@PathVariable("id" ) userId: Long): ArrayList<RoomList> {
        var Rooms: ArrayList<RoomList> = roomService.getRoomsFromUserId(userId)
        return Rooms
    }

    @PostMapping(
            value = ["/rooms/create"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun createRoom(@RequestBody request: PostCreateRequest): ArrayList<RoomList> {
        var Rooms: ArrayList<RoomList> = roomService.getRoomsFromUserId(userId)
        return Rooms
    }
}