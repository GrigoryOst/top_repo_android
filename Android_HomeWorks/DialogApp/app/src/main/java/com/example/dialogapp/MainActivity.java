package com.example.dialogapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    // Переменная для хранения EditText
    private EditText editText; // Поле ввода текста, которое будет отображать введенный текст

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Метод, вызываемый при создании активности
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Устанавливаем макет активности

        // Инициализация EditText
        editText = findViewById(R.id.editText); // Связываем переменную с элементом макета

        // Инициализация Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar); // Связываем переменную с Toolbar в макете
        setSupportActionBar(toolbar); // Устанавливаем Toolbar как ActionBar для активности
    }

    // Создание меню в ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // Метод, создающий меню в ActionBar
        // Подключаем меню из XML файла
        getMenuInflater().inflate(R.menu.menu_main, menu); // Инфлейт (раздувание) меню из XML ресурса
        return true; // Возвращаем true, чтобы меню отображалось
    }

    // Обработка нажатий на элементы меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // Метод, обрабатывающий нажатия на элементы меню
        int id = item.getItemId(); // Получаем идентификатор выбранного элемента

        if (id == R.id.action_edit_text) { // Если выбран элемент "Редактировать текст"
            // Показ диалога редактирования текста
            showEditTextDialog(); // Вызываем метод для показа диалога
            return true;
        } else if (id == R.id.action_show_alert) { // Если выбран элемент "Вызов оповещения"
            // Показ оповещения
            showAlert(); // Вызываем метод для показа оповещения
            return true;
        }

        return super.onOptionsItemSelected(item); // Возвращаем результат работы родительского метода
    }

    // Метод для показа диалога редактирования текста
    private void showEditTextDialog() {
        // Создаем новый EditText для диалога
        final EditText dialogEditText = new EditText(this); // Создаем поле ввода текста
        dialogEditText.setHint("Введите текст"); // Устанавливаем подсказку для пользователя

        // Создание AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // Создаем билдер для диалога
        builder.setTitle("Редактировать текст") // Устанавливаем заголовок диалога
                .setView(dialogEditText) // Устанавливаем EditText в диалоге
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() { // Кнопка "ОК"
                    @Override
                    public void onClick(DialogInterface dialog, int which) { // Обработчик нажатия кнопки
                        // Получаем введенный текст и устанавливаем его в основной EditText
                        String text = dialogEditText.getText().toString(); // Получаем текст из диалога
                        editText.setText(text); // Устанавливаем текст в основном EditText
                    }
                })
                .setNegativeButton("ОТМЕНА", null) // Кнопка "ОТМЕНА", не выполняет никаких действий
                .show(); // Показываем диалог
    }

    // Метод для показа оповещения
    private void showAlert() {
        // Получаем текст из EditText
        String text = editText.getText().toString(); // Получаем текст из основного EditText

        if (text.isEmpty()) { // Если текст пустой
            Toast.makeText(this, "Текстовое поле пустое!", Toast.LENGTH_SHORT).show(); // Показываем сообщение
        } else {
            // Получаем текущее время
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

            // Создание AlertDialog для оповещения
            AlertDialog.Builder builder = new AlertDialog.Builder(this); // Создаем билдер для диалога
            builder.setTitle("Оповещение") // Устанавливаем заголовок диалога
                    .setMessage("Время: " + currentTime + "\nСообщение: " + text) // Устанавливаем текст сообщения с временем и содержанием
                    .setPositiveButton("ОК", null) // Кнопка "ОК", не выполняет никаких действий
                    .show(); // Показываем диалог
        }
    }
}