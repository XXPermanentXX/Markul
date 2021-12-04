package com.markul.android.logic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.markul.android.MarkulApplication
import com.markul.android.MarkulApplication.Companion.context
import com.markul.android.logic.room.*
import com.markul.android.ui.mark.MarkViewModel
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import kotlin.coroutines.CoroutineContext


object Repository
 {
     val appDao=AppDatabase.getDatabase(context).albumDao()

    fun addUser(user: User){
        appDao.addUser(user)
    }

    fun addAlbum(album: Album){
        appDao.addAlbum(album)
    }
     fun addPhoto(photo: Photo){
         appDao.addPhoto(photo)
     }

     fun loadAllAlbums(userId:Int): OwnerWithAlbums {
         return appDao.getOwnerWithAlbums(userId)
     }

     fun loadAllPhotos(albumId:Int):List<AlbumWithPhotos>{
         return appDao.getAlbumWithPhotos(albumId)
     }


//    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
//        liveData<Result<T>>(context) {
//            val result = try {
//                block()
//            } catch (e: Exception) {
//                Result.failure<T>(e)
//            }
//            emit(result)
//        }
}

