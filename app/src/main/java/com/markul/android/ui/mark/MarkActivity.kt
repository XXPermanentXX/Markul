package com.markul.android.ui.mark

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.markul.android.MainActivity
import com.markul.android.MarkulApplication
import com.markul.android.R
import com.markul.android.logic.room.Album
import com.markul.android.logic.room.Photo
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.filter.Filter
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlinx.android.synthetic.main.activity_mark.*
import kotlin.concurrent.thread


class MarkActivity : AppCompatActivity(), MarkAdapter.OnItemClickListener {

    private val REQUEST_CODE_CHOOSE = 300 //照片选择回调
    private var picStringList=ArrayList<String>()
    //最大能上传的照片
    private val maxNum = 6

    private var markAdapter = MarkAdapter(picStringList, maxNum)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mark)
        val viewModel: MarkViewModel by viewModels()
        viewModel.loadAllAlbums(MarkulApplication.userId)
        initRecyclerview()
//        backBtn.setOnClickListener {
//            val intent=Intent(this, MainActivity::class.java)
//            startActivity(intent)
//      }

        uploadBtn.setOnClickListener {
            if(picStringList.isNotEmpty()){
            for(i in picStringList)
            {
                val photo=Photo("$i",MarkulApplication.albumMaxId)
                viewModel.addPhoto(photo)
            }
            val album=Album(MarkulApplication.userId,"")
            viewModel.addAlbum(album)
            picStringList=ArrayList<String>()
            val intent=Intent(this, MainActivity::class.java)
            startActivity(intent)}
            else{
                Toast.makeText(this,"请选择要上传的图片",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun initRecyclerview(){
        picRecycler.layoutManager = GridLayoutManager(this, 3)
        picRecycler.adapter = markAdapter
        markAdapter.setOnMyClickListener(this)
    }

    private fun selectPhoto(num: Int) {
        Matisse.from(this) //  .choose(MimeType.ofAll(),false)               //false表示不能同时选照片和视频
            .choose(MimeType.ofImage()) //选择类,日后单独配置
            //拍照需要的两个（写完这个就会有照相那个图标了）
            .capture(true)
            .captureStrategy(
                CaptureStrategy(
                    true,
                    "com.markul.android.ui.mark.fileprovider",
                    "test"
                )
            ) //最后文件存储地址：Pictures/test
            .countable(true) //选择时是否计数
            .maxSelectable(num) //最大可选择数
            .addFilter(GifSizeFilter(320, 320, 5 * Filter.K * Filter.K)) //过滤器   5M大小
     //          .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size)) //每个图片方格的大小
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT) //选择方向
            .thumbnailScale(0.85f) //刚进入图片选择页面后图片的清晰度
            .imageEngine(GlideEngine())//图片引擎
            .theme(R.style.Matisse_Dracula)//主题这里使用默认的
            .originalEnable(true) //原图按钮
            .forResult(REQUEST_CODE_CHOOSE) //请求码
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            //获取到的地址只是相对地址，但可以给图片加载，若要上传图片，需获取真实本地地址
            picStringList.addAll(Matisse.obtainPathResult(data)) //真实地址
            for(i in 0 until  picStringList.size){
                Log.i("ceshi", "onActivityResult:获取到的地址为: " + picStringList[i])
            }
            markAdapter.notifyDataSetChanged()
        }
    }

    override fun onItemAddClick(position: Int) {
        selectPhoto(maxNum - picStringList.size)
    }

    override fun onItemDelClick(position: Int) {
        picStringList.removeAt(position)
        markAdapter.notifyDataSetChanged()
    }

    //TODO
    override fun onItemPicClick(position: Int) {
        Toast.makeText(this, "点击图片", Toast.LENGTH_SHORT).show()
    }
}