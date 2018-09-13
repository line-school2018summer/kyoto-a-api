package com.example.apiSample.service

import com.example.apiSample.mapper.EventMapper
import com.example.apiSample.model.*
import org.springframework.stereotype.Service

@Service
class EventService(private val eventMapper: EventMapper) {
    fun getEventFromId(eventId: Long): Event {
        return eventMapper.findById(eventId)
    }

    fun getEventsFromType(eventType: Int, since_id: Long = 0, limit: Int = 50): ArrayList<Event> {
        return eventMapper.findListByType(eventType, since_id, limit)
    }

    fun getRoomEventsFromRoomId(room_id: Long, since_id: Long = 0, limit: Int = 50): ArrayList<Event> {
        return eventMapper.findRoomEventsFromRoomId(
                room_id,
                since_id,
                limit
        )
    }

    fun getMessageEventsFromRoomId(room_id: Long, since_id: Long = 0, limit: Int = 50): ArrayList<Event> {
        return eventMapper.findMessagesEventsFromRoomId(room_id, since_id, limit)
    }

    fun createEvent(event_type: Int, room_id: Long?, user_id: Long?, message_id: Long?): Event {
        val new_event = InsertEvent(
            event_type = event_type,
            room_id = room_id,
            user_id = user_id,
            message_id = message_id
        )
        val event_id = eventMapper.createEvent(new_event)
        return Event(
                id = event_id,
                event_type = event_type,
                room_id = room_id,
                user_id = user_id,
                message_id = message_id
        )
    }
}
