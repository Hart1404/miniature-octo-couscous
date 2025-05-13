package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.widget.AppCompatImageButton
import com.example.myapplication.data.AppDatabase
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    private lateinit var barcodeButton: AppCompatImageButton
    private lateinit var editProfileButton: AppCompatImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called")
        setContentView(R.layout.activity_main)

        // Инициализация базы данных продуктов при первом запуске приложения
        AppDatabase.getDatabase(applicationContext)

        // Меняем цвет status bar на цвет шапки
        window.statusBarColor = resources.getColor(R.color.appBarColor, theme)

        // Устанавливаем высоту appBar = высота статус-бара + 35dp
        val statusBarHeight = getStatusBarHeight()
        val extraHeight = (35 * resources.displayMetrics.density).toInt()
        val totalHeight = statusBarHeight + extraHeight
        val appBar = findViewById<View>(R.id.appBar)
        val params = appBar.layoutParams
        params.height = totalHeight
        appBar.layoutParams = params

        barcodeButton = findViewById(R.id.barcodeButton)
        editProfileButton = findViewById(R.id.editProfileButton)
        barcodeButton.setOnClickListener {
            startActivity(Intent(this, BarcodeScannerActivity::class.java))
        }

        // Находим кнопку настроек и устанавливаем обработчик
        findViewById<ImageButton>(R.id.settingsButton).setOnClickListener {
            showSettingsDialog()
        }

        // Установка начального фрагмента
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        setupBottomNavigation()
    }

    fun loadFragment(fragment: Fragment) {
        Log.d("MainActivity", "loadFragment called with fragment: ${fragment.javaClass.simpleName}")
        try {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
            
            // Скрываем кнопки в зависимости от фрагмента
            when (fragment) {
                is ProfileFragment -> {
                    barcodeButton.visibility = View.GONE
                    editProfileButton.visibility = View.VISIBLE
                }
                else -> {
                    barcodeButton.visibility = if (fragment is HomeFragment) View.VISIBLE else View.GONE
                    editProfileButton.visibility = View.GONE
                }
            }
            Log.d("MainActivity", "Fragment loaded successfully")
        } catch (e: Exception) {
            Log.e("MainActivity", "Error loading fragment", e)
        }
    }

    fun saveProfileData() {
        // Переходим в раздел профиля
        loadFragment(ProfileFragment())
        findViewById<BottomNavigationView>(R.id.bottomNavView)?.selectedItemId = R.id.navigation_profile
    }

    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun setupBottomNavigation() {
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavView)

        bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, HomeFragment())
                        .commit()
                    barcodeButton.visibility = View.VISIBLE
                    true
                }
                R.id.navigation_profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, ProfileFragment())
                        .commit()
                    barcodeButton.visibility = View.GONE
                    true
                }
                R.id.navigation_statistics -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, StatisticsFragment())
                        .commit()
                    barcodeButton.visibility = View.GONE
                    true
                }
                R.id.navigation_recommendations -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, RecommendationsFragment())
                        .commit()
                    barcodeButton.visibility = View.GONE
                    true
                }
                else -> false
            }
        }
        // Устанавливаем HomeFragment как начальный фрагмент
        bottomNavView.selectedItemId = R.id.navigation_home
    }

    private fun showSettingsDialog() {
        Log.d("MainActivity", "showSettingsDialog called")
        val dialog = SettingsDialog(this)
        dialog.show()
    }
} 