package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.myapplication.databinding.ActivityMainBinding;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Включаем поддержку векторных изображений для старых устройств
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        
        try {
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            // Инициализация FAB
            if (binding.fab != null) {
                binding.fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO: Implement add new entry functionality
                    }
                });
            } else {
                Log.e(TAG, "FAB is null");
            }

            // Проверяем остальные элементы
            if (binding.caloriesLeft != null) {
                binding.caloriesLeft.setText("452");
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error initializing layout", e);
            setContentView(R.layout.activity_main_fallback);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}