package com.example.a100_random_person;

import android.content.Context; // Импортируем класс Context, который предоставляет доступ к глобальной информации об окружении приложения
import android.view.LayoutInflater; // Импортируем класс LayoutInflater для создания представлений из XML-файлов макета
import android.view.View; // Импортируем класс View, представляющий базовый класс для всех виджетов пользовательского интерфейса
import android.view.ViewGroup; // Импортируем класс ViewGroup, представляющий специальный виджет, который может содержать другие виджеты (представления)
import android.widget.BaseAdapter; // Импортируем класс BaseAdapter, который представляет абстрактный базовый класс для адаптеров, которые используются для заполнения списков
import android.widget.ImageView; // Импортируем класс ImageView, представляющий виджет для отображения изображений
import android.widget.TextView; // Импортируем класс TextView, представляющий виджет для отображения текста

import java.util.List; // Импортируем интерфейс List для работы со списками

public class UserAdapter extends BaseAdapter { // Объявляем класс UserAdapter, который наследуется от BaseAdapter и используется для заполнения списка
    private Context context; // Поле для хранения контекста
    private List<UserModel> users; // Поле для хранения списка пользователей
    private LayoutInflater inflater; // Поле для хранения LayoutInflater

    public UserAdapter(Context context, List<UserModel> users) { // Конструктор класса UserAdapter
        this.context = context; // Инициализируем поле context переданным значением
        this.users = users; // Инициализируем поле users переданным списком пользователей
        this.inflater = LayoutInflater.from(context); // Инициализируем LayoutInflater, используя переданный контекст
    }

    @Override
    public int getCount() { // Метод для получения количества элементов в списке
        return users.size(); // Возвращаем размер списка пользователей
    }

    @Override
    public Object getItem(int position) { // Метод для получения элемента списка по позиции
        return users.get(position); // Возвращаем пользователя из списка по указанной позиции
    }

    @Override
    public long getItemId(int position) { // Метод для получения идентификатора элемента списка по позиции
        return position; // Возвращаем позицию в качестве идентификатора
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { // Метод для создания и возврата представления для каждого элемента списка
        ViewHolder holder; // Объявляем переменную ViewHolder

        if (convertView == null) { // Проверяем, если convertView не существует
            convertView = inflater.inflate(R.layout.user_list_item, parent, false); // Создаем новое представление, используя файл макета user_list_item
            holder = new ViewHolder(); // Создаем новый объект ViewHolder
            holder.avatar = convertView.findViewById(R.id.avatar); // Инициализируем поле avatar, находя виджет по его идентификатору
            holder.name = convertView.findViewById(R.id.name); // Инициализируем поле name, находя виджет по его идентификатору
            holder.age = convertView.findViewById(R.id.age); // Инициализируем поле age, находя виджет по его идентификатору
            holder.location = convertView.findViewById(R.id.location); // Инициализируем поле location, находя виджет по его идентификатору
            convertView.setTag(holder); // Сохраняем объект ViewHolder в теге представления
        } else { // Если convertView уже существует
            holder = (ViewHolder) convertView.getTag(); // Получаем объект ViewHolder из тега представления
        }

        UserModel user = users.get(position); // Получаем текущего пользователя из списка по позиции
        holder.avatar.setImageResource(user.getAvatarId()); // Устанавливаем изображение аватара
        holder.name.setText(user.getFirstName() + " " + user.getLastName()); // Устанавливаем текст имени и фамилии
        holder.age.setText("Age: " + user.getAge()); // Устанавливаем текст возраста
        holder.location.setText(user.getCity() + ", " + user.getCountry()); // Устанавливаем текст местоположения (город, страна)

        return convertView; // Возвращаем представление элемента списка
    }

    private static class ViewHolder { // Объявляем статический вложенный класс ViewHolder
        ImageView avatar; // Поле для хранения ImageView аватара
        TextView name; // Поле для хранения TextView имени
        TextView age; // Поле для хранения TextView возраста
        TextView location; // Поле для хранения TextView местоположения
    }
}





