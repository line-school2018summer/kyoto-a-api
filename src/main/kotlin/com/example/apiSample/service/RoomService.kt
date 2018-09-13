package com.example.apiSample.service

import com.example.apiSample.controller.BadRequestException
import com.example.apiSample.mapper.UserMapper
import com.example.apiSample.mapper.RoomMapper
import com.example.apiSample.mapper.UserRoomMapper
import com.example.apiSample.model.*
import org.springframework.stereotype.Service

data class InsertRoom (
        var id: Long = 0,
        var name: String
)

@Service
class RoomService(private val roomMapper: RoomMapper,
                  private val userRoomMapper: UserRoomMapper,
                  private val userMapper: UserMapper,
                  private val eventService: EventService) {

    fun getRoomFromId(roomId: Long): Room {
        val room: Room = roomFromRoomForMapping(roomMapper.findByRoomId(roomId) ?: throw BadRequestException("no room found"))
        return room
    }

    fun getRoomsFromUserId(userId: Long): ArrayList<Room> {
        val roomsForMapping = roomMapper.findFullRoomByUserId(userId)
        val rooms = roomsForMapping.map { roomFromRoomForMapping(it) } as ArrayList
        return rooms
    }

    fun getUserRoom(userId: Long, roomId: Long): ArrayList<UserRoom> {
        val userRoom = userRoomMapper.findByUserAndRoomId(userId, roomId)
        return userRoom
    }

    fun getMembers(roomId: Long): ArrayList<UserList> {
        val members = userMapper.findByRoomId(roomId)
        return members
    }

    fun createRoom(name: String): Room {
        var roomInserted = InsertRoom(
                name = name
        )
        roomMapper.createRoom(roomInserted)
        eventService.createEvent(EventTypes.ROOM_CREATED.ordinal, roomInserted.id, null, null)
        val room = this.getRoomFromId(roomInserted.id)
        return room
    }

    fun isUserExist(userId: Long, roomId: Long): Boolean {
        val rooms = roomMapper.findFullRoomByUserId(userId)
        return rooms.contains(roomMapper.findByRoomId(roomId))
    }

    fun addMember(userId: Long, roomId: Long): Long {
        eventService.createEvent(EventTypes.ROOM_MEMBER_JOINED.ordinal, roomId, userId, null)
        return userRoomMapper.addMember(userId, roomId)
    }

    fun updateRoom(roomId: Long, name: String): Long {
        eventService.createEvent(EventTypes.ROOM_UPDATED.ordinal, roomId, null, null)
        return roomMapper.updateRoom(roomId, name)
    }

    fun deleteRoom(roomId: Long): Boolean {
        return roomMapper.deleteRoom(roomId)
    }

    fun removeMember(userId: Long, roomId: Long): Boolean {
        eventService.createEvent(EventTypes.ROOM_MEMBER_LEAVED.ordinal, roomId, userId, null)
        return userRoomMapper.removeMember(userId, roomId)
    }

    fun roomFromRoomForMapping(room_for_mapping: RoomForMapping): Room {

        val room = Room(
                id = room_for_mapping.room_id,
                name = room_for_mapping.room_name,
                last_message_text = room_for_mapping.message_text,
                last_message_created_at = room_for_mapping.message_created_at,
                createdAt = room_for_mapping.room_created_at,
                updatedAt = room_for_mapping.room_updated_at
        )




        return room
    }
}
