package app.com.thetechnocafe.kotlinweather.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
* Created by gurleensethi on 16/06/17.
*/
open class BaseInfo : Serializable {

    @Expose
    @ColumnInfo(name = "idx")var idx: Int = 0
    @ColumnInfo(name = "seq")var seq: Long = 0
    @SerializedName("age") @ColumnInfo(name = "user_age") var user_age:Int = 0
}
@Entity(tableName = "VisitorInfo")
data class VisitorInfo(@ColumnInfo(name = "user_id") var user_id:Long = 0,
                       @ColumnInfo(name = "user_login_id") var user_login_id:String = "",
                       @ColumnInfo(name = "user_photo") var user_photo:String? = null,
                       @ColumnInfo(name = "user_gender") var user_gender:Int = 0,
                       @Ignore var user_home_addr1:String? = null,
                       @ColumnInfo(name = "reg_date") var reg_date:Long = 0): BaseInfo() {

    override fun toString(): String {
        return super.toString()
    }
}
@Entity(tableName = "TimelineInfo")
data class TimeLineInfo(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id:Int,
                        @ColumnInfo(name = "user_id") var user_id:Long = 0,
                        @ColumnInfo(name = "user_login_id") var user_login_id:String = "",
                        @ColumnInfo(name = "user_photo") var user_photo:String? = null,
                        @ColumnInfo(name = "news_type_id") var news_type_id:Int = 0,
                        @ColumnInfo(name = "user_gender") var user_gender:Int = 0,
                        @ColumnInfo(name = "reg_date") var reg_date:Long = 0,
                        @ColumnInfo(name = "desc") var desc:String? = null): BaseInfo() {

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