package com.markul.android

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import com.baidu.location.*
import com.markul.android.ui.mark.MarkActivity
import com.permissionx.guolindev.PermissionX
import kotlinx.android.synthetic.main.activity_main.*
import com.markul.android.logic.room.Photo
import com.markul.android.logic.room.User
import com.markul.android.ui.Personal.PersonalActivity
import com.markul.android.ui.mark.MarkViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.ArrayList
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng


class MainActivity : AppCompatActivity() {
    val user= User("13664926021","123","123")
    // 地图View实例
    private var mLocClient: LocationClient? = null
    val myListener = MyLocationListenner()
    var mMapView: MapView? = null
    var mBaiduMap: BaiduMap? = null
    // 定位图层显示方式
    private val mSensorManager: SensorManager? = null
    private val lastX = 0.0
    private val mCurrentDirection = 0
    private val mCurrentLat = 0.0
    private val mCurrentLon = 0.0
    private val mCurrentAccracy = 0f
    // 是否首次定位
    var isFirstLoc = true
    // 用于设置个性化地图的样式文件
    private val CUSTOM_FILE_NAME_CX = "custom_map_config_CX.sty"
    //定位图层
    private val myLocationData: MyLocationData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewModel: MainViewModel by viewModels()
//        viewModel.addUser(user)
        //提醒用户授权
        checkPermission()
        //地图初始化
        mMapView = findViewById<View>(R.id.bmapView) as MapView
        mBaiduMap = mMapView!!.map
        val mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL
        val accuracyCircleFillColor = -0x55000078
        val accuracyCircleStrokeColor = 0x55ff0100
         //修改为自定义图层
        var mcurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.head_image)
        mBaiduMap!!.setMyLocationConfiguration(
            MyLocationConfiguration(
                mCurrentMode, true, mcurrentMarker,
                accuracyCircleFillColor,accuracyCircleStrokeColor
            )
        )
        //改变地图样式
        changeMapStyle()
        initLocation()
        //初始化定位
        markBtn.setOnClickListener {
            val intent=Intent(this, MarkActivity::class.java)
            startActivity(intent)
        }
        personalBtn.setOnClickListener {
            val intent=Intent(this, PersonalActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initLocation() {
        // 开启定位图层
        mBaiduMap!!.isMyLocationEnabled = true
        // 定位初始化
        mLocClient = LocationClient(this)
        mLocClient!!.registerLocationListener(myListener)
        val option = LocationClientOption()
        // 打开gps
        option.isOpenGps = true
        // 设置坐标类型
        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        option.setCoorType("bd09ll")
        option.setScanSpan(1000)
        mLocClient!!.locOption = option
        mLocClient!!.start()
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView!!.onPause()
    }

    override fun onDestroy() {
        // 退出时销毁定位
        mLocClient!!.stop()
        mBaiduMap!!.isMyLocationEnabled = false;
        mMapView!!.onDestroy();
        mMapView=null
        super.onDestroy()
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
    }

    private fun getCustomStyleFilePath(context: Context, customStyleFileName: String): String? {
        var outputStream: FileOutputStream? = null
        var inputStream: InputStream? = null
        var parentPath: String? = null
        try {
            inputStream = context.assets.open("customConfigdir/$customStyleFileName")
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            parentPath = context.filesDir.absolutePath
            val customStyleFile = File("$parentPath/$customStyleFileName")
            if (customStyleFile.exists()) {
                customStyleFile.delete()
            }
            customStyleFile.createNewFile()
            outputStream = FileOutputStream(customStyleFile)
            outputStream.write(buffer)
        } catch (e: IOException) {
            Log.e("CustomMapDemo", "Copy custom style file failed", e)
        } finally {
            try {
                inputStream?.close()
                outputStream?.close()
            } catch (e: IOException) {
                Log.e("CustomMapDemo", "Close stream failed", e)
                return null
            }
        }
        return "$parentPath/$customStyleFileName"
    }

    private fun checkPermission() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_MEDIA_LOCATION
//                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION
//                Manifest.permission.READ_CONTACTS

            )
            .onExplainRequestReason { scope, deniedList, beforeRequest ->
                scope.showRequestReasonDialog(deniedList, "即将重新申请的权限是必须依赖的权限,否则无法正常使用应用", "我已明白", "取消")
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "确定", "取消")
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    Toast.makeText(this, "所有权限都已经通过", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "您拒绝了如下权限：$deniedList", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun changeMapStyle(){
        // 构建地图状态
        val builder = MapStatus.Builder()
        // 更新地图
        mMapView!!.map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))
        // 获取.sty文件路径
        val customStyleFilePath: String? = getCustomStyleFilePath(this@MainActivity, CUSTOM_FILE_NAME_CX)
        // 设置个性化地图样式文件的路径和加载方式
        mMapView!!.setMapCustomStylePath(customStyleFilePath)
        // 动态设置个性化地图样式是否生效
        mMapView!!.setMapCustomStyleEnable(true)
    }

    inner class MyLocationListenner : BDLocationListener {
        override fun onReceiveLocation(location: BDLocation) {
            // MapView 销毁后不在处理新接收的位置

            if (location == null || mMapView == null) {
                return
            }
            val locData = MyLocationData.Builder()
                .accuracy(location.radius) // 设置定位数据的精度信息，单位：米
                .direction(location.direction) // 此处设置开发者获取到的方向信息，顺时针0-360
                .latitude(23.0540551376)
                .longitude(113.4130708748)
                .build()
            // 设置定位数据, 只有先允许定位图层后设置数据才会生效
            mBaiduMap!!.setMyLocationData(locData)
            if (isFirstLoc) {
                isFirstLoc = false
                val latLng = LatLng(23.0540551376, 113.4130708748)
                val builder = MapStatus.Builder()
                builder.target(latLng).zoom(15.0f)
                mBaiduMap!!.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))
            }
        }
    }
}