package com.example.apiSample.controller

import com.example.apiSample.firebase.AuthGateway
import com.example.apiSample.firebase.FirebaseGateway
import com.example.apiSample.model.NonUidUser
import com.example.apiSample.service.FileStorage
import com.example.apiSample.service.UserService
import com.example.apiSample.service.RoomService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

@RestController
class  FileUploadRestController(private val userService: UserService,
                                private val roomService: RoomService,
                                private val auth: AuthGateway){

  @Autowired
  lateinit var fileStorage: FileStorage

  @PostMapping(
      value = ["/upload/icon"],
      produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
  )
  fun uploadFile(@RequestHeader("Token")token: String,
                 @RequestBody file: MultipartFile): Boolean /*成功ならtrue*/ {

    val uid = auth.verifyIdToken(token) ?: throw UnauthorizedException("Your token is invalid.")
    val user = userService.findByUid(uid)
    val id = user.id

    if(!file.isEmpty()){
      try{
        userService.deleteIcon(id, "public/img/icon")
        userService.setIcon(id, "public/img/icon", file)
        return true
      }
      catch(t: IOException){
        throw RuntimeException("uploads failed")
      }
    }
    else{
      return false
    }
  }

  @DeleteMapping(
      value = ["upload/icon"],
      produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
  )
  fun deleteIcon(@RequestHeader("Token")token: String): Boolean{
    val uid = auth.verifyIdToken(token) ?: throw UnauthorizedException("Your token is invalid.")
    val user = userService.findByUid(uid) //userService.findById(1)
    val id = user.id

    userService.deleteIcon(id, "public/img/icon")
    return true
  }

  @PostMapping(
          value = ["/upload/icon/room/{id}"],
          produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
  )
  fun uploadFileForRoom(@PathVariable id: Long,
                        @RequestHeader("Token")token: String,
                 @RequestBody file: MultipartFile): Boolean /*成功ならtrue*/ {

    val uid = auth.verifyIdToken(token) ?: throw UnauthorizedException("Your token is invalid.")
    val user = userService.findByUid(uid)

    if (!roomService.isUserExist(user.id, id)) {
      throw BadRequestException("has no permission")
    }

    if(!file.isEmpty()){
      try{
        roomService.deleteIcon(id, "public/img/icon")
        roomService.setIcon(id, "public/img/icon", file)
        return true
      }
      catch(t: IOException){
        throw RuntimeException("uploads failed")
      }
    }
    else{
      return false
    }
  }

  @DeleteMapping(
          value = ["upload/icon/room/{id}"],
          produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
  )
  fun deleteIconForRoom(@PathVariable id: Long, @RequestHeader("Token")token: String): Boolean{
    val uid = auth.verifyIdToken(token) ?: throw UnauthorizedException("Your token is invalid.")
    val user = userService.findByUid(uid) //userService.findById(1)

    if (!roomService.isUserExist(user.id, id)) {
      throw BadRequestException("has no permission")
    }

    roomService.deleteIcon(id, "public/img/icon")
    return true
  }
}