package com.business.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by claire on 2020/7/22.
 */
class DailyResponse(val status: String, val result: Result) {

    class Result(val daily: Daily)

    class Daily(
        val temperature: List<Temperature>,
        val skycon: List<Skycon>,
        @SerializedName("life_index")
        val lifeIndex: LifeIndex
    )

    class Temperature(val max: Float, val min: Float)

    class Skycon(val value: String, val date: Date)

    class LifeIndex(
        val coldRisk: List<LifeDescription>,
        val carWashing: List<LifeDescription>,
        val ultraviolet: List<LifeDescription>,
        val dressing: List<LifeDescription>
    )

    class LifeDescription(val desc: String)
}