package com.nhatvm.toptop.data.auth.repositories

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun doSignUp(user: User)

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<User>>

    @Query("SELECT * FROM user_table WHERE username = :username AND password = :password LIMIT 1")
    fun doSignIn(username: String, password: String): LiveData<User?>
}