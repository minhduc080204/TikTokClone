package com.nhatvm.toptop.data.Data.repositories

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDAO(): UserDAO?

    companion object {
        private const val DATABASE_NAME = "user.db"
        private var instance: UserDatabase? = null
        @Synchronized
        fun getInstance(context: Context): UserDatabase? {
            if (instance == null) {
                instance = databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    DATABASE_NAME
                ).allowMainThreadQueries().build()
            }
            return instance
        }
    }
}
