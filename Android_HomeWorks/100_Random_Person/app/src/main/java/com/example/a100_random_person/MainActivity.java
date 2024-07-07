package com.example.a100_random_person;

import android.os.Bundle; // Импортируем класс Bundle для работы с сохранённым состоянием активности
import android.widget.ListView; // Импортируем класс ListView для отображения списка элементов
import androidx.appcompat.app.AppCompatActivity; // Импортируем класс AppCompatActivity для совместимости с более старыми версиями Android
import java.util.List; // Импортируем класс List для работы с коллекциями

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Метод, который вызывается при создании активности
        super.onCreate(savedInstanceState); // Вызов базового метода для сохранения состояния
        setContentView(R.layout.activity_main); // Устанавливаем макет активности

        ListView listView = findViewById(R.id.user_list); // Находим элемент ListView в макете по его ID

        // Генерация 100 случайных пользователей
        List<UserModel> users = DataGenerator.generateUsers(100); // Создаём список пользователей, используя метод генерации случайных пользователей

        // Создание и установка адаптера
        UserAdapter adapter = new UserAdapter(this, users); // Создаём адаптер, передавая контекст и список пользователей
        listView.setAdapter(adapter); // Устанавливаем адаптер для ListView
    }
}
