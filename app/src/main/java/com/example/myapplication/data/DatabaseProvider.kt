package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Product::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val instance = INSTANCE ?: synchronized(this) {
                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "food-diary-db"
                ).build()
                INSTANCE = inst
                inst
            }
            // Проверяем и заполняем тестовыми продуктами, если таблица пуста
            instance.populateIfEmpty(context)
            return instance
        }
    }

    fun populateIfEmpty(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = this@AppDatabase.productDao()
            if (dao.getAll().isEmpty()) {
                val testProducts = listOf(
                    Product(
                        Title = "Яблоко",
                        Calories = 52,
                        Protein = 0,
                        Fat = 0,
                        Carbohydrates = 14,
                        Salt = 0.0,
                        Calcium = 6,
                        Magnesium = 5,
                        Potassium = 107,
                        Iron = 0.1f,
                        Fiber = 2.4f,
                        Omega_3 = 0.0f,
                        Vitamin_D = 0,
                        Vitamin_C = 4.6f
                    ),
                    Product(
                        Title = "Куриная грудка",
                        Calories = 165,
                        Protein = 31,
                        Fat = 3,
                        Carbohydrates = 0,
                        Salt = 0.1,
                        Calcium = 11,
                        Magnesium = 29,
                        Potassium = 256,
                        Iron = 0.7f,
                        Fiber = 0.0f,
                        Omega_3 = 0.1f,
                        Vitamin_D = 0,
                        Vitamin_C = 0.0f
                    ),
                    Product(
                        Title = "Рис отварной",
                        Calories = 130,
                        Protein = 2,
                        Fat = 0,
                        Carbohydrates = 28,
                        Salt = 0.0,
                        Calcium = 10,
                        Magnesium = 12,
                        Potassium = 35,
                        Iron = 0.2f,
                        Fiber = 0.4f,
                        Omega_3 = 0.0f,
                        Vitamin_D = 0,
                        Vitamin_C = 0.0f
                    ),
                    Product(
                        Title = "Творог 5%",
                        Calories = 121,
                        Protein = 17,
                        Fat = 5,
                        Carbohydrates = 3,
                        Salt = 0.1,
                        Calcium = 150,
                        Magnesium = 23,
                        Potassium = 112,
                        Iron = 0.2f,
                        Fiber = 0.0f,
                        Omega_3 = 0.0f,
                        Vitamin_D = 0,
                        Vitamin_C = 0.0f
                    ),
                    Product(
                        Title = "Банан",
                        Calories = 89,
                        Protein = 1,
                        Fat = 0,
                        Carbohydrates = 22,
                        Salt = 0.0,
                        Calcium = 5,
                        Magnesium = 27,
                        Potassium = 358,
                        Iron = 0.3f,
                        Fiber = 2.6f,
                        Omega_3 = 0.0f,
                        Vitamin_D = 0,
                        Vitamin_C = 8.7f
                    )
                )
                testProducts.forEach { dao.insert(it) }
            }
        }
    }
} 