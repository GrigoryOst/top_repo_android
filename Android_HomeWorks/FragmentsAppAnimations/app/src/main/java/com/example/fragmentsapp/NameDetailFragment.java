package com.example.fragmentsapp;

// Импортируем необходимые классы
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// Фрагмент для отображения деталей имени
public class NameDetailFragment extends Fragment {

    // Константы для передачи данных через аргументы
    private static final String ARG_NAME = "name"; // Ключ для имени
    private static final String ARG_MEANING = "meaning"; // Ключ для значения имени
    private static final String ARG_DATES = "dates"; // Ключ для дат именин

    private String name; // Переменная для хранения имени
    private String meaning; // Переменная для хранения значения имени
    private String dates; // Переменная для хранения дат именин

    // Метод для создания нового экземпляра фрагмента с передачей данных
    public static NameDetailFragment newInstance(String name, String meaning, String dates) {
        NameDetailFragment fragment = new NameDetailFragment(); // Создаем новый экземпляр фрагмента
        Bundle args = new Bundle(); // Создаем объект Bundle для хранения аргументов
        args.putString(ARG_NAME, name); // Добавляем имя в Bundle
        args.putString(ARG_MEANING, meaning); // Добавляем значение имени в Bundle
        args.putString(ARG_DATES, dates); // Добавляем даты именин в Bundle
        fragment.setArguments(args); // Устанавливаем аргументы для фрагмента
        return fragment; // Возвращаем новый экземпляр фрагмента
    }

    // Вызывается при создании фрагмента
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Вызов базового метода
        if (getArguments() != null) {
            // Получаем данные из аргументов
            name = getArguments().getString(ARG_NAME); // Извлекаем имя из аргументов
            meaning = getArguments().getString(ARG_MEANING); // Извлекаем значение имени из аргументов
            dates = getArguments().getString(ARG_DATES); // Извлекаем даты именин из аргументов
        }
    }

    // Вызывается для создания иерархии представлений фрагмента
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_name_detail, container, false); // Инфлейт макета фрагмента

        // Инициализация TextView для отображения данных
        TextView nameTextView = view.findViewById(R.id.nameTextView); // Получаем ссылку на TextView для имени
        TextView meaningTextView = view.findViewById(R.id.meaningTextView); // Получаем ссылку на TextView для значения имени
        TextView datesTextView = view.findViewById(R.id.datesTextView); // Получаем ссылку на TextView для дат именин

        // Установка текста на TextView
        nameTextView.setText(name); // Устанавливаем имя в TextView
        meaningTextView.setText(meaning); // Устанавливаем значение имени в TextView
        datesTextView.setText(dates); // Устанавливаем даты именин в TextView

        return view; // Возвращаем созданное представление фрагмента
    }
}