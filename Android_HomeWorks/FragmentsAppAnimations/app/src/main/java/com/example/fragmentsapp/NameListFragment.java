package com.example.fragmentsapp;

// Импортируем необходимые классы и интерфейсы
import android.content.Context; // Контекст для доступа к ресурсам приложения и взаимодействия с системными сервисами
import android.os.Bundle; // Класс для передачи данных между активностями и фрагментами
import androidx.fragment.app.Fragment; // Класс для создания фрагментов, поддерживающих обратную совместимость
import android.view.LayoutInflater; // Класс для создания View из XML-ресурсов
import android.view.View; // Класс для представления UI-элементов
import android.view.ViewGroup; // Класс, представляющий контейнер для других View
import android.view.animation.Animation; // Класс для работы с анимацией
import android.view.animation.AnimationUtils; // Утилиты для загрузки анимаций из ресурсов
import android.view.animation.LayoutAnimationController; // Контроллер для управления анимацией макета
import android.widget.AdapterView; // Интерфейс для обработки кликов на элементах адаптера
import android.widget.ArrayAdapter; // Адаптер для привязки массива данных к View
import android.widget.GridView; // Виджет, который отображает элементы в виде сетки

// Фрагмент для отображения списка имен
public class NameListFragment extends Fragment {

    // Интерфейс для обработки выбора имени
    public interface OnNameSelectedListener {
        void onNameSelected(String name); // Метод, который будет вызван при выборе имени
    }

    private OnNameSelectedListener callback; // Переменная для хранения реализации интерфейса

    // Вызывается при присоединении фрагмента к активности
    @Override
    public void onAttach(Context context) {
        super.onAttach(context); // Вызов базового метода, обязательный при переопределении
        if (context instanceof OnNameSelectedListener) {
            callback = (OnNameSelectedListener) context; // Присваиваем переменной callback реализацию интерфейса
        } else {
            throw new RuntimeException(context.toString() + " must implement OnNameSelectedListener");
            // Если контекст не реализует интерфейс, выбрасываем исключение
        }
    }

    // Вызывается при создании представления фрагмента
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Инфлейтим (раздуваем) XML-ресурс и создаем View-дерево
        View view = inflater.inflate(R.layout.fragment_name_list, container, false);

        // Инициализация GridView
        GridView gridView = view.findViewById(R.id.gridView);

        // Установка анимации при скролле
        Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
        // Создаем анимацию с использованием AnimationUtils и задаем тип анимации (сдвиг слева)
        LayoutAnimationController controller = new LayoutAnimationController(animation, 0.2f);
        // Контроллер для управления анимацией отображения элементов
        gridView.setLayoutAnimation(controller);
        // Применяем контроллер анимации к GridView

        // Данные для отображения
        String[] names = {
                "Alexander", "Sophia", "Michael", "Emma",
                "Olivia", "James", "Isabella", "Benjamin",
                "Mia", "Ethan", "Ava", "William"
        };

        // Адаптер для GridView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, names);
        // Создаем адаптер, который преобразует массив строк в View-элементы
        gridView.setAdapter(adapter); // Привязываем адаптер к GridView

        // Обработчик нажатия на элемент GridView
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Обработчик кликов по элементам GridView
                String selectedName = (String) parent.getItemAtPosition(position); // Получаем выбранное имя
                if (callback != null) {
                    callback.onNameSelected(selectedName); // Вызываем метод интерфейса с выбранным именем
                }
            }
        });

        return view; // Возвращаем созданное View-дерево
    }
}