package com.hyunjine.reborn.store

import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import java.util.UUID

/**
 * 업체 이미지 파일 저장 서비스.
 *
 * 업로드된 이미지를 로컬 파일 시스템에 저장하고,
 * 정적 리소스로 접근 가능한 URL 경로를 반환합니다.
 */
@Service
class ImageStorageService {

    private val uploadDir: Path = Path.of("uploads/stores")

    init {
        Files.createDirectories(uploadDir)
    }

    /**
     * 이미지 파일을 저장하고 접근 가능한 URL 경로를 반환합니다.
     *
     * @param storeId 업체 ID
     * @param file 업로드된 이미지 파일
     * @return 이미지 접근 URL 경로 (e.g. "/uploads/stores/1/uuid.jpg")
     */
    suspend fun saveImage(storeId: Long, file: FilePart): String {
        val storeDir = uploadDir.resolve(storeId.toString())
        Files.createDirectories(storeDir)

        val extension = file.filename().substringAfterLast('.', "jpg")
        val fileName = "${UUID.randomUUID()}.$extension"
        val filePath = storeDir.resolve(fileName)

        file.transferTo(filePath).awaitSingle()
        return "/uploads/stores/$storeId/$fileName"
    }
}
