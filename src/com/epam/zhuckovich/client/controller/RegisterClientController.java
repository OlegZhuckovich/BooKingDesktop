package com.epam.zhuckovich.client.controller;

import com.epam.zhuckovich.client.Client;
import com.epam.zhuckovich.entity.Gender;
import com.epam.zhuckovich.entity.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterClientController implements Initializable {

    @FXML private TextField nameRegisterField;
    @FXML private TextField surnameRegisterField;
    @FXML private TextField emailRegisterField;
    @FXML private PasswordField passwordRegisterField;
    @FXML private PasswordField rpPasswordRegisterField;
    @FXML private ComboBox<String> genderRegisterBox;
    @FXML private Button registerFormButton;

    @FXML
    void registerClientInDatabase(ActionEvent event) throws SQLException, IOException { //перенести на сервер
        Pattern emailRegex = Pattern.compile("[a-zA-Z0-9]+@[a-z]{2,10}\\.[a-z]{2,10}");
        Pattern nameSurnameRegex = Pattern.compile("[a-zA-Zа-яА-Я]+");

        Alert registerWarning = new Alert(Alert.AlertType.WARNING);
        registerWarning.setTitle("");
        registerWarning.setHeaderText("Необходимое поле не было заполнено");
        if(nameRegisterField.getText().trim().isEmpty()) {
            registerWarning.setContentText("Заполните своё имя");
            registerWarning.showAndWait();
        } else if (surnameRegisterField.getText().trim().isEmpty()){
            registerWarning.setContentText("Заполните свою фамилию");
            registerWarning.showAndWait();
        } else if (emailRegisterField.getText().trim().isEmpty()){
            registerWarning.setContentText("Заполните адрес электронной почты");
            registerWarning.showAndWait();
        } else if (passwordRegisterField.getText().trim().isEmpty()) {
            registerWarning.setContentText("Задайте пароль для аккаунта");
            registerWarning.showAndWait();
        } else if (rpPasswordRegisterField.getText().trim().isEmpty()) {
            registerWarning.setContentText("Повторите свой пароль для завершения регистрации");
            registerWarning.showAndWait();
        } else if (genderRegisterBox.getValue().isEmpty()) {
            registerWarning.setContentText("Выберите Ваш пол");
            registerWarning.showAndWait();
        } else if(!passwordRegisterField.getText().equals(rpPasswordRegisterField.getText())){
            Alert registerError = new Alert(Alert.AlertType.ERROR);
            registerError.setHeaderText("Ошибка регистрации");
            registerError.setContentText("Введённые Вами пароли не совпадают!");
            registerError.showAndWait();
        } else {

            boolean emailMatch;
            boolean nameMatch;
            boolean surnameMatch;

            Matcher emailMatcher = emailRegex.matcher(emailRegisterField.getText());
            emailMatch = emailMatcher.matches();

            Matcher nameMatcher = nameSurnameRegex.matcher(nameRegisterField.getText());
            nameMatch = nameMatcher.matches();

            Matcher surnameMatcher = nameSurnameRegex.matcher(surnameRegisterField.getText());
            surnameMatch = surnameMatcher.matches();

            if(!emailMatch){
                Alert registerError = new Alert(Alert.AlertType.ERROR);
                registerError.setHeaderText("Неправильный формат email");
                registerError.setContentText("Email может содержать только английские символы и обязательно дожен содержать символы @ и .");
                registerError.showAndWait();
            } else if(!nameMatch){
                Alert registerError = new Alert(Alert.AlertType.ERROR);
                registerError.setHeaderText("Неправильный формат имени");
                registerError.setContentText("Имя может содержать только кириллические и английские символы");
                registerError.showAndWait();
            } else if(!surnameMatch){
                Alert registerError = new Alert(Alert.AlertType.ERROR);
                registerError.setHeaderText("Неправильный формат фамилии");
                registerError.setContentText("Фамилия может содержать только кириллические и английские символы");
                registerError.showAndWait();
            } else {
                Date today = Calendar.getInstance().getTime();
                Gender gender;
                if (genderRegisterBox.getSelectionModel().getSelectedItem().equals(" Мужской")) {
                    gender = Gender.MAN;
                } else {
                    gender = Gender.WOMAN;
                }

                //////////////////////////////////////////

                Client.getClientWriter().writeInt(2);
                Client.getClientWriter().flush();

                Client.getClientWriter().writeObject(emailRegisterField.getText());
                Client.getClientWriter().flush();

                Client.getClientWriter().writeObject(new Person(nameRegisterField.getText(), surnameRegisterField.getText(), emailRegisterField.getText(), passwordRegisterField.getText(), today, gender, false));
                Client.getClientWriter().flush();

                boolean registerFlag = Client.getClientReader().readBoolean();

                /////////////////////////////////////////

                if (registerFlag) {
                    Alert registerError = new Alert(Alert.AlertType.INFORMATION);
                    registerError.setHeaderText("Регистрация");
                    registerError.setContentText("Пользователь " + nameRegisterField.getText() + " " + surnameRegisterField.getText() + " успешно зарегистрирован!");
                    registerError.showAndWait();
                } else {
                    Alert registerError = new Alert(Alert.AlertType.ERROR);
                    registerError.setHeaderText("Ошибка регистрации");
                    registerError.setContentText("Пользователь с таким email уже зарегистрирован");
                    registerError.showAndWait();
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> genderArrayList = new ArrayList<String>();
        genderArrayList.add(" Мужской");
        genderArrayList.add(" Женский");
        ObservableList<String> genderList = FXCollections.observableList(genderArrayList);
        genderRegisterBox.setItems(genderList);
    }
}
