package com.example.apiSample.mapper

import com.example.apiSample.model.MessageForMapping
import com.example.apiSample.model.MessageList
import com.example.apiSample.service.InsertMessage
import org.apache.ibatis.annotations.*

@Mapper
interface MessageMapper {
    @Select(
        """
        SELECT messages.id as message_id, room_id, user_id, `text`, messages.created_at as message_created_at, messages.updated_at as message_updated_at, users.name as user_name, users.created_at as user_created_at, users.updated_at as user_updated_at FROM messages LEFT OUTER JOIN users ON messages.user_id = users.id WHERE messages.id=#{messageId}
        """
    )
    fun findById(messageId: Long): MessageForMapping?

    @Select(
        """
        SELECT messages.id as message_id, room_id, user_id, `text`, messages.created_at as message_created_at, messages.updated_at as message_updated_at, users.name as user_name, users.created_at as user_created_at, users.updated_at as user_updated_at FROM messages LEFT OUTER JOIN users ON messages.user_id = users.id WHERE room_id=#{roomId} AND messages.id >= #{sinceId} ORDER BY message_id LIMIT #{limit}
        """
    )
    fun findByRoomId(roomId: Long, sinceId: Long, limit: Int): ArrayList<MessageForMapping>

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
