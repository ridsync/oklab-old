package app.com.thetechnocafe.kotlinweather.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import app.com.thetechnocafe.kotlinweather.models.TimeLineInfo
import app.com.thetechnocafe.kotlinweather.models.VisitorInfo


/**
 * Created by arent on 2017. 7. 24..
 */
@Dao
interface TimeLineInfoDao {
    @Query("SELECT * FROM TimelineInfo")
    fun getAll(): List<TimeLineInfo>

//    @Query("SELECT * FROM TimeLineInfo WHERE user_id IN (:user_ids)")
//    fun loadAllByIds(user_ids: IntArray): List<TimeLineInfo>
//
//    @Query("SELECT * FROM TimeLineInfo WHERE user_login_id LIKE :first AND " + "user_photo LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): TimeLineInfo
//
    @Insert
    fun insertAll(timelineInfo:TimeLineInfo)
//
//    @Delete
//    fun delete(TimeLineInfo: TimeLineInfo)
}

@Dao
interface VisitorInfoDao {
    @Query("SELECT * FROM VisitorInfo")
    fun getAll(): List<VisitorInfo>

//    @Query("SELECT * FROM VisitorInfo WHERE user_id IN (:user_ids)")
//    fun loadAllByIds(user_ids: IntArray): List<VisitorInfo>
//
//    @Query("SELECT * FROM VisitorInfo WHERE user_login_id LIKE :first AND " + "user_login_id LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): VisitorInfo
//
//    @Insert
//    fun insertAll(list: ArrayList<VisitorInfo>)
//
//    @Delete
//    fun delete(visitorInfo: VisitorInfo)
}