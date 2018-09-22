package com.example.apiSample.service

import com.example.apiSample.controller.BadRequestException
import com.example.apiSample.firebase.FirebaseGateway
import com.example.apiSample.mapper.UserMapper
import com.example.apiSample.model.*
import com.example.apiSample.util.ByteArrayToMultipartFile
import com.example.apiSample.util.ImageResizer
import org.apache.ibatis.jdbc.Null
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths

@Service
class UserService(private val userMapper: UserMapper, private val eventService: EventService) {

    @Autowired
    lateinit var fileStorage: FileStorage

    val regex = Regex("[[ぁ-んァ-ヶ亜-熙] \\w ー 。 、]+")

    val logger = LoggerFactory.getLogger(UserService::class.java)


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
            eventService.createEvent(EventTypes.PROFILE_UPDATED.ordinal, null, id, null)
            return userMapper.findById(id)
        }
        else{
            throw Exception("the name has invalid literal.")
        }
    }

    fun getIconUser(id: Long): IconUser {
        return userMapper.getIconUser(id)
    }

    fun setIcon(id: Long, location: String, file: MultipartFile){
        val resized_image = ImageResizer(file.bytes).resize()
        val type = fileStorage.checkFileType(file)
        val resized_file = ByteArrayToMultipartFile(resized_image, file.name, file.originalFilename, type)
        val fileName = id.toString() + type
        fileStorage.store(resized_file, location, fileName)
        userMapper.setIcon(id, fileName)
        eventService.createEvent(EventTypes.PROFILE_UPDATED.ordinal, null, id, null)
    }

    fun deleteIcon(id: Long, location: String){
        val icon = getIconUser(id).icon
        if(icon != null) {
            val prevFile = Paths.get(location).resolve(icon)
            userMapper.setIcon(id, null)
            Files.delete(prevFile)
            eventService.createEvent(EventTypes.PROFILE_UPDATED.ordinal, null, id, null)
        }
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
