package com.example.apiSample.mapper

import com.example.apiSample.model.Message
import com.example.apiSample.model.MessageList
import com.example.apiSample.service.InsertMessage
import org.apache.ibatis.annotations.*

@Mapper
interface MessageMapper {
    @Select(
        """
        SELECT id, room_id, user_id, `text`, created_at, updated_at FROM messages WHERE id=#{messageId}
        """
    )
    fun findById(messageId: Long): Message?

    @Select(
        """
        SELECT id, room_id, user_id, `text` FROM messages WHERE room_id=#{roomId} AND id >= #{sinceId} LIMIT #{limit}
        """
    )
    fun findByRoomId(roomId: Long, sinceId: Long, limit: Int): ArrayList<MessageList>

    @Insert(
        """
        INSERT INTO messages(room_id, user_id, `text`) VALUES(#{room_id}, #{user_id}, #{text})
        """
    )
    @SelectKey(statement = ["select LAST_INSERT_ID()"], keyProperty = "id",
    before = false, resultType = Int::class)
    fun createMessage(message: InsertMessage): Int

    @Update(
        """
        UPDATE messages SET `text` = #{text} WHERE id = #{messageId}
        """
    )
    fun updateMessage(messageId: Long, text: String): Int

    @Delete(
        """
        DELETE FROM messages WHERE id = #{messageId}
        """
    )
    fun deleteMessage(messageId: Long): Boolean
}
