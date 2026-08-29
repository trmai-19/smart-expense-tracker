package com.smartexpense.api.application.port.`in`

import org.springframework.web.multipart.MultipartFile

interface FileUseCase {
    fun uploadFile(file: MultipartFile): String
    fun deleteFile(fileUrl: String)
}
