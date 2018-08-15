package com.example.apiSample.service

import com.example.apiSample.controller.BadRequestException
import com.example.apiSample.mapper.MessageMapper
import com.example.apiSample.model.Message
import com.example.apiSample.model.MessageList
import org.springframework.stereotype.Service

@Service
class MessageService(private val messageMapper: MessageMapper) {
    fun getTalksFromRoomId(roomId: Long, sinceId: Long = 0, limit: Int = 50): ArrayList<MessageList> {
        val talks = messageMapper.findByRoomId(roomId, sinceId, limit)
        return talks
    }

    fun getTalkFromId(talkId: Long): Message {
        val talk = messageMapper.findById(talkId)
        return talk
    }

    fun createTalk(roomId: Long, userId: Long, text: String): Message {
        // TODO: check "ユーザーがルームに入っているか"
        val talk = messageMapper.createMessage(roomId, userId, text)
        return talk
    }

    fun updateTalk(userId: Long, talkId: Long, text: String): Message {
        val talk = this.getTalkFromId(talkId)
        if (talk.user_id == userId) {
            return messageMapper.updateMessage(talkId, text)
        }
        throw BadRequestException("talk creator only can update talk")
    }

    fun deleteTalk(userId: Long, talkId: Long): Boolean {
        val talk = this.getTalkFromId(talkId)
        if (talk.user_id == userId) {
            return messageMapper.deleteMessage(talkId)
        }
        throw BadRequestException("talk creator only can delete talk")
    }
}
