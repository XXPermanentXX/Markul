package com.markul.android.ui.Personal

import android.util.Log
import androidx.lifecycle.*
import com.markul.android.MarkulApplication
import com.markul.android.MarkulApplication.Companion.context
import com.markul.android.logic.Repository
import com.markul.android.logic.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PersonalViewModel : ViewModel() {

    private val _id= MutableLiveData<Int>()

    var albumList=ArrayList<AlbumWithPhotos>()
    var photosList=ArrayList<String?>()

//    val allPhotos:LiveData<List<AlbumWithPhotos>> =Transformations.switchMap(_id){
//        Repository.loadAllPhotos(it)
//    }

    fun loadAllPhotos(albumId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            albumList.addAll(Repository.loadAllPhotos(albumId=albumId))
            Log.d("12345","12345")
        }
    }

//    fun loadAllPhotos(useId: Int) {
//        viewModelScope.launch {
//            _id.value=useId
//        }
//    }
}