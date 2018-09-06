package com.example.apiSample.controller

import com.example.apiSample.service.FileStorage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

@RestController
class  FileUploadRestController{

  @Autowired
  lateinit var fileStorage: FileStorage

  @PostMapping(
      value = ["/upload"],
      produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
  )
  fun uploadFile(@RequestParam("file") file : MultipartFile): Boolean{

    if(!file.isEmpty()){
      try{
        fileStorage.store(file)
        return true
      }
      catch(t: IOException){
        throw RuntimeException("Error uploading file.")
      }
    }
    else{
      return false
    }
  }
}