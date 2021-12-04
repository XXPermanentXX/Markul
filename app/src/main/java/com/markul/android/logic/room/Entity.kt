package com.markul.android.logic.room


import androidx.room.*

@Entity
data class User(
    var phone: String,
    var possword: String,
    var headImage: String,
//    val friendsIdList:ArrayList<Int>,
//    val badgeIdList:ArrayList<String>,
//    var privateAlbumList:ArrayList<Album>,
){
    @PrimaryKey(autoGenerate = true)
    var userId: Int=0
}

@Entity
data class Album(
    var albumOwnerId:Int=1,
    var albumName:String?,
//    var lat:Double,
//    var lng:Double
){
    @PrimaryKey(autoGenerate = true)
    var albumId: Int=0
}

@Entity
data class Photo(
    var photoUrl:String?,
    var photoOwnerId: Int=1
){
    @PrimaryKey(autoGenerate = true)
    var photoId: Int = 0
}

data class OwnerWithAlbums(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "albumOwnerId"
    )
    val albumList: List<Album>
)

data class AlbumWithPhotos(
    @Embedded val album:Album,
    @Relation(
        parentColumn = "albumId",
        entityColumn = "photoOwnerId"
    )
    val photoList: List<Photo>
)



