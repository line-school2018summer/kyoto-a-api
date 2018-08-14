package com.example.apiSample.service

import com.example.apiSample.controller.BadRequestException
import com.example.apiSample.mapper.TalkMapper
import com.example.apiSample.model.Talk
import com.example.apiSample.model.TalkList
import org.springframework.stereotype.Service

@Service
class TalkService(private val talkMapper: TalkMapper) {
    fun getTalksFromRoomId(roomId: Long, sinceId: Long = 0, limit: Int = 50): ArrayList<TalkList> {
        val talks = talkMapper.findByRoomId(roomId, sinceId, limit)
        return talks
    }

    fun getTalkFromId(talkId: Long): Talk {
        val talk = talkMapper.findByTalkId(talkId)
        return talk
    }

    fun createTalk(roomId: Long, userId: Long, text: String): Talk {
        // TODO: check "ユーザーがルームに入っているか"
        val talk = talkMapper.createTalk(roomId, userId, text)
        return talk
    }

    fun updateTalk(userId: Long, talkId: Long, text: String): Talk {
        val talk = this.getTalkFromId(talkId)
        if (talk.user_id == userId) {
            return talkMapper.updateTalk(talkId, text)
        }
        throw BadRequestException("talk creator only can update talk")
    }

    fun deleteTalk(userId: Long, talkId: Long): Boolean {
        val talk = this.getTalkFromId(talkId)
        if (talk.user_id == userId) {
            return talkMapper.deleteTalk(talkId)
        }
        throw BadRequestException("talk creator only can delete talk")
    }
}
