package com.example.apiSample.service

import com.example.apiSample.controller.UnauthorizedException
import com.example.apiSample.mapper.TalkMapper
import com.example.apiSample.mapper.UserMapper
import com.example.apiSample.model.Talk
import com.example.apiSample.model.TalkList
import com.example.apiSample.model.UserProfile
import org.springframework.stereotype.Service

@Service
class TalkService(private val talkMapper: TalkMapper) {
    fun getTalksFromRoomId(roomId: Long): ArrayList<TalkList> {
        val talks = talkMapper.findByRoomId(roomId)
        return talks
    }

    fun getTalkFromId(talkId: Long): Talk {
        val talk = talkMapper.findByTalkId(talkId)
        return talk
    }

    fun createTalk(roomId: Long, userId: Long, text: String): Talk {
        val talk = talkMapper.createTalk(roomId, userId, text)
        return talk
    }

    fun updateTalk(userId: Long, talkId: Long, text: String): Talk {
        val talk = this.getTalkFromId(talkId)
        if (talk.user_id == userId) {
            return talkMapper.updateTalk(talkId, text)
        }
        throw UnauthorizedException("talk creator only can update talks")
    }

    fun deleteTalk(talkId: Long): Boolean {
        return talkMapper.deleteTalk(talkId)
    }
}
