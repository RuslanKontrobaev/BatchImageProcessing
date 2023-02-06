package ru.istu.labapp;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    String url = "jdbc:mysql://localhost:3306/imagesdb";
    String username = "root";
    String password = "12345";

    public void cleanDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                Statement statement = connection.createStatement();
                statement.executeUpdate("TRUNCATE TABLE files");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (Exception ex) {
            System.out.printf("Ошибка подключения к базе данных %s!", url);
            System.out.println(ex);
        }
    }

    public void add(File file) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String query = "INSERT INTO files (File, FileName, FilePath) VALUES(?,?,?)";
                PreparedStatement pstmt = connection.prepareStatement(query);
                FileInputStream fileInputStream = new FileInputStream(file);
                pstmt.setBinaryStream(1, fileInputStream);
                pstmt.setString(2, file.getName());
                pstmt.setString(3, file.getPath());
                pstmt.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (Exception ex) {
            System.out.printf("Ошибка подключения к базе данных %s!", url);
            System.out.println(ex);
        }
    }

    public List<File> getAll(int id) {
        List<File> bufList = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                for (int i = 1; i <= id; i++) {
                    String query = "SELECT File, FilePath FROM files WHERE FileID=" + i;
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);

                    while (resultSet.next()) {
                        Blob file = resultSet.getBlob(1);
                        InputStream inputStream = file.getBinaryStream();
                        int size = inputStream.available();
                        byte b[] = new byte[size];
                        inputStream.read(b);
                        String str = resultSet.getString(2);

                        File ff = new File(str);
                        OutputStream fstream = new FileOutputStream(ff);
                        fstream.write(b);
                        bufList.add(ff);
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.out.printf("Ошибка подключения к1 базе данных %s!", url);
            System.out.println(ex);
        }
        return bufList;
    }

    public void updateDB(List<File> files, int maxId) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                for (int i = 1; i <= maxId; i++) {
                    String query = "UPDATE files SET File=? WHERE FileID=?";
                    try (FileInputStream fileInputStream = new FileInputStream(files.get(i-1))) {
                        PreparedStatement pstmt = connection.prepareStatement(query);
                        pstmt.setBinaryStream(1, fileInputStream);
                        pstmt.setInt(2, i);
                        pstmt.executeUpdate();
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (Exception ex) {
            System.out.printf("Ошибка подключения к базе данных %s!", url);
            System.out.println(ex);
        }
    }
}