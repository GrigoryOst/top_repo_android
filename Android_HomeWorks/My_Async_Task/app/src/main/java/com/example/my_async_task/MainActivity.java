package com.example.my_async_task;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar; // Индикатор прогресса
    private TextView taskStatus; // Поле для отображения статуса задачи
    private Button startButton; // Кнопка для запуска задачи
    private Button cancelButton; // Кнопка для отмены задачи
    private MyAsyncTask myAsyncTask; // Экземпляр класса MyAsyncTask

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация элементов интерфейса
        progressBar = findViewById(R.id.progress_bar);
        taskStatus = findViewById(R.id.task_status);
        startButton = findViewById(R.id.start_button);
        cancelButton = findViewById(R.id.cancel_button);

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
