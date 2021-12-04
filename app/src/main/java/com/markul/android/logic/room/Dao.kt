package com.markul.android.logic.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface AppDao {
        @Insert
        fun addUser(user: User)

        @Insert
        fun addAlbum(album: Album)

        @Insert
        fun addPhoto(photo: Photo)

        @Transaction
        @Query("SELECT*from User where userId=:userId")
        fun getOwnerWithAlbums(userId:Int):OwnerWithAlbums

//        @Transaction
//        @Query("SELECT*from Album where albumOwnerId=:userId")
//        fun getAlbumWithPhotos(userId:Int):LiveData<List<AlbumWithPhotos>>

        @Transaction
        @Query("SELECT*from Album where albumOwnerId=:userId")
        fun getAlbumWithPhotos(userId:Int):List<AlbumWithPhotos>

//    @Query("SELECT*from Album ORDER BY id DESC")
//    suspend fun loadAllAlbum(id:Int):LiveData<List<Album>>
//
////    @Query("select*from Album where albumId=:albumId")
////    fun loadAlbumFlow(albumId:Int): LiveData<Album>
//
//    @Delete
//    suspend fun delete(album: Album)

}
