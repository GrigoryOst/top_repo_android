package com.example.calculator_android;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Поле для отображения чисел и результата
    private TextView display;
    // Текущее вводимое число
    private String currentInput = "";
    // Предыдущее число перед оператором
    private String previousInput = "";
    // Текущий оператор (+, -, *, /)
    private String operator = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация TextView для отображения
        display = findViewById(R.id.display);

        // Установка кнопок с числами
        setNumberButtonListeners();
        // Установка кнопок с операторами
        setOperatorButtonListeners();
    }

    // Метод для установки кнопки с числами
    private void setNumberButtonListeners() {
        // Массив ID кнопок с числами и десятичной точкой
        int[] numberButtonIds = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3,
                R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7,
                R.id.btn_8, R.id.btn_9, R.id.btn_decimal
        };

        // Создание числовых кнопок
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получение текста кнопки и добавление его к текущему вводу
                Button button = (Button) v;
                currentInput += button.getText().toString();
                // Обновление текста на дисплее
                display.setText(currentInput);
            }
        };

        // Назначение кнопкам с числами
        for (int id : numberButtonIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    // Метод для установки кнопки с операторами
    private void setOperatorButtonListeners() {
        // Массив ID кнопок с операторами и кнопкой сброса
        int[] operatorButtonIds = {
                R.id.btn_add, R.id.btn_subtract, R.id.btn_multiply, R.id.btn_divide, R.id.btn_equals, R.id.btn_clear
        };

        // Создание операторных кнопок
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получение текста кнопки
                Button button = (Button) v;
                String symbol = button.getText().toString();

                // Выполнение действия в зависимости от нажатого символа
                if (symbol.equals("C")) {
                    // Сброс всех значений
                    clear();
                } else if (symbol.equals("=")) {
                    // Вычисление результата
                    calculateResult();
                } else {
                    // Установка оператора
                    setOperator(symbol);
                }
            }
        };

        // Назначение операторным кнопкам
        for (int id : operatorButtonIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    // Метод для сброса всех значений
    private void clear() {
        currentInput = "";
        previousInput = "";
        operator = "";
        display.setText("0");
    }

    // Метод для установки оператора
    private void setOperator(String symbol) {
        // Проверка, что есть текущий ввод
        if (!currentInput.isEmpty()) {
            // Если уже есть оператор, сначала вычисляем результат
            if (!operator.isEmpty()) {
                calculateResult();
            }
            // Установка нового оператора и сохранение текущего ввода как предыдущего
            operator = symbol;
            previousInput = currentInput;
            currentInput = "";
        }
    }

    // Метод для вычисления результата
    private void calculateResult() {
        // Проверка, что введены все необходимые данные
        if (operator.isEmpty() || currentInput.isEmpty()) {
            return;
        }

        // Переменные для результата и операндов
        double result = 0.0;
        double firstOperand = Double.parseDouble(previousInput);
        double secondOperand = Double.parseDouble(currentInput);

        // Выполнение операции в зависимости от оператора
        switch (operator) {
            case "+":
                result = firstOperand + secondOperand;
                break;
            case "-":
                result = firstOperand - secondOperand;
                break;
            case "*":
                result = firstOperand * secondOperand;
                break;
            case "/":
                if (secondOperand != 0) {
                    result = firstOperand / secondOperand;
                } else {
                    // Если деление на ноль, показать ошибку и выйти
                    display.setText("Error");
                    return;
                }
                break;
        }

        // Обновление дисплея и переменных
        display.setText(String.valueOf(result));
        previousInput = String.valueOf(result);
        currentInput = "";
        operator = "";
    }
}
