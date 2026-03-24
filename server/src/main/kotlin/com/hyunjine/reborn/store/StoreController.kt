package com.hyunjine.reborn.store

import com.hyunjine.reborn.data.ApiResponse
import com.hyunjine.reborn.data.Location
import com.hyunjine.reborn.data.store.StoreRemoteDataSource
import com.hyunjine.reborn.data.store.model.StoreDetailModel
import com.hyunjine.reborn.data.store.model.StoreModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
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
): StoreRemoteDataSource {

    /**
     * 모든 업체 목록을 조회합니다.
     *
     * @param location 클라이언트 위치 (거리 계산용)
     * @return 업체 목록을 담은 공통 응답 객체
     */
    @GetMapping
    override suspend fun getStores(
        @ModelAttribute location: Location
    ): ApiResponse<ImmutableList<StoreModel>> {
        val stores = storeRepository.findAllStores(location).toImmutableList()
        return ApiResponse.Success(stores)
    }

    /**
     * 업체 상세 정보를 조회합니다.
     *
     * @param id 조회할 업체 ID
     * @return 업체 상세 정보를 담은 공통 응답 객체
     */
    @GetMapping("/{id}")
    override suspend fun getStoreDetail(@PathVariable id: Long): ApiResponse<StoreDetailModel> {
        val detail = storeRepository.findStoreDetailById(id)
            ?: return ApiResponse.Error("업체를 찾을 수 없습니다. id=$id")
        return ApiResponse.Success(detail)
    }
}
