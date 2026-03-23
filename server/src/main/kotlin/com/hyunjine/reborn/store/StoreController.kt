package com.hyunjine.reborn.store

import com.hyunjine.reborn.data.store.model.store_detail.StoreDetailModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/stores")
class StoreController(
    private val storeRepository: StoreRepository
) {

    @GetMapping("/{id}")
    fun getStoreDetail(@PathVariable id: Long): ResponseEntity<StoreDetailModel> {
        val detail = storeRepository.findStoreDetailById(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(detail)
    }
}
