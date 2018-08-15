package com.example.apiSample.mapper

import com.example.apiSample.model.UserProfile
import com.example.apiSample.model.User
import com.example.apiSample.model.UserList
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface UserMapper {
    @Select(
        """
        SELECT id, name, email, created_at, updated_at FROM users WHERE id=#{userId}
        """
    )
    fun findByUserId(userId: Long): UserProfile

    @Select(
        """
        SELECT id, uid, name FROM users WHERE name LIKE CONCAT('%', #{searchStr}, '%')
        """
    )
    fun findBySearchStr(searchStr: String): ArrayList<UserList>

    @Select(
            """
        SELECT id, uid, name, created_at, updated_at FROM users WHERE uid = #{uid}
        """
    )
    fun findByUid(uid: String): User
}
