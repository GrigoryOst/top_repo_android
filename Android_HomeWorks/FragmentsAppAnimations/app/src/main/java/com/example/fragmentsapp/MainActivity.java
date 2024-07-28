package com.example.fragmentsapp;

import android.os.Bundle;

// Импортируем необходимые библиотеки
import androidx.appcompat.app.AppCompatActivity; // Библиотека для поддержки функциональности AppCompatActivity
import androidx.fragment.app.Fragment; // Библиотека для работы с фрагментами
import androidx.fragment.app.FragmentTransaction; // Библиотека для управления транзакциями фрагментов
import java.util.ArrayList; // Библиотека для работы с коллекциями типа ArrayList
import java.util.List; // Библиотека для работы с коллекциями типа List

// Основная активность приложения
public class MainActivity extends AppCompatActivity implements NameListFragment.OnNameSelectedListener {

    // Список моделей имен
    private List<NameModel> nameList; // Коллекция для хранения данных о именах

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Вызов метода родительского класса для создания активности
        setContentView(R.layout.activity_main); // Установка макета активности

        // Инициализируем список имен
        nameList = new ArrayList<>(); // Создаем новый список имен
        nameList.add(new NameModel("Alexander", "Defender of the people", "March 12, April 22, May 10")); // Добавляем имя и его описание в список
        nameList.add(new NameModel("Sophia", "Wisdom", "September 30")); // Добавляем имя и его описание в список
        nameList.add(new NameModel("Michael", "Who is like God", "September 29")); // Добавляем имя и его описание в список
        nameList.add(new NameModel("Emma", "Whole or universal", "July 19")); // Добавляем имя и его описание в список
        nameList.add(new NameModel("Olivia", "Olive tree", "October 5")); // Добавляем имя и его описание в список
        nameList.add(new NameModel("James", "Supplanter", "May 3, July 25")); // Добавляем имя и его описание в список
        nameList.add(new NameModel("Isabella", "God is my oath", "November 19")); // Добавляем имя и его описание в список
        nameList.add(new NameModel("Benjamin", "Son of the right hand", "March 31")); // Добавляем имя и его описание в список
        nameList.add(new NameModel("Mia", "Mine or bitter", "August 15")); // Добавляем имя и его описание в список
        nameList.add(new NameModel("Ethan", "Strong, firm", "January 3, October 14")); // Добавляем имя и его описание в список
        nameList.add(new NameModel("Ava", "Life or bird", "April 29")); // Добавляем имя и его описание в список
        nameList.add(new NameModel("William", "Resolute protector", "January 10, June 25")); // Добавляем имя и его описание в список

        // Загружаем начальный фрагмент с списком имен
        loadFragment(new NameListFragment(), false); // Загружаем NameListFragment без добавления в back stack
    }

    // Метод для загрузки фрагментов
    private void loadFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right); // Задание анимаций для фрагментов
        transaction.replace(R.id.fragment_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null); // Добавление в стек, если требуется
        }
        transaction.commit();
    }

    // Метод, вызываемый при выборе имени в списке
    @Override
    public void onNameSelected(String name) {
        NameModel selectedName = findNameByName(name); // Находим модель по имени

        if (selectedName != null) {
            // Создаем фрагмент с деталями имени и передаем ему данные
            NameDetailFragment detailFragment = NameDetailFragment.newInstance(
                    selectedName.getName(), // Передаем имя
                    selectedName.getMeaning(), // Передаем значение имени
                    selectedName.getDates() // Передаем даты именин
            );
            loadFragment(detailFragment, true); // Заменяем фрагмент списка на фрагмент с деталями, добавляя в back stack
        }
    }

    // Метод для поиска модели по имени
    private NameModel findNameByName(String name) {
        for (NameModel nameModel : nameList) { // Проходим по списку моделей имен
            if (nameModel.getName().equals(name)) { // Проверяем, совпадает ли имя с искомым
                return nameModel; // Возвращаем найденную модель
            }
        }
        return null; // Возвращаем null, если модель не найдена
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left); // Анимация при запуске активности
    }

    @Override
    protected void onStop() {
        super.onStop();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right); // Анимация при завершении активности
    }

}