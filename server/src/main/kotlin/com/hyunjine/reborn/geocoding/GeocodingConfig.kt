package com.hyunjine.reborn.geocoding

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GeocodingConfig {

    @Bean
    fun reverseGeocoder(): ReverseGeocoder = ReverseGeocoder()
}
