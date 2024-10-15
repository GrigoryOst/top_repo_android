package com.example.calendar_android;

import android.app.TimePickerDialog; // Импорт диалога для выбора времени
import android.content.ContentValues; // Импорт для работы с данными, которые будут добавляться в календарь
import android.os.Bundle; // Импорт для передачи данных между состояниями Activity
import android.provider.CalendarContract; // Импорт контракта для работы с календарем
import android.widget.Button; // Импорт виджета кнопки
import android.widget.CheckBox; // Импорт виджета флажка (CheckBox)
import android.widget.EditText; // Импорт виджета поля для ввода текста
import android.widget.TimePicker; // Импорт виджета для выбора времени

import androidx.appcompat.app.AppCompatActivity; // Импорт основного класса для работы с Activity

public class AddEventActivity extends AppCompatActivity {

    // Объявление полей для ввода данных события
    private EditText titleEditText, descriptionEditText, startTimeEditText, endTimeEditText;
    private CheckBox allDayCheckBox; // Флажок для отметки события на весь день
    private Button saveButton; // Кнопка для сохранения события
    private long calendarId = 1; // Идентификатор календаря (по умолчанию)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Вызов метода родительского класса для создания Activity
        setContentView(R.layout.activity_add_event); // Устанавливаем макет для Activity

        // Находим элементы интерфейса по их ID
        titleEditText = findViewById(R.id.titleEditText); // Поле для ввода названия события
        descriptionEditText = findViewById(R.id.descriptionEditText); // Поле для ввода описания события
        startTimeEditText = findViewById(R.id.startTimeEditText); // Поле для ввода времени начала события
        endTimeEditText = findViewById(R.id.endTimeEditText); // Поле для ввода времени окончания события
        allDayCheckBox = findViewById(R.id.allDayCheckBox); // Флажок для события "Весь день"
        saveButton = findViewById(R.id.saveButton); // Кнопка для сохранения события

        // Обработка кликов на поле выбора времени начала и окончания события
        startTimeEditText.setOnClickListener(view -> showTimePicker(startTimeEditText)); // Открываем диалог выбора времени для начала
        endTimeEditText.setOnClickListener(view -> showTimePicker(endTimeEditText)); // Открываем диалог выбора времени для окончания

        // Обработка клика на кнопку сохранения события
        saveButton.setOnClickListener(view -> saveEvent()); // Вызываем метод для сохранения события

        // Отключаем поля ввода времени, если выбран флажок "Весь день"
        allDayCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) { // Если флажок выбран
                startTimeEditText.setEnabled(false); // Отключаем ввод времени начала
                endTimeEditText.setEnabled(false); // Отключаем ввод времени окончания
            } else { // Если флажок снят
                startTimeEditText.setEnabled(true); // Включаем ввод времени начала
                endTimeEditText.setEnabled(true); // Включаем ввод времени окончания
            }
        });
    }

    // Метод для отображения диалога выбора времени
    private void showTimePicker(EditText editText) {
        // Создаем диалог выбора времени
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (TimePicker view, int hourOfDay, int minute) -> {
                    // Устанавливаем выбранное время в поле ввода
                    editText.setText(hourOfDay + ":" + minute);
                }, 12, 0, true); // 12:00 по умолчанию, 24-часовой формат
        timePickerDialog.show(); // Отображаем диалог
    }

    // Метод для сохранения события в календарь
    private void saveEvent() {
        ContentValues values = new ContentValues(); // Создаем объект для хранения данных события
        values.put(CalendarContract.Events.CALENDAR_ID, calendarId); // Устанавливаем ID календаря
        values.put(CalendarContract.Events.TITLE, titleEditText.getText().toString()); // Устанавливаем название события
        values.put(CalendarContract.Events.DESCRIPTION, descriptionEditText.getText().toString()); // Устанавливаем описание события

        if (allDayCheckBox.isChecked()) { // Если событие "Весь день"
            values.put(CalendarContract.Events.ALL_DAY, 1); // Устанавливаем флаг "Весь день"
        } else {
            // Добавляем время начала и окончания события
            values.put(CalendarContract.Events.DTSTART, getTimeInMillis(startTimeEditText.getText().toString())); // Время начала
            values.put(CalendarContract.Events.DTEND, getTimeInMillis(endTimeEditText.getText().toString())); // Время окончания
        }

        values.put(CalendarContract.Events.EVENT_TIMEZONE, java.util.Calendar.getInstance().getTimeZone().getID()); // Устанавливаем текущий часовой пояс
        getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values); // Вставляем событие в календарь

        finish(); // Закрываем Activity после сохранения события
    }

    // Вспомогательный метод для конвертации времени в миллисекунды
    private long getTimeInMillis(String time) {
        String[] parts = time.split(":"); // Разделяем строку времени на часы и минуты
        int hour = Integer.parseInt(parts[0]); // Получаем часы
        int minute = Integer.parseInt(parts[1]); // Получаем минуты
        java.util.Calendar calendar = java.util.Calendar.getInstance(); // Создаем экземпляр календаря
        calendar.set(java.util.Calendar.HOUR_OF_DAY, hour); // Устанавливаем часы
        calendar.set(java.util.Calendar.MINUTE, minute); // Устанавливаем минуты
        return calendar.getTimeInMillis(); // Возвращаем время в миллисекундах
    }


}
