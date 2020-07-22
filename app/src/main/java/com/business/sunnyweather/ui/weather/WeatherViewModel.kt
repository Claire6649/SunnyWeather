package com.business.sunnyweather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.business.sunnyweather.logic.Repository
import com.business.sunnyweather.logic.model.Location

/**
 * Created by claire on 2020/7/22.
 */
class WeatherViewModel : ViewModel() {
    private val locationLiveData = MutableLiveData<Location>()

    /**
     * 这三个变量都是和界面相关的数据，放到ViewModel中可以保证它们在手机屏幕发生旋转的时候不会丢失
     */
    var locationLng = ""
    var locationLat = ""
    var placeName = ""

    /**
     * 调用Repository的refreshWeather返回的liveData可供Activity观察
     */
    val weatherLiveData = Transformations.switchMap(locationLiveData) { location ->
        Repository.refreshWeather(location.lng, location.lat)
    }

    /**
     * 首先这个将传入的经纬度参数封装成一个Location对象后复制给locationLiveData对象，然后使用Transformations转换，来观察这个对象
     */
    fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = Location(lng, lat)
    }
}