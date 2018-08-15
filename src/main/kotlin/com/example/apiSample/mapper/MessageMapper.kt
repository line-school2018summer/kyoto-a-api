package com.example.apiSample.mapper

import com.example.apiSample.model.Message
import com.example.apiSample.model.MessageList
import org.apache.ibatis.annotations.*

@Mapper
interface MessageMapper {
    @Select(
        """
        SELECT id, room_id, user_id, `text`, created_at, updated_at FROM messages WHERE id=#{messageId}
        """
    )
    fun findById(messageId: Long): Message

    @Select(
        """
        SELECT id, room_id, user_id, `text` FROM messages WHERE room_id=#{roomId} AND id > #{sinceId} LIMIT #{limit}
        """
    )
    fun findByRoomId(roomId: Long, sinceId: Long = 0, limit: Int = 50): ArrayList<MessageList>

    @Insert(
        """
        INSERT INTO messages(room_id, user_id, `text`) VALUES(#{roomId}, #{userId}, #{text})
        """
    )
    fun createMessage(roomId: Long, userId: Long, text: String): Message

    @Update(
        """
        UPDATE messages SET `text` = #{text} WHERE id = #{messageId}
        """
    )
    fun updateMessage(messageId: Long, text: String): Message

    @Delete(
        """
        DELETE FROM messages WHERE id = #{messageId}
        """
    )
    fun deleteMessage(messageId: Long): Boolean
}
