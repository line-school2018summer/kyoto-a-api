package com.example.apiSample.service

import com.example.apiSample.controller.BadRequestException
import com.example.apiSample.mapper.MessageMapper
import com.example.apiSample.model.Message
import com.example.apiSample.model.MessageList
import org.springframework.stereotype.Service

@Service
class MessageService(private val messageMapper: MessageMapper) {
    fun getMessagesFromRoomId(roomId: Long, sinceId: Long = 0, limit: Int = 50): ArrayList<MessageList> {
        val messages = messageMapper.findByRoomId(roomId, sinceId, limit)
        return messages
    }

    fun getMessageFromId(messageId: Long): Message {
        val message = messageMapper.findById(messageId)
        return message
    }

    fun createMessage(roomId: Long, userId: Long, text: String): Message {
        // TODO: check "ユーザーがルームに入っているか"
        val message = messageMapper.createMessage(roomId, userId, text)
        return message
    }

    fun updateMessage(userId: Long, messageId: Long, text: String): Message {
        val message = this.getMessageFromId(messageId)
        if (message.user_id == userId) {
            return messageMapper.updateMessage(messageId, text)
        }
        throw BadRequestException("message creator only can update message")
    }

    fun deleteMessage(userId: Long, messageId: Long): Boolean {
        val message = this.getMessageFromId(messageId)
        if (message.user_id == userId) {
            return messageMapper.deleteMessage(messageId)
        }
        throw BadRequestException("message creator only can delete message")
    }
}
