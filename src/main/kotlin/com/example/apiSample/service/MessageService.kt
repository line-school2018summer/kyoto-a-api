package com.example.apiSample.service

import com.example.apiSample.controller.BadRequestException
import com.example.apiSample.mapper.MessageMapper
import com.example.apiSample.model.Message
import com.example.apiSample.model.MessageList
import org.springframework.stereotype.Service

data class InsertMessage (
        var id: Long = 0,
        var room_id: Long,
        var user_id: Long,
        var text: String
)

@Service
class MessageService(private val messageMapper: MessageMapper) {
    fun getMessagesFromRoomId(roomId: Long, sinceId: Long = 0, limit: Int = 50): ArrayList<MessageList> {
        val messages = messageMapper.findByRoomId(roomId, sinceId, limit)
        return messages
    }

    fun getMessageFromId(messageId: Long): Message {
        val message: Message? = messageMapper.findById(messageId)
        message ?: throw BadRequestException("There are no message")
        return message
    }

    fun createMessage(roomId: Long, userId: Long, text: String): Message {
        var messageInserted = InsertMessage(
                room_id = roomId,
                user_id = userId,
                text = text
        )
        // TODO: check "ユーザーがルームに入っているか"
        messageMapper.createMessage(messageInserted)
        val message = this.getMessageFromId(messageInserted.id)
        return message
    }

    fun updateMessage(userId: Long, messageId: Long, text: String): Message {
        val message = this.getMessageFromId(messageId)
        if (message.user_id == userId) {
            messageMapper.updateMessage(messageId, text)
            return this.getMessageFromId(messageId)
        }
        throw BadRequestException("message creator only can update message")
    }

    fun deleteMessage(userId: Long, messageId: Long): Boolean {
        val message: Message = this.getMessageFromId(messageId)
        if (message.user_id == userId) {
            return messageMapper.deleteMessage(messageId)
        }
        throw BadRequestException("message creator only can delete message")
    }
}
