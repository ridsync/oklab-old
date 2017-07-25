package app.com.thetechnocafe.kotlinweather.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import app.com.thetechnocafe.kotlinweather.db.dao.TimeLineInfoDao
import app.com.thetechnocafe.kotlinweather.db.dao.VisitorInfoDao
import app.com.thetechnocafe.kotlinweather.models.TimeLineInfo
import app.com.thetechnocafe.kotlinweather.models.VisitorInfo

/**
 * Created by arent on 2017. 7. 24..
 */
@Database(entities = arrayOf(VisitorInfo::class, TimeLineInfo::class), version = 1)
abstract class AppDatabase: RoomDatabase() {

    companion object {
        val DATABASE_NAME = "basic-room-db"
    }

    abstract fun visitorDao(): VisitorInfoDao
    abstract fun timeLineDao(): TimeLineInfoDao
}