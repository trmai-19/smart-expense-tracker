package com.smartexpense.api.presentation.controller

import com.smartexpense.api.application.port.`in`.FileUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/files")
class FileController(
    private val fileUseCase: FileUseCase
) {

    @PostMapping("/upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile): ResponseEntity<Map<String, String>> {
        val fileUrl = fileUseCase.uploadFile(file)
        return ResponseEntity.ok(mapOf("url" to fileUrl))
    }

    @DeleteMapping("/delete")
    fun deleteFile(@RequestParam("url") url: String): ResponseEntity<Void> {
        fileUseCase.deleteFile(url)
        return ResponseEntity.noContent().build()
    }
}
