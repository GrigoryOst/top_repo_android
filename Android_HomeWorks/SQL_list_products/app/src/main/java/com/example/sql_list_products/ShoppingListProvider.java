package com.example.sql_list_products;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.net.Uri;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;


public class ShoppingListProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.shoppinglistprovider"; // Строка, определяющая уникальный идентификатор контент-провайдера
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH); // UriMatcher используется для сопоставления URI с определенными кодами

    static {
        uriMatcher.addURI(AUTHORITY, "lists", 1); // Добавляем правило для обработки URI типа "lists", сопоставляется с кодом 1
        uriMatcher.addURI(AUTHORITY, "lists/#", 2); // Добавляем правило для обработки URI типа "lists/#", где # — идентификатор списка, сопоставляется с кодом 2
        uriMatcher.addURI(AUTHORITY, "type", 3); // Добавляем правило для обработки URI типа "type", сопоставляется с кодом 3
        uriMatcher.addURI(AUTHORITY, "type/#", 4); // Добавляем правило для обработки URI типа "type/#", где # — идентификатор типа, сопоставляется с кодом 4
        uriMatcher.addURI(AUTHORITY, "product", 5); // Добавляем правило для обработки URI типа "product", сопоставляется с кодом 5
        uriMatcher.addURI(AUTHORITY, "product/#", 6); // Добавляем правило для обработки URI типа "product/#", где # — идентификатор продукта, сопоставляется с кодом 6
    }

    private Connection connection; // Переменная для хранения соединения с базой данных

    @Override
    public boolean onCreate() {
        // Создаем подключение к SQL Server через JDBC
        try {
            connection = DatabaseHelper.getConnection(getContext()); // Предполагается, что DatabaseHelper возвращает Connection объект
        } catch (SQLException e) {
            e.printStackTrace(); // Выводим стек вызовов при возникновении ошибки SQL
            return false; // Возвращаем false, если база данных не была успешно открыта
        } catch (java.sql.SQLException e) {
            throw new RuntimeException(e); // Бросаем исключение, если возникло другое SQLException
        }
        return connection != null; // Возвращаем true, если подключение установлено
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id; // Переменная для хранения ID вставленной записи
        String sql = ""; // Переменная для хранения SQL-запроса
        switch (uriMatcher.match(uri)) { // Определяем действие на основе URI
            case 1:
                sql = "INSERT INTO Lists (name, date, description) VALUES (?, ?, ?)"; // SQL-запрос для вставки в таблицу Lists
                break;
            case 3:
                sql = "INSERT INTO Type (label, rule) VALUES (?, ?)"; // SQL-запрос для вставки в таблицу Type
                break;
            case 5:
                sql = "INSERT INTO Product (name, count, list_id, checked, count_type) VALUES (?, ?, ?, ?, ?)"; // SQL-запрос для вставки в таблицу Product
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri); // Бросаем исключение, если URI не поддерживается
        }
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // Пример заполнения параметров на основе переданных ContentValues
            switch (uriMatcher.match(uri)) { // Определяем, какие параметры устанавливать
                case 1: // Вставка в таблицу Lists
                    stmt.setString(1, values.getAsString("name")); // Устанавливаем значение для name
                    stmt.setInt(2, values.getAsInteger("date")); // Устанавливаем значение для date
                    stmt.setString(3, values.getAsString("description")); // Устанавливаем значение для description
                    break;
                case 3: // Вставка в таблицу Type
                    stmt.setString(1, values.getAsString("label")); // Устанавливаем значение для label
                    stmt.setString(2, values.getAsString("rule")); // Устанавливаем значение для rule
                    break;
                case 5: // Вставка в таблицу Product
                    stmt.setString(1, values.getAsString("name")); // Устанавливаем значение для name
                    stmt.setDouble(2, values.getAsDouble("count")); // Устанавливаем значение для count
                    stmt.setInt(3, values.getAsInteger("list_id")); // Устанавливаем значение для list_id
                    stmt.setInt(4, values.getAsInteger("checked")); // Устанавливаем значение для checked
                    stmt.setInt(5, values.getAsInteger("count_type")); // Устанавливаем значение для count_type
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported URI: " + uri); // Бросаем исключение, если URI не поддерживается
            }

            // Выполняем SQL-запрос на вставку
            stmt.executeUpdate();

            // Получаем сгенерированный ID
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) { // Проверяем, удалось ли получить сгенерированный ID
                    id = rs.getLong(1); // Сохраняем сгенерированный ID
                } else {
                    throw new SQLException("Insert failed, no ID obtained."); // Бросаем исключение, если не удалось получить ID
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Выводим стек вызовов при возникновении ошибки SQL
            throw new SQLException("Insert failed: " + e.getMessage()); // Бросаем новое исключение с сообщением об ошибке
        } catch (java.sql.SQLException e) {
            throw new RuntimeException(e); // Бросаем исключение, если возникло другое SQLException
        }
        return ContentUris.withAppendedId(uri, id); // Возвращаем URI с добавленным ID вставленной записи
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count; // Переменная для хранения количества удаленных строк
        String sql = ""; // Переменная для хранения SQL-запроса
        switch (uriMatcher.match(uri)) { // Определяем действие на основе URI
            case 2:
                sql = "DELETE FROM Lists WHERE _id = ?"; // SQL-запрос для удаления из таблицы Lists по _id
                break;
            case 4:
                sql = "DELETE FROM Type WHERE _id = ?"; // SQL-запрос для удаления из таблицы Type по _id
                break;
            case 6:
                sql = "DELETE FROM Product WHERE _id = ?"; // SQL-запрос для удаления из таблицы Product по _id
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri); // Бросаем исключение, если URI не поддерживается
        }
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uri.getLastPathSegment()); // Устанавливаем значение _id для удаления
            count = stmt.executeUpdate(); // Выполняем SQL-запрос на удаление
        } catch (SQLException e) {
            e.printStackTrace(); // Выводим стек вызовов при возникновении ошибки SQL
            throw new SQLException("Delete failed: " + e.getMessage()); // Бросаем новое исключение с сообщением об ошибке
        } catch (java.sql.SQLException e) {
            throw new RuntimeException(e); // Бросаем исключение, если возникло другое SQLException
        }
        return count; // Возвращаем количество удаленных строк
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count; // Переменная для хранения количества обновленных строк
        String sql = ""; // Переменная для хранения SQL-запроса
        switch (uriMatcher.match(uri)) { // Определяем действие на основе URI
            case 2:
                sql = "UPDATE Lists SET name = ?, date = ?, description = ? WHERE _id = ?"; // SQL-запрос для обновления таблицы Lists по _id
                break;
            case 4:
                sql = "UPDATE Type SET label = ?, [rule] = ? WHERE _id = ?"; // SQL-запрос для обновления таблицы Type по _id
                break;
            case 6:
                sql = "UPDATE Product SET name = ?, count = ?, list_id = ?, checked = ?, count_type = ? WHERE _id = ?"; // SQL-запрос для обновления таблицы Product по _id
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri); // Бросаем исключение, если URI не поддерживается
        }
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Установка параметров в PreparedStatement на основе переданных ContentValues
            stmt.setString(1, uri.getLastPathSegment()); // Устанавливаем значение для _id
            count = stmt.executeUpdate(); // Выполняем SQL-запрос на обновление
        } catch (SQLException e) {
            e.printStackTrace(); // Выводим стек вызовов при возникновении ошибки SQL
            throw new SQLException("Update failed: " + e.getMessage()); // Бросаем новое исключение с сообщением об ошибке
        } catch (java.sql.SQLException e) {
            throw new RuntimeException(e); // Бросаем исключение, если возникло другое SQLException
        }
        return count; // Возвращаем количество обновленных строк
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String sql = ""; // Переменная для хранения SQL-запроса
        switch (uriMatcher.match(uri)) { // Определяем действие на основе URI
            case 1:
                sql = "SELECT * FROM Lists"; // SQL-запрос для выборки всех данных из таблицы Lists
                break;
            case 3:
                sql = "SELECT * FROM Type"; // SQL-запрос для выборки всех данных из таблицы Type
                break;
            case 5:
                sql = "SELECT * FROM Product"; // SQL-запрос для выборки всех данных из таблицы Product
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri); // Бросаем исключение, если URI не поддерживается
        }

        // Здесь мы будем использовать JDBC для выполнения SQL-запроса
        try (Connection connection = DriverManager.getConnection("jdbc:sqlserver://your_server;databaseName=your_database;user=your_username;password=your_password");
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Применить фильтры (selection и selectionArgs) если они присутствуют
            if (selection != null && selectionArgs != null) {
                stmt.setString(1, selectionArgs[0]); // Устанавливаем значение для фильтрации
                // Дополнительные условия могут быть добавлены с аналогичной логикой
            }

            ResultSet resultSet = stmt.executeQuery(); // Выполняем SQL-запрос на выборку

            // Создаем MatrixCursor для хранения данных
            MatrixCursor cursor = new MatrixCursor(projection != null ? projection : getAllColumns(resultSet));

            // Перебираем все строки из ResultSet и добавляем их в Cursor
            while (resultSet.next()) { // Переходим к следующей строке результата
                Object[] row = new Object[cursor.getColumnCount()]; // Создаем массив для хранения данных строки
                for (int i = 0; i < cursor.getColumnCount(); i++) { // Перебираем все колонки
                    row[i] = resultSet.getObject(cursor.getColumnName(i)); // Получаем значение колонки и сохраняем в массив
                }
                cursor.addRow(row); // Добавляем строку в курсор
            }

            return cursor; // Возвращаем курсор с данными

        } catch (SQLException e) {
            e.printStackTrace(); // Выводим стек вызовов при возникновении ошибки SQL
            throw new RuntimeException("Query failed: " + e.getMessage()); // Бросаем новое исключение с сообщением об ошибке
        } catch (java.sql.SQLException e) {
            throw new RuntimeException(e); // Бросаем исключение, если возникло другое SQLException
        }
    }

    // Метод для получения всех колонок из ResultSet если projection не задано
    private String[] getAllColumns(ResultSet resultSet) throws SQLException, java.sql.SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData(); // Получаем метаданные ResultSet
        String[] columns = new String[metaData.getColumnCount()]; // Создаем массив для хранения имен колонок
        for (int i = 0; i < metaData.getColumnCount(); i++) { // Перебираем все колонки
            columns[i] = metaData.getColumnName(i + 1); // Получаем имя колонки и сохраняем в массив
        }
        return columns; // Возвращаем массив имен колонок
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) { // Определяем тип данных на основе URI
            case 1:
                return "vnd.android.cursor.dir/vnd." + AUTHORITY + ".lists"; // Возвращаем тип данных для списка Lists
            case 2:
                return "vnd.android.cursor.item/vnd." + AUTHORITY + ".lists"; // Возвращаем тип данных для элемента Lists
            case 3:
                return "vnd.android.cursor.dir/vnd." + AUTHORITY + ".type"; // Возвращаем тип данных для списка Type
            case 4:
                return "vnd.android.cursor.item/vnd." + AUTHORITY + ".type"; // Возвращаем тип данных для элемента Type
            case 5:
                return "vnd.android.cursor.dir/vnd." + AUTHORITY + ".product"; // Возвращаем тип данных для списка Product
            case 6:
                return "vnd.android.cursor.item/vnd." + AUTHORITY + ".product"; // Возвращаем тип данных для элемента Product
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri); // Бросаем исключение, если URI не поддерживается
        }
    }

    @Override
    public void shutdown() {
        DatabaseHelper.closeConnection(); // Закрываем соединение с базой данных
        super.shutdown(); // Вызываем метод shutdown родительского класса
    }
}
