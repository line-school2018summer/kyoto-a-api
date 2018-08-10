package com.example.apiSample.service

import com.example.apiSample.mapper.UserMapper
import com.example.apiSample.model.UserList
import org.springframework.stereotype.Service

@Service
class UserService(private val userMapper: UserMapper) {

    fun findUsersList(searchStr: String): ArrayList<UserList> {
        return userMapper.findBySearchStr(searchStr)
    }
}
