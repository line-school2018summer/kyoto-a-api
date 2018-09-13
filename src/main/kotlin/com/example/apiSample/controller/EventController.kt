package com.example.apiSample.controller

import com.example.apiSample.firebase.AuthGateway
import com.example.apiSample.model.Event
import com.example.apiSample.service.EventService
import com.example.apiSample.service.RoomService
import com.example.apiSample.service.UserService
import io.swagger.annotations.Api
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


@RestController
@Api(value = "api",description = "イベントに関するAPIです。")
class EventController(private val eventService: EventService, private val userService: UserService, private val authGateway: AuthGateway, private val roomService: RoomService) {

    @GetMapping(
            value = ["/events/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getEvent(@RequestHeader(value = "Token", required = true) token: String, @PathVariable("id") eventId: Long): Event {
        val uid = authGateway.verifyIdToken(token) ?: throw UnauthorizedException("invalid token")
        val user = userService.findByUid(uid)
        val event = eventService.getEventFromId(eventId)
        val room_id = event.room_id
        room_id ?: return event
        if (!roomService.isUserExist(user.id, room_id)) {
            throw BadRequestException("has no permission")
        }
        return event
    }

    @GetMapping(
            value = ["/events/rooms/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getRoomEvents(@RequestHeader(value = "Token", required = true) token: String, @PathVariable("id") roomId: Long): ArrayList<Event> {
        return eventService.getRoomEventsFromRoomId(roomId)
    }

    @GetMapping(
            value = ["/events/rooms/{id}/messages"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getMessageEvents(@RequestHeader(value = "Token", required = true) token: String, @PathVariable("id") roomId: Long): ArrayList<Event> {
        return eventService.getMessageEventsFromRoomId(roomId)
    }
}
