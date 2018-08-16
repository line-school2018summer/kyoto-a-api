package com.example.apiSample.mapper

import com.example.apiSample.model.Room
import com.example.apiSample.model.RoomList
import com.example.apiSample.service.InsertRoom
import org.apache.ibatis.annotations.*

@Mapper
interface RoomMapper {
    @Select(
            """
        SELECT id, `name`, created_at, updated_at FROM rooms WHERE id=#{roomId}
        """
    )
    fun findByRoomId(roomId: Long): Room?

    @Select(
            """
        SELECT rooms.id, rooms.`name`, created_at, updated_at FROM users_rooms LEFT JOIN rooms ON users_rooms.room_id=rooms.id WHERE users_rooms.user_id=#{userId}
        """
    )
    fun findByUserId(userId: Long): ArrayList<Room>

    @Insert(
            """
        INSERT INTO rooms(`name`) VALUES(#{name})
        """
    )
    @SelectKey(
            statement=["select LAST_INSERT_ID()"], keyProperty="id", before=false, resultType=Int::class
    )
    fun createRoom(room: InsertRoom): Long

    @Update(
            """
        UPDATE rooms SET `name` = #{name} WHERE id = #{roomId}
        """
    )
    fun updateRoom(roomId: Long, name: String): Long

    @Delete(
            """
        DELETE FROM rooms WHERE id = #{roomId}
        """
    )
    fun deleteRoom(roomId: Long): Boolean
}
