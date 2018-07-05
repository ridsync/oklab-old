package app.com.thetechnocafe.kotlinweather

import android.content.Context
import android.support.multidex.MultiDexApplication
import android.arch.persistence.room.Room
import app.com.thetechnocafe.kotlinweather.db.AppDatabase
import id.kotlin.sample.room.extensions.objectOf
import android.arch.persistence.room.Room as RoomDB
import com.facebook.stetho.Stetho



class OKKotlin : MultiDexApplication() {

    companion object {
        lateinit var db: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) { Stetho.initializeWithDefaults(this) }

        db = Room.databaseBuilder(applicationContext,
                objectOf<AppDatabase>() , AppDatabase.DATABASE_NAME).build()

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }
}