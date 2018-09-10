package com.example.apiSample.controller

import com.example.apiSample.firebase.AuthGateway
import com.example.apiSample.firebase.FirebaseGateway
import com.example.apiSample.model.NonUidUser
import com.example.apiSample.service.FileStorage
import com.example.apiSample.service.UserService
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
                                private val auth: AuthGateway){

  @Autowired
  lateinit var fileStorage: FileStorage

  @PostMapping(
      value = ["/upload/icon"],
      produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
  )
  fun uploadFile(@RequestHeader("Token")token: String,
                 @RequestBody file: MultipartFile): Boolean /*成功ならtrue*/ {

    //val uid = auth.verifyIdToken(token) ?: throw UnauthorizedException("Your token is invalid.")
    //val user = userService.findByUid(uid)
    val id = 1L//user.id

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
    //val uid = auth.verifyIdToken(token) ?: throw UnauthorizedException("Your token is invalid.")
    //val user = userService.findByUid(uid) //userService.findById(1)
    val id = 1L//user.id

    userService.deleteIcon(id, "public/img/icon")
    return true
  }
}