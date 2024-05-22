package com.nhatvm.toptop.data.auth.repositories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
class UserViewModel(application: Application): AndroidViewModel(application) {
    val readAllData: LiveData<List<User>>
    private val repository: UserRepository

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        readAllData = repository.readAllData
    }

    fun doSignUp(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.doSignUp(user)
        }
    }

    fun doSignIn(username: String, password: String): LiveData<User?> {
        return repository.doSignIn(username, password)
    }
}