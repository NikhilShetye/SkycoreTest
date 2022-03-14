package com.example.nikhiltest

import androidx.room.*

@Dao
interface UserInfoDAO {
    @Query("SELECT * FROM user_info")
    fun getAll(): List<UserInfoEntity>

    @Insert
    fun insertAll(vararg userInfo: UserInfoEntity)

    @Delete
    fun delete(userInfo: UserInfoEntity)

    @Update
    fun updateUser(vararg userInfo: UserInfoEntity)
}