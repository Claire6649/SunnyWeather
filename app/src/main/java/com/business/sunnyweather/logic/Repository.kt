package com.business.sunnyweather.logic

import androidx.lifecycle.liveData
import com.business.sunnyweather.logic.model.Place
import com.business.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers

/**
 * Created by claire on 2020/7/21.
 */
object Repository {
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<Place>>(e)
        }
        emit(result)
        //emit(result)类似liveData的setValue()方法来通知数据变化，只不过这里我们无法取得返回的LiveData对象
    }
}