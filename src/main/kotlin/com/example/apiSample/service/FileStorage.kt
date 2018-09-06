package com.example.apiSample.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths;

@Component
class FileStorage{

  fun init(){
    Files.createDirectory(rootLocation)
  }

  val log = LoggerFactory.getLogger(this::class.java)
  val rootLocation = Paths.get("filestorage")

  fun store(file: MultipartFile){
    Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()))
  }

}