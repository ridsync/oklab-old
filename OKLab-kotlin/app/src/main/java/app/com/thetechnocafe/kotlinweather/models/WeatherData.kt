package app.com.thetechnocafe.kotlinweather.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
* Created by gurleensethi on 16/06/17.
*/
open class BaseInfo : Serializable {

    @Expose
    val idx: Int = 0
    open var seq: Long = 0
    @SerializedName("age") var user_age:Int = 0
}

data class VisitorInfo(var user_id:Long,
                       var user_login_id:String,
                       var user_photo:String,
                       var user_gender:Int,
                       var user_home_addr1:String,
                       var reg_date:Long): BaseInfo() {


    override fun toString(): String {
        return super.toString()
    }
}

data class TimeLineInfo(var user_id:Long,
                       var user_login_id:String,
                       var user_photo:String,
                       var news_type_id:Int,
                       var is_zzim:Int,
                       var user_gender:Int,
                       var user_home_addr1:String,
                       var reg_date:Long,
                       var id:Int, var desc:String): BaseInfo() {

    override fun toString(): String {
        return super.toString()
    }
}


data class WeatherData(
        @SerializedName("query") val query: Query
) : Serializable

data class Query(
        @SerializedName("count") val count: Int,
        @SerializedName("created") val created: String,
        @SerializedName("lang") val lang: String,
        @SerializedName("results") val results: Results
) : Serializable {

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun toString(): String {
        return super.toString()
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

}


data class Results(
        val channel: Channel
) : Serializable

data class Channel(
        val title: String,
        val link: String,
        val description: String,
        val language: String,
        val lastBuildDate: String,
        val ttl: String,
        val location: Location,
        val units: Units,
        val wind: Wind,
        val atmosphere: Atmosphere,
        val astronomy: Astronomy,
        val image: Image,
        val item: Item
) : Serializable

data class Location(
        val city: String,
        val country: String,
        val region: String
) : Serializable

data class Units(
        val distance: String,
        val pressure: String,
        val speed: String,
        val temperature: String
) : Serializable

data class Wind(
        val chill: String,
        val direction: String,
        val speed: String
) : Serializable

data class Atmosphere(
        val humidity: String,
        val pressure: String,
        val rising: String,
        val visibility: String
) : Serializable

data class Astronomy(
        val sunrise: String,
        val sunset: String
) : Serializable

data class Image(
        val title: String,
        val width: String,
        val height: String,
        val link: String,
        val url: String
) : Serializable

data class Item(
        val title: String,
        val lat: String,
        val long: String,
        val link: String,
        val pubDate: String,
        val condition: Condition,
        val description: String,
        val forecast: Array<Forecast>,
        val guid: Guid
) : Serializable

data class Condition(
        val code: String,
        val date: String,
        val temp: String,
        val text: String
) : Serializable

data class Forecast(
        val code: String,
        val date: String,
        val day: String,
        val high: String,
        val low: String,
        val text: String
) : Serializable

data class Guid(
        val isPermaLink: String,
        val content: String
) : Serializable