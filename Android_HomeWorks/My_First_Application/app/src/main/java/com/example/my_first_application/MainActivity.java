package com.example.my_first_application;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация кнопок
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);

        // Установка обработчиков нажатий
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button1) {
            Toast.makeText(this, "Button 1 clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.button2) {
            Toast.makeText(this, "Button 2 clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.button3) {
            Toast.makeText(this, "Button 3 clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.button4) {
            Toast.makeText(this, "Button 4 clicked", Toast.LENGTH_SHORT).show();
        }
    }
}