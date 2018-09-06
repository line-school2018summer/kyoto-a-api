package com.example.apiSample.controller

import com.example.apiSample.service.FileStorage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class FileDownloadsRestController{

  @Autowired
  lateinit var fileStorage: FileStorage

  @GetMapping("/downloads/{id}")
  fun downloadFile(@PathVariable id: Int): ResponseEntity<Resource> {
    val file = fileStorage.loadFile("#{id}.", "public/img/")
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
        .body(file);
  }
}