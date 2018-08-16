package com.example.apiSample.service

import com.example.apiSample.controller.BadRequestException
import com.example.apiSample.mapper.UserMapper
import com.example.apiSample.model.NonUidUser
import com.example.apiSample.model.User
import com.example.apiSample.model.UserList
import org.springframework.stereotype.Service

@Service
class UserService(private val userMapper: UserMapper) {

    //Userのリスト返却
    fun getUserList(): ArrayList<NonUidUser>{
        return userMapper.getUserList()
    }

    fun findById(id: Long): NonUidUser{
        return userMapper.findById(id)
    }

    fun updateName(id: Long, changedName: String): NonUidUser{
        userMapper.updateName(id, changedName)
        return userMapper.findById(id)
    }

    fun create(uid: String, name: String): NonUidUser{
        userMapper.create(uid, name)
        val id = userMapper.findByUid(uid).id
        return userMapper.findById(id)
    }

    fun findByUid(uid: String): User {
        return userMapper.findByUid(uid) ?: throw BadRequestException("no user found")
    }
}
