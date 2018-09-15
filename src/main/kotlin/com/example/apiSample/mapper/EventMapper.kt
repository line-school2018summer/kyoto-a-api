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
        SELECT id, event_type, room_id, user_id, message_id FROM events where id = #{eventId}
        """
    )
    fun findById(eventId: Long): Event

    @Select(
        """
        SELECT id, event_type, room_id, user_id, message_id FROM events WHERE event_type=#{event_type} AND id >= #{sinceId} ORDER BY id limit #{limit}
        """
    )
    fun findListByType(eventType: Int, sinceId: Long, limit: Int): ArrayList<Event>

    @Select(
        """
            SELECT id, event_type, room_id, user_id, message_id FROM events WHERE event_type IN (6,7,8) AND room_id=#{roomId} AND id >= #{sinceId} ORDER BY id ASC LIMIT #{limit}
        """
    )
    fun findMessagesEventsFromRoomId(roomId: Long, sinceId: Long, limit: Int): ArrayList<Event>

    @Select(
        """
            SELECT id, event_type, room_id, user_id, message_id FROM events WHERE event_type IN (0,1,2,3,4,5) AND room_id=#{roomId} AND id >= #{sinceId} ORDER BY id ASC LIMIT #{limit}
        """
    )
    fun findRoomEventsFromRoomId(roomId: Long, sinceId: Long, limit: Int): ArrayList<Event>

    @Select(
        """
            SELECT id, event_type, room_id, user_id, message_id FROM events WHERE event_type IN (0,1,2,3,4,5) AND room_id IN $""" + """{roomIds} AND id >= #{sinceId} ORDER BY id ASC LIMIT #{limit}
        """
    )
    fun findRoomEventsFromRoomIds(roomIds: String, sinceId: Long, limit: Int): ArrayList<Event>

    @Insert(
        """
        INSERT INTO events(event_type, room_id, user_id, message_id) VALUES(#{event_type}, #{room_id}, #{user_id}, #{message_id})
        """
    )
    @SelectKey(statement = ["select LAST_INSERT_ID()"], keyProperty = "id",
    before = false, resultType = Long::class)
    fun createEvent(event: InsertEvent): Long
}
