package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.room.migration.Migration

@Database(entities = [Product::class], version = 4)
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
                )
                .addMigrations(MIGRATION_3_4)
                .build()
                INSTANCE = inst
                inst
            }
            // Проверяем и заполняем тестовыми продуктами, если таблица пуста
            instance.populateIfEmpty(context)
            return instance
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Создаем временную таблицу с новой структурой
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS products_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        calories INTEGER NOT NULL,
                        protein REAL NOT NULL,
                        fat REAL NOT NULL,
                        carbs REAL NOT NULL,
                        barcode TEXT,
                        is_protein_tag INTEGER NOT NULL DEFAULT 0,
                        is_fast_tag INTEGER NOT NULL DEFAULT 0,
                        is_vegan_tag INTEGER NOT NULL DEFAULT 0,
                        is_vegetarian_tag INTEGER NOT NULL DEFAULT 0,
                        is_sea_tag INTEGER NOT NULL DEFAULT 0,
                        is_light_tag INTEGER NOT NULL DEFAULT 0,
                        is_breakfast_tag INTEGER NOT NULL DEFAULT 0,
                        is_lunch_tag INTEGER NOT NULL DEFAULT 0,
                        is_dinner_tag INTEGER NOT NULL DEFAULT 0,
                        is_snack_tag INTEGER NOT NULL DEFAULT 0,
                        is_gluten_free INTEGER NOT NULL DEFAULT 0,
                        is_lactose_free INTEGER NOT NULL DEFAULT 0,
                        is_nuts_free INTEGER NOT NULL DEFAULT 0,
                        is_eggs_free INTEGER NOT NULL DEFAULT 0,
                        is_fish_free INTEGER NOT NULL DEFAULT 0,
                        is_peanut_tag INTEGER NOT NULL DEFAULT 0,
                        is_fruits_tag INTEGER NOT NULL DEFAULT 0,
                        is_chocolate_tag INTEGER NOT NULL DEFAULT 0,
                        is_berries_tag INTEGER NOT NULL DEFAULT 0,
                        is_vegetables_tag INTEGER NOT NULL DEFAULT 0,
                        rating REAL NOT NULL DEFAULT 3.0
                    )
                """)

                // Копируем данные из старой таблицы в новую
                database.execSQL("""
                    INSERT INTO products_new (
                        id, name, calories, protein, fat, carbs,
                        barcode, is_protein_tag, is_fast_tag,
                        is_vegan_tag, is_vegetarian_tag, is_sea_tag, is_light_tag, is_breakfast_tag, is_lunch_tag, is_dinner_tag, is_snack_tag,
                        is_gluten_free, is_lactose_free, is_nuts_free, is_eggs_free, is_fish_free,
                        is_peanut_tag, is_fruits_tag, is_chocolate_tag, is_berries_tag, is_vegetables_tag,
                        rating
                    )
                    SELECT 
                        id, name, calories, protein, fat, carbs,
                        barcode, is_protein_tag, is_fast_tag,
                        is_vegan_tag, is_vegetarian_tag, is_sea_tag, is_light_tag, is_breakfast_tag, is_lunch_tag, is_dinner_tag, is_snack_tag,
                        is_gluten_free, is_lactose_free, is_nuts_free, is_eggs_free, is_fish_free,
                        is_peanut_tag, is_fruits_tag, is_chocolate_tag, is_berries_tag, is_vegetables_tag,
                        rating
                    FROM products
                """)

                // Удаляем старую таблицу
                database.execSQL("DROP TABLE products")

                // Переименовываем новую таблицу
                database.execSQL("ALTER TABLE products_new RENAME TO products")
            }
        }
    }

    fun populateIfEmpty(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = this@AppDatabase.productDao()
            if (dao.getAll().isEmpty()) {
                val testProducts = listOf(
                    Product(
                        name = "Яблоко",
                        calories = 52,
                        protein = 0.3f,
                        fat = 0.2f,
                        carbs = 14.0f,
                        barcode = "0",
                        isVeganTag = true,
                        isVegetarianTag = true,
                        isLightTag = true,
                        isFruitsTag = true,
                        isGlutenFree = true,
                        isLactoseFree = true,
                        isNutsFree = true,
                        isEggsFree = true,
                        isFishFree = true,
                        rating = 3.0f
                    ),
                    Product(
                        name = "Куриная грудка",
                        calories = 165,
                        protein = 31.0f,
                        fat = 3.6f,
                        carbs = 0.0f,
                        barcode = "0",
                        isProteinTag = true,
                        isLightTag = true,
                        isGlutenFree = true,
                        isLactoseFree = true,
                        isNutsFree = true,
                        isEggsFree = true,
                        isFishFree = true,
                        rating = 3.0f
                    ),
                    Product(
                        name = "Рис отварной",
                        calories = 130,
                        protein = 2.7f,
                        fat = 0.3f,
                        carbs = 28.0f,
                        barcode = "0",
                        isFastTag = true,
                        isVeganTag = true,
                        isVegetarianTag = true,
                        isLightTag = true,
                        isGlutenFree = true,
                        isLactoseFree = true,
                        isNutsFree = true,
                        isEggsFree = true,
                        isFishFree = true,
                        rating = 3.0f
                    ),
                    Product(
                        name = "Творог 5%",
                        calories = 121,
                        protein = 17.0f,
                        fat = 5.0f,
                        carbs = 3.0f,
                        barcode = "0",
                        isProteinTag = true,
                        isVegetarianTag = true,
                        isLightTag = true,
                        isGlutenFree = true,
                        isNutsFree = true,
                        isEggsFree = true,
                        isFishFree = true,
                        rating = 3.0f
                    ),
                    Product(
                        name = "Банан",
                        calories = 89,
                        protein = 1.1f,
                        fat = 0.3f,
                        carbs = 22.8f,
                        barcode = "0",
                        isVeganTag = true,
                        isVegetarianTag = true,
                        isLightTag = true,
                        isFruitsTag = true,
                        isGlutenFree = true,
                        isLactoseFree = true,
                        isNutsFree = true,
                        isEggsFree = true,
                        isFishFree = true,
                        rating = 3.0f
                    )
                )
                testProducts.forEach { dao.insert(it) }
            }
        }
    }
} 