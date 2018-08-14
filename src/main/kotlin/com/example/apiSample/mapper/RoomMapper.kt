package com.example.apiSample.mapper

import com.example.apiSample.model.Room
import com.example.apiSample.model.RoomList
import org.apache.ibatis.annotations.*

@Mapper
interface RoomMapper {
    @Select(
            """
        SELECT id, name, created_at, updated_at FROM rooms WHERE id=#{roomId}
        """
    )
    fun findByRoomId(roomId: Long): Room

    @Select(
            """
        SELECT rooms.id, rooms.name FROM userRooms left join rooms on userRooms.room_id=rooms.id WHERE userRooms.user_id=#{userId}
        """
    )
    fun findByUserId(userId: Long): ArrayList<RoomList>

    @Insert(
            """
        INSERT INTO rooms(name) VALUES(#{name})
        """
    )
    fun createRoom(name: String): Room

    @Update(
            """
        UPDATE rooms SET name = #{name} WHERE id = #{roomId}
        """
    )
    fun updateRoom(roomId: Long, name: String): Room

    @Delete(
            """
        DELETE FROM rooms WHERE id = #{roomId}
        """
    )
    fun deleteRoom(roomId: Long): Boolean
}
