package com.nhatvm.toptop.data.auth.repositories

import androidx.lifecycle.LiveData
import javax.inject.Inject

class UserRepository(private val userDao: UserDao){
    val readAllData: LiveData<List<User>> = userDao.readAllData()

    suspend fun doSignUp(user: User){
        userDao.doSignUp(user)
    }

    fun doSignIn(username: String, password: String): LiveData<User?> {
        return userDao.doSignIn(username, password)
    }
}