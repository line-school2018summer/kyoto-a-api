package com.example.apiSample.mapper

import com.example.apiSample.model.Talk
import com.example.apiSample.model.TalkList
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

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
        SELECT id, room_id, user_id, `text` FROM talks WHERE room_id=#{roomId}
        """
    )
    fun findByRoomId(roomId: Long): ArrayList<TalkList>
}
