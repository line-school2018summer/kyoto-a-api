package com.example.apiSample.mapper

import com.example.apiSample.model.Talk
import com.example.apiSample.model.TalkList
import org.apache.ibatis.annotations.*

@Mapper
interface TalkMapper {
    @Select(
        """
        SELECT id, room_id, user_id, `text`, created_at, updated_at FROM talks WHERE id=#{talkId}
        """
    )
    fun findByTalkId(talkId: Long): Talk

    @Select(
        """
        SELECT id, room_id, user_id, `text` FROM talks WHERE room_id=#{roomId} AND id > #{sinceId} LIMIT #{limit}
        """
    )
    fun findByRoomId(roomId: Long, sinceId: Long = 0, limit: Int = 50): ArrayList<TalkList>

    @Insert(
        """
        INSERT INTO talks(room_id, user_id, `text`) VALUES(#{roomId}, #{userId}, #{text})
        """
    )
    fun createTalk(roomId: Long, userId: Long, text: String): Talk

    @Update(
        """
        UPDATE talks SET `text` = #{text} WHERE id = #{talkId}
        """
    )
    fun updateTalk(talkId: Long, text: String): Talk

    @Delete(
        """
        DELETE FROM talks WHERE id = #{talkId}
        """
    )
    fun deleteTalk(talkId: Long): Boolean
}
