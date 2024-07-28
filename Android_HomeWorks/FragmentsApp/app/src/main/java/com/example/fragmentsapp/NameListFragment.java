package com.example.fragmentsapp;

// Импортируем необходимые классы и интерфейсы
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

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
        super.onAttach(context); // Вызов базового метода
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
        View view = inflater.inflate(R.layout.fragment_name_list, container, false); // Инфлейт макета фрагмента

        // Инициализация GridView
        GridView gridView = view.findViewById(R.id.gridView); // Получаем ссылку на GridView из макета

        // Данные для отображения
        String[] names = {
                "Alexander", "Sophia", "Michael", "Emma",
                "Olivia", "James", "Isabella", "Benjamin",
                "Mia", "Ethan", "Ava", "William"
        };

        // Адаптер для GridView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, names);
        // Создаем адаптер для отображения списка имен в GridView
        gridView.setAdapter(adapter); // Устанавливаем адаптер для GridView

        // Обработчик нажатия на элемент GridView
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedName = (String) parent.getItemAtPosition(position); // Получаем выбранное имя
                if (callback != null) {
                    callback.onNameSelected(selectedName); // Передаем выбранное имя в активность через callback
                }
            }
        });

        return view; // Возвращаем созданное представление фрагмента
    }
}
