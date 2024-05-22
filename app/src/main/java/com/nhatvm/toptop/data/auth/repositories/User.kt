package com.nhatvm.toptop.data.auth.repositories

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    var Name: String,
    var Phone: String,
    var Username: String,
)