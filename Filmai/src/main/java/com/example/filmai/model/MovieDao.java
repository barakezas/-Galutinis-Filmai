package com.example.filmai.model;

import com.example.filmai.controller.DashboardController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDao {

    //---------------------Kuriame įrašą---------------------
    public static void create(Movie movie) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/db";
        String querry = "INSERT INTO `filmai`(`title`, `length_seconds`, `actors`, `director`, `year`, `user_id`) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, "root", "");

            PreparedStatement preparedStatement = connection.prepareStatement(querry);
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setInt(2, movie.getLength_seconds());
            preparedStatement.setString(3, movie.getActors());
            preparedStatement.setString(4, movie.getDirector());
            preparedStatement.setInt(5, movie.getYear());
            preparedStatement.setInt(6, movie.getUser_id());


            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //---------------------Įrašo paieška pagal name---------------------
    public static List<Movie> searchByName(String name, String role) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/db";
        String querry = "";

        // Administratorius iesko tarp visu irasu
        if (role.equals("ADMINISTRATORIUS")) {
            if (name.isEmpty()) {
                querry = "SELECT * FROM `filmai`";
            } else {
                querry = "SELECT * FROM `filmai` WHERE `title` LIKE '%" + name + "%'";
            }
        } else {
            // Vartotojas iesko tik tarp savo irasu
            int userID = UserDao.searchByUsernameReturnID(UserSingleton.getInstance().getUserName());
            if (name.isEmpty()) {
                querry = "SELECT * FROM `filmai` WHERE `user_id` = " + userID;
            } else {
                querry = "SELECT * FROM `filmai` WHERE `title` LIKE '%" + name + "%'" + " AND `user_id` = " + userID;
            }
        }

        ArrayList<Movie> list = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(querry);
//            if(!name.isEmpty()){
//                preparedStatement.setString(1, name);
//            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) { //Kol turime sarase elementus
                list.add(new Movie(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getInt("length_seconds"),
                        resultSet.getString("actors"),
                        resultSet.getString("director"),
                        resultSet.getInt("year"),
                        resultSet.getInt("user_id")
                ));
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    //---------------------Atnaujiname įrašą---------------------
    public static void update(Movie movie) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/db";
        String update = "UPDATE `filmai` SET `title`= ?,`length_seconds`= ?,`actors`= ?,`director`= ?,`year`= ? WHERE `id` = ?";
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setInt(2, movie.getLength_seconds());
            preparedStatement.setString(3, movie.getActors());
            preparedStatement.setString(4, movie.getDirector());
            preparedStatement.setInt(5, movie.getYear());
            preparedStatement.setInt(6, movie.getId());

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

            System.out.println("Pavyko atnaujinti įrašą");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Nepavyko atnaujinti įrašą");
        }
    }

    //---------------------Triname įrašą---------------------
    public static void deleteById(int id) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/db";
        String delete = "DELETE FROM filmai WHERE id = ?";

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, "root", "");

            PreparedStatement preparedStatement = connection.prepareStatement(delete);
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

            System.out.println("Pavyko ištrinti įrašą");
        } catch (SQLException e) {
            e.printStackTrace();

            System.out.println("Įrašo ištrinti nepavyko");
        }
    }

}
