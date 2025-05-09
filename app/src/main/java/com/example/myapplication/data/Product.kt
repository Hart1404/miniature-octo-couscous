package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val Title: String,
    val Calories: Int,
    val Protein: Int,
    val Fat: Int,
    val Carbohydrates: Int,
    val Salt: Double,
    val Calcium: Int,
    val Magnesium: Int,
    val Potassium: Int,
    val Iron: Float,
    val Fiber: Float,
    val Omega_3: Float,
    val Vitamin_D: Int,
    val Vitamin_C: Float
) 