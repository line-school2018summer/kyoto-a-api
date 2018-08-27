package com.example.apiSample.mapper

import com.example.apiSample.model.Room
import com.example.apiSample.model.RoomForMapping
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

    @Select(
            """
        SELECT rooms.id AS room_id, messages.id AS message_id, rooms.name AS room_name,  messages.user_id AS message_user_id, messages.text AS message_text, messages.created_at AS message_created_at, messages.updated_at AS message_updated_at, rooms.created_at AS room_created_at, rooms.updated_at AS room_updated_at FROM rooms LEFT OUTER JOIN users_rooms ON users_rooms.room_id=rooms.id LEFT OUTER JOIN messages ON messages.id = ( SELECT m1.id FROM messages AS m1 WHERE rooms.id=m1.room_id ORDER BY m1.id DESC LIMIT 1 ) WHERE users_rooms.user_id=#{userId}
        """
    )
    fun findFullRoomByUserId(userId: Long): ArrayList<RoomForMapping>

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
