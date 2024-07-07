package com.example.a100_random_person;


import java.util.ArrayList; // Импортируем класс ArrayList для использования динамического массива
import java.util.List; // Импортируем интерфейс List для использования списка
import java.util.Random; // Импортируем класс Random для генерации случайных чисел

public class DataGenerator { // Объявляем класс DataGenerator для генерации данных пользователей
    // Массив идентификаторов ресурсов с аватарами
    private static final int[] AVATAR_IDS = {
            R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3, R.drawable.avatar4, R.drawable.avatar5
    };

    // Массив имен
    private static final String[] FIRST_NAMES = {
            "John", "Jane", "Alex", "Chris", "Katie", "Mike", "Sara", "Tom", "Anna", "James"
    };

    // Массив фамилий
    private static final String[] LAST_NAMES = {
            "Smith", "Doe", "Johnson", "Brown", "Davis", "Wilson", "Taylor", "Anderson", "Thomas", "Jackson"
    };

    // Массив стран
    private static final String[] COUNTRIES = {
            "USA", "Canada", "UK"
    };

    // Двумерный массив городов, соответствующих странам
    private static final String[][] CITIES = {
            {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix"}, // Города для USA
            {"Toronto", "Vancouver", "Montreal", "Calgary", "Ottawa"}, // Города для Canada
            {"London", "Birmingham", "Manchester", "Glasgow", "Liverpool"} // Города для UK
    };

    // Метод для генерации списка пользователей
    public static List<UserModel> generateUsers(int count) {
        List<UserModel> users = new ArrayList<>(); // Создаем новый список пользователей
        Random random = new Random(); // Создаем экземпляр Random для генерации случайных чисел

        // Цикл для генерации заданного количества пользователей
        for (int i = 0; i < count; i++) {
            int avatarId = AVATAR_IDS[random.nextInt(AVATAR_IDS.length)]; // Выбираем случайный аватар
            String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)]; // Выбираем случайное имя
            String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)]; // Выбираем случайную фамилию
            long age = 14 + random.nextInt(86); // Генерируем случайный возраст
            int countryIndex = random.nextInt(COUNTRIES.length); // Выбираем случайный индекс страны
            String country = COUNTRIES[countryIndex]; // Получаем страну по индексу
            String city = CITIES[countryIndex][random.nextInt(CITIES[countryIndex].length)]; // Получаем случайный город для выбранной страны

            // Добавляем нового пользователя в список
            users.add(new UserModel(avatarId, firstName, lastName, age, country, city));
        }

        return users; // Возвращаем список пользователей
    }
}