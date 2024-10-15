package com.example.calendar_android;

import android.app.TimePickerDialog; // Импортируем класс для отображения диалога выбора времени
import android.content.ContentUris; // Импортируем класс для работы с URI, позволяющий работать с контент-провайдерами
import android.content.ContentValues; // Импортируем класс для хранения пар "ключ-значение", используемых для обновления данных
import android.database.Cursor; // Импортируем класс для работы с курсорами, возвращаемыми запросами из базы данных
import android.net.Uri; // Импортируем класс для представления URI (унифицированного идентификатора ресурсов)
import android.os.Bundle; // Импортируем класс для передачи данных между компонентами и сохранения состояния активностей
import android.provider.CalendarContract; // Импортируем класс для взаимодействия с календарем Android через контент-провайдер
import android.widget.Button; // Импортируем класс для работы с кнопками
import android.widget.CheckBox; // Импортируем класс для работы с флажками
import android.widget.EditText; // Импортируем класс для работы с текстовыми полями
import android.widget.TimePicker; // Импортируем класс для работы с выбором времени

import androidx.appcompat.app.AppCompatActivity; // Импортируем базовый класс для активности с поддержкой ActionBar

public class EditEventActivity extends AppCompatActivity { // Определяем новый класс активности для редактирования события

    // Объявляем переменные для элементов пользовательского интерфейса
    private EditText titleEditText, descriptionEditText, startTimeEditText, endTimeEditText;
    private CheckBox allDayCheckBox; // Флажок для выбора события на весь день
    private Button saveButton; // Кнопка для сохранения изменений
    private long eventId; // ID события, которое нужно отредактировать

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Метод, вызываемый при создании активности
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event); // Устанавливаем макет активности

        // Получаем ID события из Intent
        eventId = getIntent().getLongExtra("eventId", -1); // Извлекаем переданное ID события, которое нужно редактировать

        // Инициализация UI элементов
        titleEditText = findViewById(R.id.titleEditText); // Находим поле для ввода заголовка
        descriptionEditText = findViewById(R.id.descriptionEditText); // Находим поле для ввода описания
        startTimeEditText = findViewById(R.id.startTimeEditText); // Находим поле для ввода времени начала
        endTimeEditText = findViewById(R.id.endTimeEditText); // Находим поле для ввода времени окончания
        allDayCheckBox = findViewById(R.id.allDayCheckBox); // Находим флажок для выбора "Весь день"
        saveButton = findViewById(R.id.saveButton); // Находим кнопку для сохранения изменений

        // Загружаем данные о событии
        loadEventData(eventId); // Загружаем данные события для редактирования

        // Time pickers для начала и конца события
        startTimeEditText.setOnClickListener(view -> showTimePicker(startTimeEditText)); // Устанавливаем обработчик для показа выбора времени начала
        endTimeEditText.setOnClickListener(view -> showTimePicker(endTimeEditText)); // Устанавливаем обработчик для показа выбора времени окончания

        // Обработчик для сохранения изменений
        saveButton.setOnClickListener(view -> updateEvent()); // Устанавливаем обработчик для кнопки сохранения изменений

        // Делаем поля времени неактивными, если выбрано "Весь день"
        allDayCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) { // Если флажок "Весь день" активирован
                startTimeEditText.setEnabled(false); // Отключаем возможность редактирования времени начала
                endTimeEditText.setEnabled(false); // Отключаем возможность редактирования времени окончания
            } else { // Если флажок "Весь день" деактивирован
                startTimeEditText.setEnabled(true); // Включаем редактирование времени начала
                endTimeEditText.setEnabled(true); // Включаем редактирование времени окончания
            }
        });
    }

    // Загрузка данных о событии для редактирования

    private void loadEventData(long eventId) {
        // Создаем URI для доступа к событию
        Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId);

        // Определяем поля, которые нужно получить из базы данных
        String[] projection = {
                CalendarContract.Events.TITLE,         // Заголовок события
                CalendarContract.Events.DESCRIPTION,   // Описание события
                CalendarContract.Events.DTSTART,       // Время начала события
                CalendarContract.Events.DTEND,         // Время окончания события
                CalendarContract.Events.ALL_DAY        // Информация о том, является ли событие событием на весь день
        };

        // Выполняем запрос для получения данных события
        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                // Извлекаем заголовок события
                String title = cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events.TITLE));
                // Извлекаем описание события
                String description = cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events.DESCRIPTION));
                // Извлекаем время начала
                long dtStart = cursor.getLong(cursor.getColumnIndexOrThrow(CalendarContract.Events.DTSTART));
                // Извлекаем время окончания
                long dtEnd = cursor.getLong(cursor.getColumnIndexOrThrow(CalendarContract.Events.DTEND));
                // Извлекаем флаг события "Весь день"
                int allDay = cursor.getInt(cursor.getColumnIndexOrThrow(CalendarContract.Events.ALL_DAY));

                // Устанавливаем заголовок в текстовом поле
                titleEditText.setText(title);
                // Устанавливаем описание в текстовом поле
                descriptionEditText.setText(description);

                // Если событие на весь день
                if (allDay == 1) {
                    // Устанавливаем флажок "Весь день"
                    allDayCheckBox.setChecked(true);
                    // Отключаем поля для времени
                    startTimeEditText.setEnabled(false);
                    endTimeEditText.setEnabled(false);
                } else {
                    // Если событие не на весь день, устанавливаем время начала и окончания
                    startTimeEditText.setText(getTimeFromMillis(dtStart));
                    endTimeEditText.setText(getTimeFromMillis(dtEnd));
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Обрабатываем возможные ошибки
        }
    }

    // Показать диалог выбора времени
    private void showTimePicker(EditText editText) { // Метод для показа диалога выбора времени
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, // Создаем диалог выбора времени
                (TimePicker view, int hourOfDay, int minute) -> { // Обработчик выбора времени
                    editText.setText(hourOfDay + ":" + minute); // Устанавливаем выбранное время в текстовом поле
                }, 12, 0, true); // Устанавливаем время по умолчанию (12:00) и формат 24-часового времени
        timePickerDialog.show(); // Показываем диалог
    }

    // Обновление события в календаре
    private void updateEvent() { // Метод для обновления события в календаре
        ContentValues values = new ContentValues(); // Создаем объект для хранения новых значений полей
        values.put(CalendarContract.Events.TITLE, titleEditText.getText().toString()); // Обновляем заголовок события
        values.put(CalendarContract.Events.DESCRIPTION, descriptionEditText.getText().toString()); // Обновляем описание события

        if (allDayCheckBox.isChecked()) { // Если событие отмечено как "Весь день"
            values.put(CalendarContract.Events.ALL_DAY, 1); // Устанавливаем флаг "Весь день"
        } else { // Если событие не на весь день
            // Добавляем время начала и окончания
            values.put(CalendarContract.Events.DTSTART, getTimeInMillis(startTimeEditText.getText().toString())); // Обновляем время начала
            values.put(CalendarContract.Events.DTEND, getTimeInMillis(endTimeEditText.getText().toString())); // Обновляем время окончания
        }

        values.put(CalendarContract.Events.EVENT_TIMEZONE, java.util.Calendar.getInstance().getTimeZone().getID()); // Устанавливаем часовой пояс

        Uri updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId); // Создаем URI для обновления события
        getContentResolver().update(updateUri, values, null, null); // Выполняем обновление события

        finish(); // Закрываем активити после обновления
    }

    // Преобразуем строку времени в миллисекунды
    private long getTimeInMillis(String time) { // Метод для преобразования строки времени в миллисекунды
        String[] parts = time.split(":"); // Разделяем строку времени на часы и минуты
        int hour = Integer.parseInt(parts[0]); // Преобразуем часы в число
        int minute = Integer.parseInt(parts[1]); // Преобразуем минуты в число
        java.util.Calendar calendar = java.util.Calendar.getInstance(); // Создаем объект Calendar
        calendar.set(java.util.Calendar.HOUR_OF_DAY, hour); // Устанавливаем часы в календаре
        calendar.set(java.util.Calendar.MINUTE, minute); // Устанавливаем минуты в календаре
        return calendar.getTimeInMillis(); // Возвращаем время в миллисекундах
    }

    // Преобразуем миллисекунды в строку времени (часы:минуты)
    private String getTimeFromMillis(long millis) { // Метод для преобразования миллисекунд в строку времени
        java.util.Calendar calendar = java.util.Calendar.getInstance(); // Создаем объект Calendar
        calendar.setTimeInMillis(millis); // Устанавливаем время в миллисекундах
        int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY); // Получаем часы
        int minute = calendar.get(java.util.Calendar.MINUTE); // Получаем минуты
        return hour + ":" + minute; // Возвращаем строку времени (часы:минуты)
    }
}

