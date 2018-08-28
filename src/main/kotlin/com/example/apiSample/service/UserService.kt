package com.example.apiSample.service

import com.example.apiSample.controller.BadRequestException
import com.example.apiSample.firebase.FirebaseGateway
import com.example.apiSample.mapper.UserMapper
import com.example.apiSample.model.NonUidUser
import com.example.apiSample.model.User
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserService(private val userMapper: UserMapper) {

    val logger = LoggerFactory.getLogger(UserService::class.java)

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

        //既にuidが登録されている場合(新規登録でない場合)はエラーが返ってくるのでキャッチする。
        try {
            userMapper.create(uid, name)
        }catch(e : Exception){
            logger.info("error",e)
        }

        val id = userMapper.findByUid(uid)?.id ?: throw BadRequestException("cannot create user.")
        return userMapper.findById(id)
    }

    fun findByUid(uid: String): User {
        return userMapper.findByUid(uid) ?: throw BadRequestException("no user found")
    }
}
