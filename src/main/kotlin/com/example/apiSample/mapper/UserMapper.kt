package com.example.apiSample.mapper

import com.example.apiSample.model.NonUidUser
import com.example.apiSample.model.User
import com.example.apiSample.model.UserList
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface UserMapper {

    //全ユーザのリストを返します
    @Select(
        """
        SELECT id, name, created_at, updated_at FROM users
        """
    )
    fun getUserList(): ArrayList<NonUidUser>

    @Update(
        """
        UPDATE users SET name=#{changedName} WHERE id=#{id}
        """
    )
    fun updateName(id: Long, changedName: String): Unit

    @Select(
        """
        SELECT id, uid, name, created_at, updated_at FROM users WHERE uid = #{uid}
        """
    )
    fun findByUid(uid: String): User?

    @Select(
        """
        SELECT id, name, created_at, updated_at FROM users WHERE id=#{id}
        """
    )
    fun findById(id: Long): NonUidUser

    @Insert(
        """
        INSERT INTO users (uid, name) VALUES ( #{uid}, #{name})
        """
    )
    fun create(uid: String, name: String)

    @Select(
        """
        SELECT users.id, users.`name` FROM users_rooms LEFT JOIN users ON users_rooms.user_id=users.id WHERE users_rooms.room_id=#{roomId}
        """
    )
    fun findByRoomId(roomId: Long): ArrayList<UserList>
}