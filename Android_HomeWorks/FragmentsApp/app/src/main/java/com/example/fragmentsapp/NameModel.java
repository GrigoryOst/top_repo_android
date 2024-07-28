package com.example.fragmentsapp;

// Модель данных для хранения информации об имени
public class NameModel {

    // Поля для хранения информации об имени
    private String name; // Имя
    private String meaning; // Значение имени
    private String dates; // Даты именин

    // Конструктор для инициализации полей
    public NameModel(String name, String meaning, String dates) {
        this.name = name; // Инициализация поля name значением, переданным в конструктор
        this.meaning = meaning; // Инициализация поля meaning значением, переданным в конструктор
        this.dates = dates; // Инициализация поля dates значением, переданным в конструктор
    }

    // Геттеры для доступа к полям
    public String getName() {
        return name; // Возвращает значение поля name
    }

    public String getMeaning() {
        return meaning; // Возвращает значение поля meaning
    }

    public String getDates() {
        return dates; // Возвращает значение поля dates
    }
}