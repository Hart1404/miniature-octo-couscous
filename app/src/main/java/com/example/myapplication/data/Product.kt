package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.io.Serializable

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "calories")
    val calories: Int,

    @ColumnInfo(name = "protein")
    val protein: Float,

    @ColumnInfo(name = "fat")
    val fat: Float,

    @ColumnInfo(name = "carbs")
    val carbs: Float,

    @ColumnInfo(name = "barcode")
    val barcode: String? = null,

    @ColumnInfo(name = "is_protein_tag")
    val isProteinTag: Boolean = false,

    @ColumnInfo(name = "is_fast_tag")
    val isFastTag: Boolean = false,

    @ColumnInfo(name = "is_vegan_tag")
    val isVeganTag: Boolean = false,

    @ColumnInfo(name = "is_vegetarian_tag")
    val isVegetarianTag: Boolean = false,

    @ColumnInfo(name = "is_sea_tag")
    val isSeaTag: Boolean = false,

    @ColumnInfo(name = "is_light_tag")
    val isLightTag: Boolean = false,

    @ColumnInfo(name = "is_breakfast_tag")
    val isBreakfastTag: Boolean = false,

    @ColumnInfo(name = "is_lunch_tag")
    val isLunchTag: Boolean = false,

    @ColumnInfo(name = "is_dinner_tag")
    val isDinnerTag: Boolean = false,

    @ColumnInfo(name = "is_snack_tag")
    val isSnackTag: Boolean = false,

    @ColumnInfo(name = "is_gluten_free")
    val isGlutenFree: Boolean = false,

    @ColumnInfo(name = "is_lactose_free")
    val isLactoseFree: Boolean = false,

    @ColumnInfo(name = "is_nuts_free")
    val isNutsFree: Boolean = false,

    @ColumnInfo(name = "is_eggs_free")
    val isEggsFree: Boolean = false,

    @ColumnInfo(name = "is_fish_free")
    val isFishFree: Boolean = false,

    @ColumnInfo(name = "is_peanut_tag")
    val isPeanutTag: Boolean = false,

    @ColumnInfo(name = "is_fruits_tag")
    val isFruitsTag: Boolean = false,

    @ColumnInfo(name = "is_chocolate_tag")
    val isChocolateTag: Boolean = false,

    @ColumnInfo(name = "is_berries_tag")
    val isBerriesTag: Boolean = false,

    @ColumnInfo(name = "is_vegetables_tag")
    val isVegetablesTag: Boolean = false,

    @ColumnInfo(name = "rating")
    val rating: Float = 3.0f
) : Serializable 