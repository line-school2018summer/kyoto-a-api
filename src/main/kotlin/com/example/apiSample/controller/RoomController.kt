package com.example.apiSample.controller

import com.example.apiSample.model.Talk
import com.example.apiSample.model.TalkList
import com.example.apiSample.service.TalkService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


@RestController
class RoomController(private val talkService: TalkService) {
    @GetMapping(
            value = ["/rooms/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getTalks(@PathVariable("id" ) roomId: Long): ArrayList<TalkList> {
        var Talks: ArrayList<TalkList> = talkService.getTalksFromRoomId(roomId)
        return Talks
    }
}
