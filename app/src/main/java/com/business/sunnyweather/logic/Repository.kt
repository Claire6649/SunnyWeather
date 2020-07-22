package com.business.sunnyweather.logic

import androidx.lifecycle.liveData
import com.business.sunnyweather.logic.model.Weather
import com.business.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * Created by claire on 2020/7/21.
 */
object Repository {
    fun searchPlaces(query: String) = fire(Dispatchers.IO){
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }



//        liveData(Dispatchers.IO) {
//        val result = try {
//            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
//            if (placeResponse.status == "ok") {
//                val places = placeResponse.places
//                Result.success(places)
//            } else {
//                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
//            }
//        } catch (e: Exception) {
//            Result.failure<List<Place>>(e)
//        }
//        emit(result)
//        //emit(result)类似liveData的setValue()方法来通知数据变化，只不过这里我们无法取得返回的LiveData对象
//    }

    /**
     * 由于使用了协程来简化网络回调的写法，导致SunnyWeatherNetwork中封装的每个网络请求接口都可能会抛出异常，
     * 于是我们必须在仓库层中为每个网络请求都进行try catch处理。
     * 我们定义fire封装，统一处理异常
     */
    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO){
        coroutineScope {
            val deferredRealtime = async { SunnyWeatherNetwork.getRealtimeWeather(lng, lat) }
            val deferredDaily = async { SunnyWeatherNetwork.getDailyWeather(lng, lat) }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            //同时得到它们的响应结果才能进一步执行程序
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather =
                    Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" +
                                "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

//        liveData(Dispatchers.IO) {
//        val result = try {
//            coroutineScope {
//                //并发执行,分别在两个async函数中发起网络请求，然后再分别调用它们的await()方法
//                //async必须在协程作用域内才能调用
//                val deferredRealtime = async { SunnyWeatherNetwork.getRealtimeWeather(lng, lat) }
//                val deferredDaily = async { SunnyWeatherNetwork.getDailyWeather(lng, lat) }
//                val realtimeResponse = deferredRealtime.await()
//                val dailyResponse = deferredDaily.await()
//                //同时得到它们的响应结果才能进一步执行程序
//                if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
//                    val weather =
//                        Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
//                    Result.success(weather)
//                } else {
//                    Result.failure(
//                        RuntimeException(
//                            "realtime response status is ${realtimeResponse.status}" +
//                                    "daily response status is ${dailyResponse.status}"
//                        )
//                    )
//                }
//            }
//        } catch (e: Exception) {
//            Result.failure<Weather>(e)
//        }
//        emit(result)
//    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }

}