package com.example.app_register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp; // Import FirebaseApp

public class MainActivity extends AppCompatActivity {
    private Button btnManageStudents, btnManageClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this); // Khởi tạo Firebase
        setContentView(R.layout.activity_main);

        btnManageStudents = findViewById(R.id.btnManageStudents);
        btnManageClasses = findViewById(R.id.btnManageClasses);

        btnManageStudents.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StudentManagementActivity.class);
            startActivity(intent);
        });

        btnManageClasses.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ClassManagementActivity.class);
            startActivity(intent);
        });
    }
}
