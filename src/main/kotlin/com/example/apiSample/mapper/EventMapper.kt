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

    @Insert(
        """
        INSERT INTO events(event_type, target_id) VALUES(#{eventType}, #{targetId})
        """
    )
    @SelectKey(statement = ["select LAST_INSERT_ID()"], keyProperty = "id",
    before = false, resultType = Int::class)
    fun createEvent(event: InsertEvent): Int
}
