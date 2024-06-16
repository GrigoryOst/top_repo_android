package com.example.my_second_application;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

//крестики нолики
public class MainActivity extends AppCompatActivity {

    // Объявление переменных для отслеживания состояния игры
    private boolean playerXTurn = true; // true - ход игрока X, false - ход игрока O
    private Button[][] buttons = new Button[3][3]; // Сетка 3x3 для кнопок
    private TextView currentPlayerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация TextView
        currentPlayerTextView = findViewById(R.id.currentPlayerTextView);

        // Инициализация кнопок
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
            }
        }
    }

    // Метод, вызываемый при нажатии на любую ячейку сетки
    public void onCellClicked(View view) {
        Button clickedButton = (Button) view;
        if (!clickedButton.getText().toString().equals("")) {
            // Если кнопка уже нажата, ничего не делать
            return;
        }

        // Установка текста кнопки в зависимости от текущего игрока
        if (playerXTurn) {
            clickedButton.setText("X");
        } else {
            clickedButton.setText("O");
        }

        // Проверка на победу или ничью
        if (checkForWin()) {
            if (playerXTurn) {
                showToast("Player X wins!");
            } else {
                showToast("Player O wins!");
            }
            disableButtons();
        } else if (isBoardFull()) {
            showToast("It's a draw!");
        } else {
            // Переход хода к другому игроку
            playerXTurn = !playerXTurn;
            updateCurrentPlayerTextView();
        }
    }

    // Метод для обновления TextView, отображающего текущего игрока
    private void updateCurrentPlayerTextView() {
        if (playerXTurn) {
            currentPlayerTextView.setText("Player X's turn");
        } else {
            currentPlayerTextView.setText("Player O's turn");
        }
    }

    // Метод для проверки победы
    private boolean checkForWin() {
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        // Проверка строк, столбцов и диагоналей
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("")) {
                return true;
            }
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("")) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals("")) {
            return true;
        }
        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals("")) {
            return true;
        }

        return false;
    }

    // Метод для проверки, заполнена ли вся доска
    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().toString().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    // Метод для отображения тоста
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Метод для отключения всех кнопок после победы
    private void disableButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    // Метод для сброса игры
    public void resetGame(View view) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
        playerXTurn = true;
        updateCurrentPlayerTextView();
    }
}