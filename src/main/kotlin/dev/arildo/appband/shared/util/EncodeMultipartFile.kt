package dev.arildo.appband.shared.util

import org.bson.internal.Base64
import org.springframework.scheduling.annotation.Async
import org.springframework.web.multipart.MultipartException
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID
import java.util.regex.Pattern

open class EncodeMultipartFile(private val base64Image: String, private val bucketUrl: String?) : MultipartFile {
    private val file: File
    private val fileByteArray: ByteArray
    private val destPath = System.getProperty("java.io.tmpdir") + "/"
    private var fileOutputStream: FileOutputStream? = null
    private val name: String

    init {
        name = generateFileName()
        file = File(destPath + name)
        fileByteArray = base64Image.extractByteArray()
    }

    override fun getName(): String {
        return this.name
    }

    fun getImageUrl() = bucketUrl + name

    override fun getContentType() = base64Image.extractMimeType()

    override fun getSize() = fileByteArray.size.toLong()

    override fun getOriginalFilename() = name

    override fun isEmpty() = false

    @Async
    override fun transferTo(dest: File) {
        fileOutputStream = FileOutputStream(dest)
        fileOutputStream?.write(fileByteArray)
    }

    @Throws(MultipartException::class)
    override fun getBytes(): ByteArray {
        return fileByteArray
    }

    @Throws(MultipartException::class)
    override fun getInputStream(): InputStream {
        return ByteArrayInputStream(base64Image.toByteArray())
    }

    private fun String.extractByteArray(): ByteArray {
        val withOutMimeType = replaceBefore(",", "").replace(",", "")
        return Base64.decode(withOutMimeType)
    }

    private fun String.extractMimeType(): String {
        val mime = Pattern.compile("^data:([a-zA-Z0-9]+/[a-zA-Z0-9]+).*,.*")
        val matcher = mime.matcher(this)
        return if (!matcher.find()) "" else matcher.group(1).toLowerCase()
    }

    private fun generateFileName(): String {
        val uuid = UUID.randomUUID().toString()
        val fileExtension = "." + contentType.split("/").last().toLowerCase()
        return uuid + fileExtension
    }
}