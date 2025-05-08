package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.widget.AppCompatImageButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavView)

        // Находим кнопку сохранения и устанавливаем обработчик
        findViewById<AppCompatImageButton>(R.id.saveProfileButton)?.setOnClickListener {
            saveProfileData()
        }

        // Установка начального фрагмента
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.navigation_recommendations -> {
                    loadFragment(RecommendationsFragment())
                    true
                }
                R.id.navigation_statistics -> {
                    loadFragment(StatisticsFragment())
                    true
                }
                R.id.navigation_profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    fun saveProfileData() {
        // Переходим в раздел профиля
        loadFragment(ProfileFragment())
        findViewById<BottomNavigationView>(R.id.bottomNavView)?.selectedItemId = R.id.navigation_profile
    }
} 