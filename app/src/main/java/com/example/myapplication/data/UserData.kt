package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.util.Date

@Entity(tableName = "user_data")
data class UserData(
    @PrimaryKey
    @ColumnInfo(name = "date")
    val date: Date,

    @ColumnInfo(name = "weight")
    val weight: Float = 0f,

    @ColumnInfo(name = "calories")
    val calories: Int = 0,

    @ColumnInfo(name = "protein")
    val protein: Float = 0f,

    @ColumnInfo(name = "fat")
    val fat: Float = 0f,

    @ColumnInfo(name = "carbs")
    val carbs: Float = 0f,

    @ColumnInfo(name = "eaten")
    val eaten: Int = 0,

    @ColumnInfo(name = "burned")
    val burned: Int = 0,

    @ColumnInfo(name = "salt")
    val salt: Float = 0f,

    @ColumnInfo(name = "calcium")
    val calcium: Int = 0,

    @ColumnInfo(name = "magnesium")
    val magnesium: Int = 0,

    @ColumnInfo(name = "potassium")
    val potassium: Int = 0,

    @ColumnInfo(name = "iron")
    val iron: Float = 0f,

    @ColumnInfo(name = "fiber")
    val fiber: Float = 0f,

    @ColumnInfo(name = "omega3")
    val omega3: Float = 0f,

    @ColumnInfo(name = "vitamin_d")
    val vitaminD: Int = 0,

    @ColumnInfo(name = "vitamin_c")
    val vitaminC: Int = 0
) 