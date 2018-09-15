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

    fun getRoomEventsFromRoomId(room_id: Long, since_id: Long?, limit: Int?): ArrayList<Event> {
        return eventMapper.findRoomEventsFromRoomId(
                room_id,
                since_id ?: 0,
                limit ?: 50
        )
    }

    fun getRoomEventsFromRoomIds(room_ids: List<Long>, since_id: Long?, limit: Int?): ArrayList<Event> {
        val roomIdsStr = room_ids.toString().replace('[', '(').replace(']', ')')
        return eventMapper.findRoomEventsFromRoomIds(
                roomIdsStr,
                since_id ?: 0,
                limit ?: 50
        )
    }

    fun getMessageEventsFromRoomId(room_id: Long, since_id: Long?, limit: Int?): ArrayList<Event> {
        return eventMapper.findMessagesEventsFromRoomId(room_id, since_id ?: 0, limit ?: 50)
    }

    fun createEvent(event_type: Int, room_id: Long?, user_id: Long?, message_id: Long?): Event {
        val create_event = InsertEvent(
            event_type = event_type,
            room_id = room_id,
            user_id = user_id,
            message_id = message_id
        )
        val event_id = eventMapper.createEvent(create_event)
        val new_event = Event(
                id = event_id,
                event_type = event_type,
                room_id = room_id,
                user_id = user_id,
                message_id = message_id
        )
        // eventからフック
        return new_event
    }
}
