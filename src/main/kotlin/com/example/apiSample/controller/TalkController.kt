package com.example.apiSample.controller

import com.example.apiSample.model.Talk
import com.example.apiSample.service.TalkService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


@RestController
class TalkController(private val talkService: TalkService) {
    @GetMapping(
            value = ["/talks/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getTalk(@PathVariable("id" ) talkId: Long): Talk {
        return talkService.getTalkFromId(talkId)
    }
}
