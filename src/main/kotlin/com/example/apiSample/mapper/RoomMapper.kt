package com.example.apiSample.mapper

import com.example.apiSample.model.Room
import com.example.apiSample.model.RoomList
import org.apache.ibatis.annotations.*

@Mapper
interface RoomMapper {
    @Select(
            """
        SELECT id, `name`, created_at, updated_at FROM Rooms WHERE id=#{roomId}
        """
    )
    fun findByRoomId(roomId: Long): Room

    @Select(
            """
        SELECT Rooms.id, Rooms.`name` FROM UserRooms LEFT JOIN Rooms ON UserRooms.room_id=Rooms.id WHERE UserRooms.user_id=#{userId}
        """
    )
    fun findByUserId(userId: Long): ArrayList<RoomList>

    @Insert(
            """
        INSERT INTO Rooms(`name`) VALUES(#{name})
        """
    )
    fun createRoom(name: String): Room

    @Update(
            """
        UPDATE Rooms SET `name` = #{name} WHERE id = #{roomId}
        """
    )
    fun updateRoom(roomId: Long, name: String): Room

    @Delete(
            """
        DELETE FROM Rooms WHERE id = #{roomId}
        """
    )
    fun deleteRoom(roomId: Long): Boolean
}
