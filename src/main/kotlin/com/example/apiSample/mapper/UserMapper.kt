package com.example.apiSample.mapper

import com.example.apiSample.model.User
import com.example.apiSample.model.UserProfile
import com.example.apiSample.model.UserList
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface UserMapper {

    //全ユーザのリストを返します
    @Select(
        """
        SELECT id, uid, name, created_at, updated_at FROM Users
        """
    )
    fun getUserList(): ArrayList<User>

    @Update(
        """
        UPDATE Users SET name=#{changedName} WHERE uid=#{uid}
        """
    )
    fun updateName(uid: String, changedName: String): Unit

    @Select(
        """
        SELECT id, uid, name, created_at, updated_at FROM users WHERE uid = #{uid}
        """
    )
    fun findByUid(uid: String): User

    @Select(
        """
        SELECT id, uid, name, created_at, updated_at FROM users WHERE id=#{id}
        """
    )
    fun findById(id: Long): User
}
