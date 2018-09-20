package com.example.apiSample.util

import org.springframework.web.multipart.MultipartFile
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import kotlin.math.roundToInt

class ImageResizer(private val file: MultipartFile) {
    private val image: ByteArray
    private var ext: String? = null

    init {
        val mime = file.contentType
        mime ?: throw Exception("invalid file")
        println(mime)
        if (!mime.matches(Regex("^image.*"))) {
            throw Exception("unsupported file")
        }
        ext = mime.split("/")[1]
        image = file.bytes
    }

    fun resize(height: Int = 60, width: Int = 60): ByteArray {
        // オリジナルのファイルを読み込む
        val original = ImageIO.read(ByteArrayInputStream(image)) ?: throw Exception("File is invalid")
        val originalWidth  = original.width.toDouble()
        val originalHeight = original.height.toDouble()

        // リサイズ後のファイルを作成
        var resizedWidth  = width.toDouble()
        var resizedHeight = width * originalHeight / originalWidth
        if (height > width) {
            resizedWidth = height * originalWidth / originalHeight
            resizedHeight = height.toDouble()
        }
        val resized = BufferedImage(resizedWidth.roundToInt(), resizedHeight.roundToInt(), original.type)

        // 変換器
        val transformer = AffineTransformOp(
                AffineTransform.getScaleInstance( resizedWidth / originalWidth, resizedHeight / originalHeight)
                , AffineTransformOp.TYPE_BILINEAR
        )

        // 変換
        transformer.filter(original, resized)

        // 変換した結果を書き込む
        val result = ByteArrayOutputStream()
        ImageIO.write(resized, ext ?: "jpg", result)

        return result.toByteArray()
    }
}