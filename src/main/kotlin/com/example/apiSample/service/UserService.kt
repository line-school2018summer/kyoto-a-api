package com.example.apiSample.service

import com.example.apiSample.controller.BadRequestException
import com.example.apiSample.mapper.UserMapper
import com.example.apiSample.model.NonUidUser
import com.example.apiSample.model.User
import com.example.apiSample.model.UserList
import com.example.apiSample.model.UserRoom
import org.springframework.stereotype.Service

@Service
class UserService(private val userMapper: UserMapper) {

    val regex = Regex("[[ぁ-んァ-ヶ亜-熙] \\w ー 。 、]+")

    //Userのリスト返却
    fun getUserList(): ArrayList<NonUidUser>{
        return userMapper.getUserList()
    }

    fun findById(id: Long): NonUidUser{
        return userMapper.findById(id)
    }

    fun updateName(id: Long, changedName: String): NonUidUser{

        if (regex.matches(changedName)){
            userMapper.updateName(id, changedName)
            return userMapper.findById(id)
        }
        else{
            throw Exception("the name has invalid literal.")
        }
    }

    fun create(uid: String, name: String): NonUidUser{
        userMapper.create(uid, name)
        val id = userMapper.findByUid(uid)?.id ?: throw BadRequestException("cannot create user.")
        return userMapper.findById(id)
    }

    fun findByUid(uid: String): User {
        return userMapper.findByUid(uid) ?: throw BadRequestException("no user found")
    }

    fun searchUser(str: String): List<NonUidUser>{
        val userList = userMapper.getUserList()
        val regex = Regex(str)
        var list :MutableList<NonUidUser> = mutableListOf()
        for (user in userList) {
            if(regex.containsMatchIn(user.name)) {
                list.add(user.copy())
            }
        }
        return list
    }
}
