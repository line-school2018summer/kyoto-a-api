package com.example.apiSample.controller

import com.example.apiSample.model.Room
import com.example.apiSample.model.RoomList
import com.example.apiSample.service.RoomService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


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
}