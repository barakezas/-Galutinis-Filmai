package com.example.filmai.controller;

import com.example.filmai.MainApplication;
import com.example.filmai.model.Movie;
import com.example.filmai.model.MovieDao;
import com.example.filmai.model.UserDao;
import com.example.filmai.model.UserSingleton;
import com.example.filmai.utils.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private Label status;
    @FXML
    private TableView filmsTableView;
    @FXML
    private TextField idField;

    // Lentelės stulpeliai
    @FXML
    private TableColumn idColumn;
    @FXML
    private TableColumn titleColumn;
    @FXML
    private TableColumn secondsColumn;
    @FXML
    private TableColumn actorsColumn;
    @FXML
    private TableColumn directorColumn;
    @FXML
    private TableColumn yearColumn, userIdCollumn;

    // Formos elementai
    @FXML
    private TextField titleField;
    @FXML
    private TextField secondsField;

    //
    @FXML
    private CheckBox checkBoxJohn;
    @FXML
    private CheckBox checkBoxAndrew;
    @FXML
    private CheckBox checkBoxPete;

    //
    @FXML
    private RadioButton radioButtonJohn;
    @FXML
    private RadioButton radioButtonAndrew;
    @FXML
    private RadioButton radioButtonPete;

    //
    @FXML
    private ChoiceBox choiceBoxYear;

    @FXML
    private Label groupLabel, usernameLabel;

    @FXML
    private Button logOutButton;




    ObservableList<Movie> list = FXCollections.observableArrayList();

    @FXML
    public void searchButtonClick() {
        list.clear();
        String titleField2 = titleField.getText();

        List<Movie> movieList = MovieDao.searchByName(titleField2, groupLabel.getText());

        for (Movie movie : movieList) {
            list.add(new Movie(movie.getId(), movie.getTitle(), movie.getLength_seconds(), movie.getActors(), movie.getDirector(), movie.getYear(), movie.getUser_id()));

            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            secondsColumn.setCellValueFactory(new PropertyValueFactory<>("length_seconds"));
            actorsColumn.setCellValueFactory(new PropertyValueFactory<>("actors"));
            directorColumn.setCellValueFactory(new PropertyValueFactory<>("director"));
            yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
            userIdCollumn.setCellValueFactory(new PropertyValueFactory<>("user_id"));


            filmsTableView.setItems(list);
        }
        if (movieList.isEmpty()) {
            status.setText("Nepavyko atlikti paieška pagal filmų pavadinimą");
        } else {
            status.setText("Pavyko atlikti paieška pagal filmų pavadinimą");
        }
    }

    @FXML
    public void onCreateButtonClick() {
        String titleField2 = titleField.getText();
        String secondsField2 = secondsField.getText();          // Paliekam String tipa kad veiktu validacija

        String actors = "";
        if (checkBoxJohn.isSelected()) {
            actors += checkBoxJohn.getText() + ",";
        }
        if (checkBoxAndrew.isSelected()) {
            actors += checkBoxAndrew.getText() + ",";
        }
        if (checkBoxPete.isSelected()) {
            actors += checkBoxPete.getText() + ",";
        }

        String director = "";
        if (radioButtonJohn.isSelected()) {
            director = radioButtonJohn.getText();
        } else if (radioButtonAndrew.isSelected()) {
            director = radioButtonAndrew.getText();
        } else if (radioButtonPete.isSelected()) {
            director = radioButtonPete.getText();
        }

        String year = "";
        if (!choiceBoxYear.getSelectionModel().isEmpty()) {
            year = choiceBoxYear.getSelectionModel().getSelectedItem().toString();
        }

        // Tikriname pagal Validacijas
        if (!Validation.isValidTitle(titleField2)) {
            status.setText("Neteisingai įvedėt pavadinimą");
        } else if (!Validation.isValidTime(secondsField2)) {
            status.setText("Neteisingai įvedėt filmo trukmę");
        } else if (actors.isEmpty()) {
            status.setText("Prašome pasirinkti aktorius");
        } else {
            // keiciame kintamuju tipus pagal konstruktoriu
            int year2 = Integer.parseInt(year);
            int secondsField3 = Integer.parseInt(secondsField.getText());

            // Kuriame įrašą DB
            int userID = UserDao.searchByUsernameReturnID(usernameLabel.getText());

            Movie movies = new Movie(titleField2, secondsField3, actors, director, year2, userID);
            MovieDao.create(movies);
            status.setText("Pavyko sukurti įrašą");
        }
    }

    @FXML
    public void onUpdateButtonClick() {
        String titleField2 = titleField.getText();
        String secondsField2 = secondsField.getText();          // Paliekam String tipa kad veiktu validacija
        String idField2 = idField.getText();

        if(groupLabel.getText().equals("ADMINISTRATORIUS")){
            String actors = "";
            if (checkBoxJohn.isSelected()) {
                actors += checkBoxJohn.getText() + ",";
            }
            if (checkBoxAndrew.isSelected()) {
                actors += checkBoxAndrew.getText() + ",";
            }
            if (checkBoxPete.isSelected()) {
                actors += checkBoxPete.getText() + ",";
            }

            String director = "";
            if (radioButtonJohn.isSelected()) {
                director = radioButtonJohn.getText();
            } else if (radioButtonAndrew.isSelected()) {
                director = radioButtonAndrew.getText();
            } else if (radioButtonPete.isSelected()) {
                director = radioButtonPete.getText();
            }

            String year = "";
            if (!choiceBoxYear.getSelectionModel().isEmpty()) {
                year = choiceBoxYear.getSelectionModel().getSelectedItem().toString();
            }

            // Tikriname pagal Validacijas
            if (!Validation.isValidTime(idField2)) {
                status.setText("Neteisingai įvestas ID");
            } else if (!Validation.isValidTitle(titleField2)) {
                status.setText("Neteisingai įvedėte pavadinimą");
            } else if (!Validation.isValidTime(secondsField2)) {
                status.setText("Neteisingai įvedėte filmo trukmę");
            } else if (actors.isEmpty()) {
                status.setText("Prašome pasirinkti aktorius");
            } else {
                // keiciame kintamuju tipus pagal konstruktoriu
                int year2 = Integer.parseInt(year);
                int secondsField3 = Integer.parseInt(secondsField.getText());
                int idField3 = Integer.parseInt(idField.getText());

                // Kuriame įrašą DB


                Movie movie = new Movie(idField3, titleField2, secondsField3, actors, director, year2);
                MovieDao.update(movie);
                status.setText("Pavyko paredaguoti įrašą");
            }
        } else{
            // Vartotojas
            status.setText("Redaguoti įraša gali tik administratorius. SORIUKAS");
        }


    }

    @FXML
    public void onDeleteButtonClick() {
        String idField2 = idField.getText();

        if(groupLabel.getText().equals("ADMINISTRATORIUS")) {
            if (!Validation.isValidTime(idField2)) {
                status.setText("Neteisingai įvestas ID");
            } else {
                int idField3 = Integer.parseInt(idField.getText());
                MovieDao.deleteById(idField3);
                status.setText("Pavyko sėkmingai ištrinti įrašą");
            }
        } else{
            // Vartotojas
            status.setText("Trinti įraša gali tik administratorius. SORIUKAS");
        }
    }

    @FXML
    public void onLogOutButtonClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(MainApplication.class.getResource("login-view.fxml"));
        Stage LoginStage = new Stage();
        LoginStage.setTitle("Prisijungimo langas");
        LoginStage.setScene(new Scene(root, 600, 400));
        LoginStage.show();
        ((Node) event.getSource()).getScene().getWindow().hide();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Pridedame reikšmes į ChoiceBox
        choiceBoxYear.getItems().add("1998");
        choiceBoxYear.getItems().add("1999");
        choiceBoxYear.getItems().add("2000");
        choiceBoxYear.getItems().add("2001");
        choiceBoxYear.getItems().add("2002");
        choiceBoxYear.getItems().add("2003");
        choiceBoxYear.getItems().add("2004");
        choiceBoxYear.getItems().add("2005");

        // Pažymėtos reikšmės
        checkBoxJohn.setSelected(true);
        radioButtonJohn.setSelected(true);
        choiceBoxYear.getSelectionModel().selectFirst();

        // Parodomas prisijunges vartotojas
        String username = UserSingleton.getInstance().getUserName();
        usernameLabel.setText(username);

        // Parodoma prisijungusio vartotojo role(grupe)
        String role = UserDao.searchByUsername(username);
        groupLabel.setText(role);

    }
}
