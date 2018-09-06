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
  /*ファイル操作をします
   *多くの関数の引数にあるlocationではパスを指定します
   */

  //ファイルの保存をする関数です
  fun store(file: MultipartFile, location: String, filename: String){
    Files.copy(file.getInputStream(), Paths.get(location).resolve(filename))
  }

  //ファイルを読み込みます
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

  val regex = Regex("\\.[a-z]+$")

  //ファイルの拡張子を返します(.を含みます)
  fun checkFileType(file: String): String{
    val extention = regex.find(file)?.value ?: throw Exception("this is invalid")
    return extention
  }
  fun checkFileType(file: MultipartFile): String{
    val name = file.originalFilename ?: throw Exception("this is invalid")
    return checkFileType(name)
  }
}