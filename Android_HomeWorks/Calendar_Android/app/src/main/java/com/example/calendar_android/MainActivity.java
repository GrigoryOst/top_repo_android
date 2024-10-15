package com.example.calendar_android;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView; // Объявляем переменную для календаря
    private ListView eventsListView; // Объявляем переменную для списка событий
    private FloatingActionButton fab; // Объявляем переменную для плавающей кнопки

    // Код запроса разрешений на доступ к календарю
    private static final int REQUEST_CALENDAR_PERMISSIONS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Вызов метода родительского класса для инициализации Activity
        setContentView(R.layout.activity_main); // Устанавливаем макет для Activity

        // Находим элементы интерфейса по их ID
        calendarView = findViewById(R.id.calendarView); // Инициализируем календарь
        eventsListView = findViewById(R.id.eventsListView); // Инициализируем список событий
        fab = findViewById(R.id.fab); // Инициализируем плавающую кнопку

        // Проверяем и запрашиваем разрешения на чтение и запись календаря
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // Если разрешения не даны, запрашиваем их
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR},
                    REQUEST_CALENDAR_PERMISSIONS);
        } else {
            // Если разрешения даны, загружаем события для текущей даты
            loadEventsForDay(calendarView.getDate());
        }

        // Обрабатываем изменения даты в CalendarView
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Получаем выбранную дату в миллисекундах
            long selectedDate = getMillisFromDate(year, month, dayOfMonth);
            // Загружаем события для выбранной даты
            loadEventsForDay(selectedDate);
        });

        // Обрабатываем нажатие на плавающую кнопку (для добавления события)
        fab.setOnClickListener(view -> {
            // Переход на экран добавления события
            Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
            startActivity(intent);
        });

        // Обрабатываем нажатие на элемент списка событий
        eventsListView.setOnItemClickListener((adapterView, view, position, id) -> {
            // Здесь можно добавить функционал для отображения деталей события или его редактирования
        });

        // Устанавливаем слушатель длительного нажатия на элемент списка событий
        eventsListView.setOnItemLongClickListener((adapterView, view, position, id) -> {

            // Создаем диалоговое окно с заголовком "Редактировать или удалить событие"
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Edit or Delete Event") // Устанавливаем заголовок окна

                    // Устанавливаем варианты действий: "Редактировать" и "Удалить"
                    .setItems(new String[]{"Edit", "Delete"}, (dialog, which) -> {

                        // Если выбрано действие "Редактировать"
                        if (which == 0) {
                            // Открываем активити для редактирования события
                            Intent intent = new Intent(MainActivity.this, EditEventActivity.class);
                            intent.putExtra("eventId", id); // Передаем ID события для редактирования
                            startActivity(intent); // Запускаем активити редактирования

                            // Если выбрано действие "Удалить"
                        } else if (which == 1) {
                            // Создаем URI для удаления события по его ID
                            Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id);
                            // Удаляем событие из календаря
                            getContentResolver().delete(deleteUri, null, null);
                        }
                    })
                    .show(); // Отображаем диалоговое окно

            return true; // Возвращаем true, чтобы показать, что событие длительного нажатия обработано
        });

    }

    // Загружаем события для выбранного дня
    private void loadEventsForDay(long dayInMillis) {
        // Список для хранения заголовков событий
        List<String> eventTitles = new ArrayList<>();

        // URI для доступа к событиям в календаре
        Uri uri = CalendarContract.Events.CONTENT_URI;

        // Определяем, какие столбцы будем извлекать
        String[] projection = {
                Events.CALENDAR_ID,      // ID календаря
                Events.TITLE,            // Заголовок события
                Events.DESCRIPTION,       // Описание события
                Events.DTSTART,          // Время начала события
                Events.DTEND,            // Время окончания события
                Events.EVENT_COLOR       // Цвет события
        };

        // Условие выборки: события за конкретный день
        String selection = CalendarContract.Events.DTSTART + " >= ? AND " + CalendarContract.Events.DTSTART + " < ?";

        // Аргументы выборки: начало и конец дня в миллисекундах
        String[] selectionArgs = new String[]{
                String.valueOf(dayInMillis),                        // Начало дня
                String.valueOf(dayInMillis + 24 * 60 * 60 * 1000)   // Конец дня (прибавляем 24 часа)
        };

        // Выполняем запрос к контент-провайдеру и получаем курсор с данными
        try (Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null)) {
            if (cursor != null) {
                // Проходим по всем результатам запроса
                while (cursor.moveToNext()) {
                    // Извлекаем заголовок события и добавляем его в список
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(Events.TITLE));
                    eventTitles.add(title);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Обрабатываем возможные ошибки
        }

        // Создаем адаптер для отображения заголовков событий в ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventTitles);

        // Устанавливаем адаптер для ListView
        eventsListView.setAdapter(adapter);
    }

    // Конвертируем выбранную дату в миллисекунды
    private long getMillisFromDate(int year, int month, int day) {
        java.util.Calendar calendar = java.util.Calendar.getInstance(); // Создаем экземпляр календаря
        calendar.set(year, month, day, 0, 0, 0); // Устанавливаем год, месяц, день и время (начало дня)
        return calendar.getTimeInMillis(); // Возвращаем дату в миллисекундах
    }

    // Обрабатываем результат запроса разрешений
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Вызываем метод родителя
        if (requestCode == REQUEST_CALENDAR_PERMISSIONS) { // Проверяем код запроса
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Если разрешение получено, загружаем события для текущей даты
                loadEventsForDay(calendarView.getDate());
            }
        }
    }


}