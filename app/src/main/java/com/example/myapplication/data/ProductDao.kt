package com.example.myapplication.data

import androidx.room.*

@Dao
interface ProductDao {
    @Query("SELECT * FROM product")
    suspend fun getAll(): List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("SELECT * FROM product WHERE id = :id")
    suspend fun getById(id: Long): Product?

    @Query("SELECT * FROM product WHERE Title LIKE :query")
    suspend fun searchByTitle(query: String): List<Product>
} 