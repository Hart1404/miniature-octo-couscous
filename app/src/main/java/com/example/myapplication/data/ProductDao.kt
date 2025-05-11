package com.example.myapplication.data

import androidx.room.*

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    suspend fun getAll(): List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getById(id: Int): Product?

    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%'")
    suspend fun searchByName(query: String): List<Product>

    @Query("SELECT * FROM products WHERE barcode = :barcode")
    suspend fun getByBarcode(barcode: String): Product?

    @Query("""
        SELECT * FROM products 
        WHERE is_protein_tag = :isProtein 
        AND is_fast_tag = :isFast 
        AND is_vegan_tag = :isVegan
        AND is_vegetarian_tag = :isVegetarian
        AND is_sea_tag = :isSea
        AND is_light_tag = :isLight
        AND is_breakfast_tag = :isBreakfast
        AND is_lunch_tag = :isLunch
        AND is_dinner_tag = :isDinner
        AND is_snack_tag = :isSnack
        AND is_gluten_free = :isGlutenFree
        AND is_lactose_free = :isLactoseFree
        AND is_nuts_free = :isNutsFree
        AND is_eggs_free = :isEggsFree
        AND is_fish_free = :isFishFree
        AND is_peanut_tag = :isPeanut
        AND is_fruits_tag = :isFruits
        AND is_chocolate_tag = :isChocolate
        AND is_berries_tag = :isBerries
        AND is_vegetables_tag = :isVegetables
    """)
    suspend fun getFilteredProducts(
        isProtein: Boolean = false,
        isFast: Boolean = false,
        isVegan: Boolean = false,
        isVegetarian: Boolean = false,
        isSea: Boolean = false,
        isLight: Boolean = false,
        isBreakfast: Boolean = false,
        isLunch: Boolean = false,
        isDinner: Boolean = false,
        isSnack: Boolean = false,
        isGlutenFree: Boolean = false,
        isLactoseFree: Boolean = false,
        isNutsFree: Boolean = false,
        isEggsFree: Boolean = false,
        isFishFree: Boolean = false,
        isPeanut: Boolean = false,
        isFruits: Boolean = false,
        isChocolate: Boolean = false,
        isBerries: Boolean = false,
        isVegetables: Boolean = false
    ): List<Product>
} 