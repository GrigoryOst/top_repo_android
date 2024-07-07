package com.example.a100_random_person;

public class UserModel { // Объявляем класс UserModel, который представляет модель данных пользователя
    private int avatarId; // Поле для хранения идентификатора ресурса с аватаром
    private String firstName; // Поле для хранения имени пользователя
    private String lastName; // Поле для хранения фамилии пользователя
    private long age; // Поле для хранения возраста пользователя в формате long UTC
    private String country; // Поле для хранения страны пользователя
    private String city; // Поле для хранения города пользователя

    // Конструктор класса, принимающий все поля в качестве параметров
    public UserModel(int avatarId, String firstName, String lastName, long age, String country, String city) {
        this.avatarId = avatarId; // Инициализация поля avatarId
        this.firstName = firstName; // Инициализация поля firstName
        this.lastName = lastName; // Инициализация поля lastName
        this.age = age; // Инициализация поля age
        this.country = country; // Инициализация поля country
        this.city = city; // Инициализация поля city
    }

    // Метод для получения идентификатора аватара
    public int getAvatarId() {
        return avatarId;
    }

    // Метод для получения имени пользователя
    public String getFirstName() {
        return firstName;
    }

    // Метод для получения фамилии пользователя
    public String getLastName() {
        return lastName;
    }

    // Метод для получения возраста пользователя
    public long getAge() {
        return age;
    }

    // Метод для получения страны пользователя
    public String getCountry() {
        return country;
    }

    // Метод для получения города пользователя
    public String getCity() {
        return city;
    }
}