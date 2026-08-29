package com.smartexpense.api.application.usecase.file

import com.smartexpense.api.application.port.`in`.FileUseCase
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.UUID

@Service
class FileUseCaseImpl : FileUseCase {

    private val uploadDir = "uploads"

    init {
        val directory = File(uploadDir)
        if (!directory.exists()) {
            directory.mkdirs()
        }
    }

    override fun uploadFile(file: MultipartFile): String {
        if (file.isEmpty) {
            throw IllegalArgumentException("File is empty")
        }
        val originalFilename = file.originalFilename ?: "unknown.file"
        val extension = originalFilename.substringAfterLast('.', "")
        val newFilename = "${UUID.randomUUID()}.$extension"
        val filePath = Paths.get(uploadDir, newFilename)

        Files.copy(file.inputStream, filePath, StandardCopyOption.REPLACE_EXISTING)

        return "/uploads/$newFilename"
    }

    override fun deleteFile(fileUrl: String) {
        if (fileUrl.isBlank()) return
        val filename = fileUrl.substringAfterLast("/")
        val filePath = Paths.get(uploadDir, filename)
        Files.deleteIfExists(filePath)
    }
}
