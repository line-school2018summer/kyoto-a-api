package com.example.apiSample.service

import com.example.apiSample.mapper.UserMapper
import com.example.apiSample.mapper.RoomMapper
import com.example.apiSample.mapper.UserRoomMapper
import com.example.apiSample.model.UserList
import com.example.apiSample.model.Room
import com.example.apiSample.model.RoomList
import com.example.apiSample.model.UserRoom
import org.springframework.stereotype.Service

@Service
class RoomService(private val roomMapper: RoomMapper,
                  private val userRoomMapper: UserRoomMapper,
                  private val userMapper: UserMapper) {

    fun getRoomFromId(roomId: Long): Room {
        val room = roomMapper.findByRoomId(roomId)
        return room
    }

    fun getRoomsFromUserId(userId: Long): ArrayList<RoomList> {
        val rooms = roomMapper.findByUserId(userId)
        return rooms
    }

    fun getMembers(roomId: Long): ArrayList<UserList> {
        val members = userMapper.findByRoomId(roomId)
        return members
    }

    fun createRoom(name: String): Room {
        val room = roomMapper.createRoom(name)
        return room
    }

    fun addMember(userId: Long, roomId: Long): UserRoom {
        val userRoom = userRoomMapper.addMember(userId, roomId)
        return userRoom
    }

    fun updateRoom(roomId: Long, name: String): Room {
        val room = roomMapper.updateRoom(roomId, name)
        return room
    }

    fun deleteRoom(roomId: Long): Boolean {
        return roomMapper.deleteRoom(roomId)
    }

    fun removeMember(userId: Long, roomId: Long): Boolean {
        return userRoomMapper.removeMember(userId, roomId)
    }
}
