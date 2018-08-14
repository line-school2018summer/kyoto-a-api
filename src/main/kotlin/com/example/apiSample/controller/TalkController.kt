package com.example.apiSample.controller

import com.example.apiSample.Requests.PostTalkRequest
import com.example.apiSample.model.Talk
import com.example.apiSample.service.TalkService
import com.example.apiSample.service.UserService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


@RestController
class TalkController(private val talkService: TalkService, private val userService: UserService) {
    @GetMapping(
            value = ["/talks/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getTalk(@PathVariable("id" ) talkId: Long): Talk {
        return talkService.getTalkFromId(talkId)
    }

    @PutMapping(
            value = ["/talks/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun updateTalk(@RequestHeader(value="Token", required=true) token: String, @PathVariable("id") talkId: Long, @RequestBody request: PostTalkRequest): Talk {
        val auth = FirebaseGateway()
        auth.authInit()
        val uid = auth.verifyIdToken(token) ?: throw UnauthorizedException("")
        val user = userService.findByUid(uid)
        return talkService.updateTalk(user.id, talkId, request.text)
    }

    @DeleteMapping(
            value = ["/talks/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun deleteTalk(@RequestHeader(value="Token", required=true) token: String, @PathVariable("id") talkId: Long, @RequestBody request: PostTalkRequest): Boolean {
        val auth = FirebaseGateway()
        auth.authInit()
        val uid = auth.verifyIdToken(token) ?: throw UnauthorizedException("")
        val user = userService.findByUid(uid)
        return talkService.deleteTalk(user.id, talkId)
    }
}
