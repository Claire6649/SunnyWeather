package com.business.sunnyweather.logic.model

/**
 * Created by claire on 2020/7/22.
 */
data class Weather(val realtime: RealtimeResponse.Realtime, val daily: DailyResponse.Daily)