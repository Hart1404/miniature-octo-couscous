package com.example.myapplication.data

import androidx.room.*
import java.util.Date

@Dao
interface UserDataDao {
    @Query("SELECT * FROM user_data")
    suspend fun getAll(): List<UserData>

    @Query("SELECT * FROM user_data WHERE date = :date")
    suspend fun getByDate(date: Date): UserData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userData: UserData)

    @Update
    suspend fun update(userData: UserData)

    @Delete
    suspend fun delete(userData: UserData)

    @Query("SELECT * FROM user_data WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getByDateRange(startDate: Date, endDate: Date): List<UserData>
} 