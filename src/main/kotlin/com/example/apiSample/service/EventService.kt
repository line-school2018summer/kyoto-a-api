package com.example.apiSample.service

import com.example.apiSample.controller.WebsocketController
import com.example.apiSample.firebase.AuthGateway
import com.example.apiSample.mapper.EventMapper
import com.example.apiSample.model.*
import com.sun.org.apache.xpath.internal.operations.Bool
import org.springframework.stereotype.Service
import java.sql.Timestamp
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
        publishEvent(new_event)
        return new_event
    }

    fun publishEvent(event: Event): Boolean {
        try {
            when (event.event_type) {
                EventTypes.PROFILE_UPDATED.ordinal -> {
                    simpMessagingTemplate?.convertAndSend("/topic/greetings", Message(2, 1, 1, "test", "profile_updated", Timestamp(1L), Timestamp(1L)))
                }
                EventTypes.ROOM_CREATED.ordinal -> {
                    simpMessagingTemplate?.convertAndSend("/topic/greetings", Message(2, 1, 1, "test", "room_created", Timestamp(1L), Timestamp(1L)))
                }
                EventTypes.ROOM_UPDATED.ordinal -> {
                    simpMessagingTemplate?.convertAndSend("/topic/greetings", Message(2, 1, 1, "test", "room_updated", Timestamp(1L), Timestamp(1L)))
                }
                EventTypes.ROOM_MEMBER_JOINED.ordinal -> {
                    simpMessagingTemplate?.convertAndSend("/topic/greetings", Message(2, 1, 1, "test", "room_member_joined", Timestamp(1L), Timestamp(1L)))
                }
                EventTypes.ROOM_MEMBER_LEAVED.ordinal, EventTypes.ROOM_MEMBER_DELETED.ordinal -> {
                    simpMessagingTemplate?.convertAndSend("/topic/greetings", Message(2, 1, 1, "test", "room_member_leaved", Timestamp(1L), Timestamp(1L)))
                }
                EventTypes.MESSAGE_SENT.ordinal -> {
                    simpMessagingTemplate?.convertAndSend("/topic/greetings", Message(2, 1, 1, "test", "message_sent", Timestamp(1L), Timestamp(1L)))
                }
                EventTypes.MESSAGE_UPDATED.ordinal -> {
                    simpMessagingTemplate?.convertAndSend("/topic/greetings", Message(2, 1, 1, "test", "message_updated", Timestamp(1L), Timestamp(1L)))
                }
                EventTypes.MESSAGE_DELETED.ordinal -> {
                    simpMessagingTemplate?.convertAndSend("/topic/greetings", Message(2, 1, 1, "test", "message_deleted", Timestamp(1L), Timestamp(1L)))
                }
            }
        } catch (e: MessagingException) {
            return false
        }
        return true
    }
}
