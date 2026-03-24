package com.hyunjine.reborn.geocoding

import com.hyunjine.reborn.data.Location
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 역지오코딩 REST API 컨트롤러.
 *
 * @param reverseGeocoder 역지오코딩 서비스
 */
@RestController
@RequestMapping("/api/geocoding")
class GeocodingController(
    private val reverseGeocoder: ReverseGeocoder
) {

    /**
     * 위경도로 주소를 조회합니다.
     *
     * @param location 위경도 정보
     * @return address가 채워진 Location
     */
    @GetMapping("/reverse")
    suspend fun reverse(@ModelAttribute location: Location): Location {
        val address = reverseGeocoder.getAddress(location.latitude, location.longitude) ?: ""
        return location.copy(address = address)
    }
}
