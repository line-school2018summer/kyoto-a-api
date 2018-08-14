package com.example.apiSample.controller

import com.example.apiSample.Requests.PostTalkRequest
import com.example.apiSample.model.Talk
import com.example.apiSample.model.TalkList
import com.example.apiSample.service.TalkService
import com.example.apiSample.service.UserService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


@RestController
class RoomController(private val talkService: TalkService, private val userService: UserService) {
    @GetMapping(
            value = ["/rooms/{id}/talks"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getTalks(@PathVariable("id" ) roomId: Long): ArrayList<TalkList> {
        val Talks: ArrayList<TalkList> = talkService.getTalksFromRoomId(roomId)
        return Talks
    }

    @PostMapping(
            value = ["/rooms/{id}/talks"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun createTalk(@RequestHeader(value="Token", required=true) token: String, @PathVariable("id") roomId: Long, @RequestBody request: PostTalkRequest): Talk {
        val auth = FirebaseGateway()
        auth.authInit()
        val uid = auth.verifyIdToken(token) ?: throw UnauthorizedException("")
        val user = userService.findByUid(uid)
        val talk: Talk = talkService.createTalk(roomId, user.id, request.text)
    }
}
