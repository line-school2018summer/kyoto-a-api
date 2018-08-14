package com.example.apiSample.service

import com.example.apiSample.mapper.RoomMapper
import com.example.apiSample.mapper.UserMapper
import com.example.apiSample.model.Room
import com.example.apiSample.model.RoomList
import com.example.apiSample.model.UserList
import org.springframework.stereotype.Service

@Service
class RoomService(private val roomMapper: RoomMapper) {

    fun getRoomFromId(roomId: Long): Room {
        val room = roomMapper.findByRoomId(roomId)
        return room
    }

    fun getRoomsFromUserId(userId: Long): ArrayList<RoomList> {
        val rooms = roomMapper.findByUserId(userId)
        return rooms
    }

    fun createRoom(userId: Long, name: String): Room {
        val room = roomMapper.createRoom(userId, name)
        return room
    }

    fun updateRoom(roomId: Long, name: String): Room {
        val room = roomMapper.updateRoom(roomId, name)
        return room
    }

    fun deleteRoom(roomId: Long): Boolean {
        return roomMapper.deleteRoom(roomId)
    }
}
