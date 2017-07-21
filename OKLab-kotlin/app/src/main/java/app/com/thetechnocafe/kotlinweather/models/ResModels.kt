package app.com.thetechnocafe.kotlinweather.models

/**
* Created by andman on 2015-12-03.
*/
open class BaseModel {

    var retVal: Int = 0
    var retMsg: String? = null
    var retHeart: Int = 0

}

data class ResVisitorList(var list: ArrayList<VisitorInfo>?) : BaseModel()

data class ResTimeLineList(var list: ArrayList<TimeLineInfo>?) : BaseModel()