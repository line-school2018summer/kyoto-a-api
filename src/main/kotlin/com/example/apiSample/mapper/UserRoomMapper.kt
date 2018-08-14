package com.example.apiSample.mapper

import com.example.apiSample.model.UserRoom
import org.apache.ibatis.annotations.*

@Mapper
interface UserRoomMapper {
    @Select(
            """
        SELECT user_id, room_id FROM userRooms WHERE user_id=#{userId}
        """
    )
    fun findByUserId(userId: Long): ArrayList<UserRoom>

    @Select(
            """
        SELECT user_id, room_id FROM userRooms WHERE room_id=#{roomId}
        """
    )
    fun findByRoomId(roomId: Long): ArrayList<UserRoom>

    @Insert(
            """
        INSERT INTO userRooms(user_id, room_id) VALUES(#{userId}, #{roomId})
        """
    )
    fun createUserRoom(userId: Long, roomId: Long): UserRoom

    @Delete(
            """
        DELETE FROM userRooms WHERE user_id = #{userId} AND room_id = #{roomId}
        """
    )
    fun deleteUserRoom(userId: Long, roomId: Long): Boolean
}
