package com.markul.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.markul.android.logic.room.AppDatabase

class MarkulApplication : Application() {
    
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context:Context
        var userId:Int=1;
        var albumMaxId:Int=1
    }


    override fun onCreate() {
        super.onCreate()
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        context=applicationContext
        SDKInitializer.initialize(this)
        //定义全局context

        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL)
    }
}