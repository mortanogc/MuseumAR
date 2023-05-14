package com.example.museumar.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.museumar.R;
import com.example.museumar.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = binding.bottomNavView;
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Добавим слушатель для пятой кнопки в BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_old_menu) {
                binding.drawerLayout.openDrawer(binding.navView);
                return true;
            }
            return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
        });

        // Настраиваем NavigationView для закрытия меню при нажатии на элементы и перехода к соответствующим фрагментам
        NavigationView navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            binding.drawerLayout.closeDrawers();

            int itemId = item.getItemId();
            switch (itemId) {
                case R.id.nav_home:
                    navController.navigate(R.id.nav_home);
                    break;
                case R.id.nav_map:
                    navController.navigate(R.id.nav_map);
                    break;
                case R.id.nav_about_museum:
                    navController.navigate(R.id.nav_about_museum);
                    break;
                case R.id.nav_about_app:
                    navController.navigate(R.id.nav_about_app);
                    break;
            }
            return true;
        });
    }

    private static final int REQUEST_CODE_PERMISSIONS = 101;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
    };

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, R.string.permissions_not_granted, Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

