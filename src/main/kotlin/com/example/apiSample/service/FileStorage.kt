package com.example.apiSample.service

import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths;
import java.util.stream.Stream

@Service
class FileStorage(){

  fun store(file: MultipartFile, location: String, filename: Int){
    Files.copy(file.getInputStream(), Paths.get(location).resolve(filename.toString()))
  }

  fun loadFile(filename: String, location: String): Resource {
    val file = Paths.get(location).resolve(filename)
    val resource = UrlResource(file.toUri())

    if(resource.exists() || resource.isReadable()) {
      return resource
    }else{
      throw RuntimeException("FAIL!")
    }
  }

  fun deleteAll(location: String){
    FileSystemUtils.deleteRecursively(Paths.get(location).toFile())
  }

  fun loadFiles(location: String): Stream<Path> {
    val p = Paths.get(location)
    return Files.walk(p, 1)
        .filter{path -> !path.equals(p)}
        .map(p::relativize)
  }

}