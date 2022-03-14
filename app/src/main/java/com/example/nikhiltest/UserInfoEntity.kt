package com.example.nikhiltest

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_info")
data class UserInfoEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "userName") var userName: String,
    @ColumnInfo(name = "userMobile") var userMobile: String,
    @ColumnInfo(name = "userBook") var userBook: String,
)
