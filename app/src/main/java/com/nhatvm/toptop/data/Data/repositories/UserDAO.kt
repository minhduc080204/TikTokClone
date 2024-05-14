package com.nhatvm.toptop.data.Data.repositories

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface UserDAO {
    @Insert
    fun insertUser(user: User?)
}
