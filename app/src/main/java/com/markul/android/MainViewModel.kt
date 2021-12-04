package com.markul.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markul.android.logic.Repository
import com.markul.android.logic.room.Album
import com.markul.android.logic.room.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel(){

    fun addUser(user:User) {
        viewModelScope.launch(Dispatchers.IO) {
            Repository.addUser(user=user)
        }
    }
}