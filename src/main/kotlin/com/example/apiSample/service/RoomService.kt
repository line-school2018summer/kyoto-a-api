package com.example.apiSample.service

import com.example.apiSample.controller.BadRequestException
import com.example.apiSample.mapper.UserMapper
import com.example.apiSample.mapper.RoomMapper
import com.example.apiSample.mapper.UserRoomMapper
import com.example.apiSample.model.UserList
import com.example.apiSample.model.Room
import com.example.apiSample.model.RoomList
import com.example.apiSample.model.UserRoom
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
        val room: Room? = roomMapper.findByRoomId(roomId)
        room ?: throw BadRequestException("There is no room")
        return room
    }

    fun getRoomsFromUserId(userId: Long): ArrayList<RoomList> {
        val rooms = roomMapper.findByUserId(userId)
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
}
