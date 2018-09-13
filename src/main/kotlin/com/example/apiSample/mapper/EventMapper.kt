package com.example.apiSample.mapper

import com.example.apiSample.model.Event
import com.example.apiSample.model.InsertEvent
import com.example.apiSample.model.MessageForMapping
import com.example.apiSample.service.InsertMessage
import org.apache.ibatis.annotations.*

@Mapper
interface EventMapper {
    @Select(
        """
        SELECT id, event_type, target_id FROM events where id = #{eventId}
        """
    )
    fun findById(eventId: Long): Event

    @Select(
            """
        SELECT id, event_type, target_id FROM events WHERE event_type=#{event_type} AND id >= #{sinceId} ORDER BY id limit #{limit}
        """
    )
    fun findListById(eventType: Long, sinceId: Long, limit: Int): ArrayList<Event>

    @Select(
            """
        SELECT id, event_type, target_id FROM events WHERE id >= #{sinceId} ORDER BY id limit #{limit}
        """
    )
    fun findListById(sinceId: Long, limit: Int): ArrayList<Event>

    @Insert(
        """
        INSERT INTO events(event_type, target_id) VALUES(#{eventType}, #{targetId})
        """
    )
    @SelectKey(statement = ["select LAST_INSERT_ID()"], keyProperty = "id",
    before = false, resultType = Int::class)
    fun createEvent(event: InsertEvent): Int
}
