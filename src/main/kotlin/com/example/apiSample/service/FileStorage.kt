package com.example.apiSample.service

import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile
import java.awt.Graphics
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths;
import java.util.stream.Stream
import java.io.InputStream;
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.Image
import java.io.ByteArrayOutputStream

@Service
  class FileStorage(){

  /*
   *ファイル操作をします
   *多くの関数の引数にあるlocationではパスを指定します
   */

  //ファイルの保存をする関数です
  fun store(file: MultipartFile, location: String, filename: String){
    Files.copy(file.getInputStream(), Paths.get(location, filename))
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
  fun checkFileType(file: String, needPeriod: Boolean = true): String{
    var extention = regex.find(file)?.value ?: throw Exception("this is invalid")
    if (!needPeriod){
      extention =  extention.substring(1)
    }
    return extention
  }
  fun checkFileType(file: MultipartFile, needPeriod: Boolean = true): String{
    val name: String = file.originalFilename ?: throw Exception("this is invalid")
    return checkFileType(name, needPeriod)
  }

/*
  fun reduceImg(fileName: String, location: String, width: Int = 100, height: Int = 100): MultipartFile {

    // リソースファイルを読み込み
    val resource: Resource = loadFile(fileName , location)
    val image: InputStream = resource.getInputStream()

    // BufferedImageへ設定
    val bufferedImage1: BufferedImage = ImageIO.read(image)
    lateinit var bufferedImage2: BufferedImage

    // 変換情報を設定
    val image1: Image = bufferedImage1.getScaledInstance(width, height, Image.SCALE_DEFAULT)
    bufferedImage2 = BufferedImage(image1.getWidth(null), image1.getHeight(null), BufferedImage.TYPE_INT_RGB)

    // 縮小処理
    lateinit var graphics2: Graphics
    try{
      graphics2 = bufferedImage2.getGraphics()
      graphics2.drawImage(bufferedImage1, 0, 0, width, height, null)
    }finally {
      graphics2.dispose()
    }

    var baos: ByteArrayOutputStream = ByteArrayOutputStream()
    ImageIO.write( bufferedImage2, checkFileType(fileName, false), baos )
    baos.flush()
    val file: MultipartFile = MultipartFile(fileName, baos.toByteArray())
    return file
  }
  */
}