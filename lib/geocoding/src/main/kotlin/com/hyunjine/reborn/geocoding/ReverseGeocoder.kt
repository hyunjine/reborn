package com.hyunjine.reborn.geocoding

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.MultiPolygon
import org.locationtech.jts.geom.Polygon
import org.locationtech.jts.index.strtree.STRtree

/**
 * 한국 행정구역 경계 데이터 기반 역지오코딩.
 * 위경도를 입력하면 "시도 시군구" 형태의 주소를 반환합니다.
 */
class ReverseGeocoder {

    private val spatialIndex: STRtree
    private val geometryFactory = GeometryFactory()

    init {
        val json = Json { ignoreUnknownKeys = true }
        val text = this::class.java.classLoader
            .getResourceAsStream("skorea-municipalities-2018-geo.json")
            ?.bufferedReader()?.use { it.readText() }
            ?: error("skorea-municipalities-2018-geo.json not found in resources")

        val collection = json.decodeFromString<GeoFeatureCollection>(text)

        spatialIndex = STRtree()
        for (feature in collection.features) {
            val jtsGeom = parseGeometry(feature.geometry)
            val province = PROVINCE_MAP[feature.properties.code.take(2)] ?: ""
            val entry = IndexedEntry(jtsGeom, "$province ${feature.properties.name}")
            spatialIndex.insert(jtsGeom.envelopeInternal, entry)
        }
        spatialIndex.build()
    }

    /**
     * 위경도로 주소를 조회합니다.
     *
     * @param latitude 위도
     * @param longitude 경도
     * @return "시도 시군구" 형태의 주소, 한국 영역 밖이면 null
     */
    fun getAddress(latitude: Double, longitude: Double): String? {
        val point = geometryFactory.createPoint(Coordinate(longitude, latitude))
        val candidates = spatialIndex.query(point.envelopeInternal)
        for (candidate in candidates) {
            val entry = candidate as IndexedEntry
            if (entry.geometry.contains(point)) {
                return entry.address
            }
        }
        return null
    }

    private fun parseGeometry(geometry: GeoGeometry): org.locationtech.jts.geom.Geometry {
        return when (geometry.type) {
            "MultiPolygon" -> parseMultiPolygon(geometry.coordinates)
            "Polygon" -> parsePolygon(geometry.coordinates)
            else -> error("Unsupported geometry type: ${geometry.type}")
        }
    }

    private fun parseMultiPolygon(coordinates: JsonElement): MultiPolygon {
        val polygons = coordinates.jsonArray.map { parsePolygon(it) }
        return geometryFactory.createMultiPolygon(polygons.toTypedArray())
    }

    private fun parsePolygon(coordinates: JsonElement): Polygon {
        val rings = coordinates.jsonArray
        val shell = geometryFactory.createLinearRing(parseRing(rings[0].jsonArray))
        val holes = if (rings.size > 1) {
            (1 until rings.size).map {
                geometryFactory.createLinearRing(parseRing(rings[it].jsonArray))
            }.toTypedArray()
        } else {
            emptyArray()
        }
        return geometryFactory.createPolygon(shell, holes)
    }

    private fun parseRing(ring: JsonArray): Array<Coordinate> {
        return ring.map { point ->
            val arr = point.jsonArray
            Coordinate(arr[0].jsonPrimitive.double, arr[1].jsonPrimitive.double)
        }.toTypedArray()
    }

    private data class IndexedEntry(
        val geometry: org.locationtech.jts.geom.Geometry,
        val address: String
    )

    companion object {
        private val PROVINCE_MAP = mapOf(
            "11" to "서울특별시",
            "21" to "부산광역시",
            "22" to "대구광역시",
            "23" to "인천광역시",
            "24" to "광주광역시",
            "25" to "대전광역시",
            "26" to "울산광역시",
            "29" to "세종특별자치시",
            "31" to "경기도",
            "32" to "강원특별자치도",
            "33" to "충청북도",
            "34" to "충청남도",
            "35" to "전북특별자치도",
            "36" to "전라남도",
            "37" to "경상북도",
            "38" to "경상남도",
            "39" to "제주특별자치도",
        )
    }
}

@Serializable
internal data class GeoFeatureCollection(
    val features: List<GeoFeature>
)

@Serializable
internal data class GeoFeature(
    val properties: GeoProperties,
    val geometry: GeoGeometry
)

@Serializable
internal data class GeoProperties(
    val name: String,
    val code: String
)

@Serializable
internal data class GeoGeometry(
    val type: String,
    val coordinates: JsonElement
)
