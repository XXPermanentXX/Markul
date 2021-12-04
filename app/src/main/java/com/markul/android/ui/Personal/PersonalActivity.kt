package com.markul.android.ui.Personal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.markul.android.MarkulApplication
import com.markul.android.R
import com.markul.android.logic.room.Album
import com.markul.android.logic.room.AlbumWithPhotos
import com.markul.android.ui.mark.MarkViewModel
import kotlinx.android.synthetic.main.activity_mark.*
import kotlinx.android.synthetic.main.activity_mark.picRecycler
import kotlinx.android.synthetic.main.activity_personal.*
import kotlin.concurrent.thread

class PersonalActivity : AppCompatActivity() {

    private lateinit var ablumAdapter: AlbumAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal)
        val viewModel: PersonalViewModel by viewModels()
        viewModel.loadAllPhotos(MarkulApplication.userId)
        Thread.sleep(1000)
        for (i in viewModel.albumList) {
            Log.d("1234567", "1234567")
            for (j in i.photoList) {
                viewModel.photosList.add(j.photoUrl)
                Log.d("123456", "123456")
            }
        }
        ablumAdapter = AlbumAdapter(viewModel.photosList)
        ablumAdapter.notifyDataSetChanged()
//        viewModel.UrlLiveData.observe(this, Observer { result->
//            val Urls=result.getOrNull()
//            if(Urls!=null){
//                viewModel.UrlList.addAll(Urls.photoList)
//            }
//        })

//        for(album in viewModel.loadAllPhotos(MarkulApplication.Id)){
//
//        }
        initRecyclerview()


    }

    private fun initRecyclerview() {
        picRecycler.layoutManager = GridLayoutManager(this, 3)
        picRecycler.adapter = ablumAdapter
    }
}