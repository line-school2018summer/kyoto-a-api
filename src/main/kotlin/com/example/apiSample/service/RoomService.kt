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
                  private val userMapper: UserMapper) {

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
        val room = this.getRoomFromId(roomInserted.id)
        return room
    }

    fun isUserExist(userId: Long, roomId: Long): Boolean {
        val rooms = roomMapper.findFullRoomByUserId(userId)
        return rooms.contains(roomMapper.findByRoomId(roomId))
    }

    fun addMember(userId: Long, roomId: Long): Long {
        return userRoomMapper.addMember(userId, roomId)
    }

    fun updateRoom(roomId: Long, name: String): Long {
        return roomMapper.updateRoom(roomId, name)
    }

    fun deleteRoom(roomId: Long): Boolean {
        return roomMapper.deleteRoom(roomId)
    }

    fun removeMember(userId: Long, roomId: Long): Boolean {
        return userRoomMapper.removeMember(userId, roomId)
    }

    fun roomFromRoomForMapping(room_for_mapping: RoomForMapping): Room {
        var message: Message? = null

        val room = Room(
                id = room_for_mapping.room_id,
                name = room_for_mapping.room_name,
                last_message = message,
                createdAt = room_for_mapping.room_created_at,
                updatedAt = room_for_mapping.room_updated_at
        )

        message = Message(
                id = room_for_mapping.message_id ?: return room,
                user_id = room_for_mapping.message_user_id ?: return room,
                room_id = room_for_mapping.room_id,
                user = null,
                text = room_for_mapping.message_text ?: return room,
                createdAt = room_for_mapping.message_created_at ?: return room,
                updatedAt = room_for_mapping.message_updated_at ?: return room
        )

        room.last_message = message


        return room
    }
}
