package com.nhatvm.toptop.data.Data.repositories

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
class User(var userName: String, var password: String) {
    @PrimaryKey
    var id = 0
}
