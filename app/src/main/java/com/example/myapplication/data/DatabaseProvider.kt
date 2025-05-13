package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.room.migration.Migration
import java.util.Date

@Database(entities = [Product::class, UserData::class], version = 6)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun userDataDao(): UserDataDao

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
                .addMigrations(MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
                .build()
                INSTANCE = inst
                inst
            }
            // Проверяем и заполняем тестовыми продуктами, если таблица пуста
            instance.populateIfEmpty(context)
            // Проверяем и создаем запись на текущий день
            instance.checkAndCreateTodayRecord(context)
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

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE products ADD COLUMN salt REAL NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE products ADD COLUMN calcium INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE products ADD COLUMN magnesium INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE products ADD COLUMN potassium INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE products ADD COLUMN iron REAL NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE products ADD COLUMN fiber REAL NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE products ADD COLUMN omega3 REAL NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE products ADD COLUMN vitamin_d INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE products ADD COLUMN vitamin_c INTEGER NOT NULL DEFAULT 0")
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS user_data (
                        date INTEGER NOT NULL PRIMARY KEY,
                        weight REAL NOT NULL DEFAULT 0,
                        calories INTEGER NOT NULL DEFAULT 0,
                        protein REAL NOT NULL DEFAULT 0,
                        fat REAL NOT NULL DEFAULT 0,
                        carbs REAL NOT NULL DEFAULT 0,
                        eaten INTEGER NOT NULL DEFAULT 0,
                        burned INTEGER NOT NULL DEFAULT 0,
                        salt REAL NOT NULL DEFAULT 0,
                        calcium INTEGER NOT NULL DEFAULT 0,
                        magnesium INTEGER NOT NULL DEFAULT 0,
                        potassium INTEGER NOT NULL DEFAULT 0,
                        iron REAL NOT NULL DEFAULT 0,
                        fiber REAL NOT NULL DEFAULT 0,
                        omega3 REAL NOT NULL DEFAULT 0,
                        vitamin_d INTEGER NOT NULL DEFAULT 0,
                        vitamin_c INTEGER NOT NULL DEFAULT 0
                    )
                """)
            }
        }
    }

    private fun checkAndCreateTodayRecord(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = this@AppDatabase.userDataDao()
            val today = Date()
            val todayRecord = dao.getByDate(today)
            
            if (todayRecord == null) {
                // Получаем данные из SharedPreferences
                val prefs = context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
                val weight = prefs.getString("weight", "")?.toDoubleOrNull() ?: 0.0
                val height = prefs.getString("height", "")?.toDoubleOrNull() ?: 0.0
                val age = prefs.getString("age", "")?.toDoubleOrNull() ?: 0.0
                val gender = prefs.getString("gender", "Мужской") ?: "Мужской"
                val lifestyle = prefs.getString("lifestyle", "Неактивный") ?: "Неактивный"
                val goal = prefs.getString("goal", "Поддержание веса") ?: "Поддержание веса"

                // Рассчитываем норму калорий
                // 1. BMR
                val bmr = if (gender == "Мужской") {
                    88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)
                } else {
                    447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age)
                }

                // 2. Коэффициент активности
                val activityFactor = when (lifestyle) {
                    "Неактивный" -> 1.2
                    "Умеренный" -> 1.375
                    "Активный" -> 1.55
                    "Очень активный" -> 1.725
                    else -> 1.2
                }
                var tdee = bmr * activityFactor

                // 3. Корректировка по цели
                tdee = when (goal) {
                    "Сброс веса" -> tdee * 0.9
                    "Набор массы" -> tdee * 1.1
                    else -> tdee
                }
                val caloriesBase = tdee.toInt()

                // Создаем новую запись на сегодня
                val newRecord = UserData(
                    date = today,
                    weight = weight.toFloat(),
                    calories = caloriesBase
                )
                dao.insert(newRecord)
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
                        rating = 3.0f,
                        salt = 0.01f,
                        calcium = 16,
                        magnesium = 5,
                        potassium = 107,
                        iron = 0.12f,
                        fiber = 2.4f,
                        omega3 = 0f,
                        vitaminD = 0,
                        vitaminC = 4
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
                        rating = 3.0f,
                        salt = 0.07f,
                        calcium = 15,
                        magnesium = 27,
                        potassium = 256,
                        iron = 0.7f,
                        fiber = 0f,
                        omega3 = 0.05f,
                        vitaminD = 0,
                        vitaminC = 0
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
                        rating = 3.0f,
                        salt = 0f,
                        calcium = 10,
                        magnesium = 12,
                        potassium = 35,
                        iron = 0.2f,
                        fiber = 0.4f,
                        omega3 = 0f,
                        vitaminD = 0,
                        vitaminC = 0
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
                        rating = 3.0f,
                        salt = 0.1f,
                        calcium = 164,
                        magnesium = 23,
                        potassium = 112,
                        iron = 0.4f,
                        fiber = 0f,
                        omega3 = 0.02f,
                        vitaminD = 0,
                        vitaminC = 0
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
                        rating = 3.0f,
                        salt = 0f,
                        calcium = 5,
                        magnesium = 27,
                        potassium = 358,
                        iron = 0.26f,
                        fiber = 2.6f,
                        omega3 = 0.03f,
                        vitaminD = 0,
                        vitaminC = 9
                    )
                )
                testProducts.forEach { dao.insert(it) }
            }
        }
    }
} 