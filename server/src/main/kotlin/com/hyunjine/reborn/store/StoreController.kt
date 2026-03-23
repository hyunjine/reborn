package com.hyunjine.reborn.store

import com.hyunjine.reborn.data.store.model.store_detail.StoreDetailModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 업체 관련 REST API 컨트롤러.
 *
 * @param storeRepository 업체 정보 조회 Repository
 */
@RestController
@RequestMapping("/api/stores")
class StoreController(
    private val storeRepository: StoreRepository
) {

    /**
     * 업체 상세 정보를 조회합니다.
     *
     * @param id 조회할 업체 ID
     * @return 업체 상세 정보 (존재하지 않으면 404)
     */
    @GetMapping("/{id}")
    fun getStoreDetail(@PathVariable id: Long): ResponseEntity<StoreDetailModel> {
        val detail = storeRepository.findStoreDetailById(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(detail)
    }
}
