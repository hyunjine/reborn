package com.hyunjine.reborn.store

import com.hyunjine.reborn.data.ApiResponse
import com.hyunjine.reborn.data.Location
import com.hyunjine.reborn.data.store.StoreRemoteDataSource
import com.hyunjine.reborn.data.store.model.RegistStoreModel
import com.hyunjine.reborn.data.store.model.StoreDetailModel
import com.hyunjine.reborn.data.store.model.StoreModel
import com.hyunjine.reborn.store.dto.DayScheduleRequest
import com.hyunjine.reborn.store.dto.PriceItemRequest
import com.hyunjine.reborn.store.dto.RegistStoreRequest
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController

/**
 * 업체 관련 REST API 컨트롤러.
 *
 * @param storeRepository 업체 정보 조회 Repository
 */
@RestController
@RequestMapping("/api/stores")
class StoreController(
    private val storeRepository: StoreRepository,
    private val imageStorageService: ImageStorageService
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
     * 새로운 업체를 등록합니다.
     *
     * multipart/form-data 형식으로 업체 정보(JSON)와 사진 파일을 함께 전달받습니다.
     *
     * @param data 업체 등록 요청 JSON 데이터
     * @param photos 업체 사진 파일 목록
     * @return 생성된 업체 ID를 담은 공통 응답 객체
     */
    @PostMapping
    suspend fun registerStoreMultipart(
        @RequestPart("data") data: RegistStoreRequest,
        @RequestPart("photos") photos: List<FilePart>
    ): ApiResponse<Long> {
        val storeId = storeRepository.insertStore(data)
        photos.forEach { photo ->
            val imageUrl = imageStorageService.saveImage(storeId, photo)
            storeRepository.insertStoreImage(storeId, imageUrl)
        }
        return ApiResponse.Success(storeId)
    }

    /**
     * [StoreRemoteDataSource] 인터페이스 구현.
     *
     * 실제 HTTP 요청은 [registerStoreMultipart]가 처리하며,
     * 이 메서드는 인터페이스 계약을 충족하기 위한 내부 구현입니다.
     *
     * @param model 업체 등록 UI 모델
     * @param latitude 위도
     * @param longitude 경도
     * @return 생성된 업체 ID를 담은 공통 응답 객체
     */
    override suspend fun registerStore(
        model: RegistStoreModel,
        latitude: Double,
        longitude: Double
    ): ApiResponse<Long> {
        val request = RegistStoreRequest(
            name = model.name,
            phone = model.phone,
            address = model.address,
            description = model.description,
            latitude = latitude,
            longitude = longitude,
            daySchedules = model.daySchedules.map { schedule ->
                DayScheduleRequest(
                    dayOfWeek = schedule.dayOfWeek,
                    isEnabled = schedule.isEnabled,
                    startTime = schedule.startTime,
                    endTime = schedule.endTime
                )
            },
            priceItems = model.priceItems.map { item ->
                PriceItemRequest(
                    name = item.name.value,
                    price = item.price ?: 0
                )
            }
        )
        val storeId = storeRepository.insertStore(request)
        return ApiResponse.Success(storeId)
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
