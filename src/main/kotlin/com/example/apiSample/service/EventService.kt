package com.example.apiSample.service

import com.example.apiSample.mapper.EventMapper
import com.example.apiSample.model.*
import org.springframework.stereotype.Service
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.MessagingException


@Service
class EventService(private val eventMapper: EventMapper) {

    @Autowired
    var simpMessagingTemplate: SimpMessagingTemplate? = null

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
        val createEvent = InsertEvent(
            event_type = event_type,
            room_id = room_id,
            user_id = user_id,
            message_id = message_id
        )
        val eventId = eventMapper.createEvent(createEvent)
        val newEvent = Event(
                id = eventId,
                event_type = event_type,
                room_id = room_id,
                user_id = user_id,
                message_id = message_id
        )
        publishEvent(newEvent)
        return newEvent
    }

    fun publishEvent(event: Event): Boolean {
        val messageTemplate = simpMessagingTemplate ?: return false
        try {
            when (event.event_type) {
                EventTypes.PROFILE_UPDATED.ordinal -> {
                    messageTemplate.convertAndSend("/topic/users", event)
                }
                EventTypes.ROOM_CREATED.ordinal -> {
                    messageTemplate.convertAndSend("/topic/rooms", event)
                }
                EventTypes.ROOM_UPDATED.ordinal -> {
                    messageTemplate.convertAndSend("/topic/rooms", event)
                }
                EventTypes.ROOM_MEMBER_JOINED.ordinal -> {
                    messageTemplate.convertAndSend("/topic/rooms", event)
                }
                EventTypes.ROOM_MEMBER_LEAVED.ordinal, EventTypes.ROOM_MEMBER_DELETED.ordinal -> {
                    messageTemplate.convertAndSend("/topic/rooms", event)
                }
                EventTypes.MESSAGE_SENT.ordinal -> {
                    messageTemplate.convertAndSend("/topic/rooms/" + event.room_id.toString() + "/messages", event)
                }
                EventTypes.MESSAGE_UPDATED.ordinal -> {
                    messageTemplate.convertAndSend("/topic/rooms/" + event.room_id.toString() + "/messages", event)
                }
                EventTypes.MESSAGE_DELETED.ordinal -> {
                    messageTemplate.convertAndSend("/topic/rooms/" + event.room_id.toString() + "/messages", event)
                }
            }
        } catch (e: MessagingException) {
            return false
        }
        return true
    }
}
