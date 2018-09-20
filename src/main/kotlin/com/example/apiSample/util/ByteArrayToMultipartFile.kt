package com.example.apiSample.util

import org.springframework.web.multipart.MultipartFile
import java.io.*


class ByteArrayToMultipartFile(private val imgContent: ByteArray, private val imgName: String, private val imgFilename: String?, private val imgType: String) : MultipartFile {

    override fun getName(): String {
        return imgName
    }

    override fun getOriginalFilename(): String? {
        return imgFilename
    }

    override fun getContentType(): String? {
        return imgType
    }

    override fun isEmpty(): Boolean {
        return imgContent.isEmpty()
    }

    override fun getSize(): Long {
        return imgContent.size.toLong()
    }

    @Throws(IOException::class)
    override fun getBytes(): ByteArray {
        return imgContent
    }

    @Throws(IOException::class)
    override fun getInputStream(): InputStream {
        return ByteArrayInputStream(imgContent)
    }

    @Throws(IOException::class, IllegalStateException::class)
    override fun transferTo(dest: File) {
        FileOutputStream(dest).write(imgContent)
    }
}