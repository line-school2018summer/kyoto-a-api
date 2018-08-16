package com.example.apiSample.service

import com.example.apiSample.mapper.UserMapper
import com.example.apiSample.model.User
import com.example.apiSample.model.UserList
import org.springframework.stereotype.Service

@Service
class UserService(private val userMapper: UserMapper) {

    fun findByUid(uid: String): User {
        return userMapper.findByUid(uid)
    }

    //Userのリスト返却
    fun getUserList(): ArrayList<User>{
        return userMapper.getUserList()
    }

    fun findById(id: Long): User{
        return userMapper.findById(id)
    }

    fun updateName(id: Long, changedName: String): Unit{
        userMapper.updateName(id, changedName)
    }
}
