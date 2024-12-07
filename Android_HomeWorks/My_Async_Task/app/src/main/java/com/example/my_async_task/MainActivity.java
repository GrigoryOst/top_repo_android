package com.example.my_async_task;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar; // Индикатор прогресса
    private TextView taskStatus; // Поле для отображения статуса задачи
    private Button startButton; // Кнопка для запуска задачи
    private Button cancelButton; // Кнопка для отмены задачи
    private MyAsyncTask myAsyncTask; // Экземпляр класса MyAsyncTask

    private FirebaseAuth firebaseAuth; // Экземпляр Firebase Authentication
    private EditText emailEditText, passwordEditText; // Поля для ввода email и пароля
    private Button signUpButton, signInButton, signOutButton; // Кнопки для регистрации, входа и выхода



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Инициализация UI элементов
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        signUpButton = findViewById(R.id.sign_up_button);
        signInButton = findViewById(R.id.sign_in_button);
        signOutButton = findViewById(R.id.sign_out_button);

        // Инициализация элементов интерфейса
        progressBar = findViewById(R.id.progress_bar);
        taskStatus = findViewById(R.id.task_status);
        startButton = findViewById(R.id.start_button);
        cancelButton = findViewById(R.id.cancel_button);

        // Регистрация пользователя
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                if (!email.isEmpty() && !password.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Ошибка регистрации: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(MainActivity.this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Вход пользователя
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                if (!email.isEmpty() && !password.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Вход выполнен!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Ошибка входа: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(MainActivity.this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Выход пользователя
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Toast.makeText(MainActivity.this, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();
            }
        });

        // Установка слушателей для кнопок
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создание и запуск задачи
                myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute(100); // 100 итераций по умолчанию
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Отмена задачи
                if (myAsyncTask != null) {
                    myAsyncTask.cancel(true);
                }
            }
        });
    }

    // Внутренний класс для реализации AsyncTask
    private class MyAsyncTask extends AsyncTask<Integer, Integer, Void> {

        // Метод, выполняемый до начала задачи (на UI потоке)
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Обновление статуса задачи
            taskStatus.setText("Status: Running");
        }

        // Основной метод, выполняющийся в фоновом потоке
        @Override
        protected Void doInBackground(Integer... params) {
            int iterations = params[0]; // Получение числа итераций
            for (int i = 0; i < iterations; i++) {
                if (isCancelled()) { // Проверка, отменена ли задача
                    break;
                }
                try {
                    // Эмуляция тяжелой задачи задержкой
                    Thread.sleep(300); // Задержка 300 мс
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Публикация прогресса
                publishProgress((i * 100) / iterations);
            }
            return null;
        }

        // Метод, выполняемый при обновлении прогресса (на UI потоке)
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // Обновление прогресса в индикаторе прогресса
            progressBar.setProgress(values[0]);
        }

        // Метод, выполняемый после завершения задачи (на UI потоке)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Обновление статуса задачи
            taskStatus.setText("Status: Finished");
        }

        // Метод, выполняемый при отмене задачи (на UI потоке)
        @Override
        protected void onCancelled() {
            super.onCancelled();
            // Обновление статуса задачи
            taskStatus.setText("Status: Cancelled");
        }
    }
}
