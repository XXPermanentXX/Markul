package com.markul.android.ui.mark

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import androidx.room.Database
import com.markul.android.MarkulApplication
import com.markul.android.MarkulApplication.Companion.context
import com.markul.android.logic.Repository
import com.markul.android.logic.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MarkViewModel: ViewModel() {

    private val _id = MutableLiveData<Int>()

    var picStringList=ArrayList<String>()

    fun addAlbum(album: Album) {
        viewModelScope.launch(Dispatchers.IO) {
            Repository.addAlbum(album = album)
        }
    }

    fun addPhoto(photo: Photo) {
        viewModelScope.launch(Dispatchers.IO) {
            Repository.addPhoto(photo = photo)
        }
    }

    fun loadAllAlbums(userId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val MaxId=Repository.loadAllAlbums(userId=userId).albumList.size+1
            if(MarkulApplication.albumMaxId<MaxId)
            MarkulApplication.albumMaxId=MaxId
        }
    }
//    val allAlbums: LiveData<OwnerWithAlbums> = Transformations.switchMap(_id) {
//        Repository.loadAllAlbums(it)
//    }
//
//    fun loadAllAlbums(useId: Int) {
//        viewModelScope.launch {
//            _id.value = useId
//        }
//    }
}
