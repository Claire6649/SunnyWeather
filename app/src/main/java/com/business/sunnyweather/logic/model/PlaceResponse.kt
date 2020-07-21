package com.business.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName

/**
 * Created by claire on 2020/7/21.
 */
data class PlaceResponse(val status: String, val places: List<Place>)

data class Place(
    val name: String,
    val location: Location,
    //Json中一些字段的命名可能与Kotlin的命名规范不太一致，因此这里使用了SerializedName注解方式让json字段与kotlin字段建立映射
    @SerializedName("formatted_address") val address: String
)

data class Location(val lng: String, val lat: String)