package com.epam.zhuckovich.client.controller.admincontroller;

import com.epam.zhuckovich.client.Client;
import com.epam.zhuckovich.client.action.SceneAnimation;
import com.epam.zhuckovich.entity.Gender;
import com.epam.zhuckovich.entity.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterLibrarianController implements Initializable{

    @FXML private AnchorPane rootPane;
    @FXML private Button RegisterButton;
    @FXML private Button MainMenuButton;
    @FXML private TextField nameField;
    @FXML private TextField surnameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> GenderComboBox;
    @FXML private PasswordField repeatPasswordField;

    @FXML
    void goToMainMenu(ActionEvent event) {
        SceneAnimation.ChangeScene(rootPane,"../scenes/LibrarianMenu.fxml");
    }

    @FXML
    void registerLibrarian(ActionEvent event) throws SQLException, IOException {
        Pattern emailRegex = Pattern.compile("[a-zA-Z0-9]+@[a-z]{2,10}\\.[a-z]{2,10}");
        Pattern nameSurnameRegex = Pattern.compile("[a-zA-Zа-яА-Я]+");
        boolean emailMatch;
        boolean nameMatch;
        boolean surnameMatch;
        Matcher emailMatcher = emailRegex.matcher(emailField.getText());
        emailMatch = emailMatcher.matches();
        Matcher nameMatcher = nameSurnameRegex.matcher(nameField.getText());
        nameMatch = nameMatcher.matches();
        Matcher surnameMatcher = nameSurnameRegex.matcher(surnameField.getText());
        surnameMatch = surnameMatcher.matches();

        if(!nameMatch){
            Alert registerError = new Alert(Alert.AlertType.WARNING);
            registerError.setTitle("");
            registerError.setHeaderText("Введено некорректное значение");
            registerError.setContentText("Имя может содержать только кириллические и английские символы");
            registerError.showAndWait();
        } else if(!surnameMatch){
            Alert registerError = new Alert(Alert.AlertType.WARNING);
            registerError.setTitle("");
            registerError.setHeaderText("Введено некорректное значение");
            registerError.setContentText("Фамилия может содержать только кириллические и английские символы");
            registerError.showAndWait();
        } else if(!emailMatch){
            Alert registerError = new Alert(Alert.AlertType.WARNING);
            registerError.setTitle("");
            registerError.setHeaderText("Неккоректный формат email");
            registerError.setContentText("Email может содержать только английские символы и обязательно дожен содержать символы @ и .");
            registerError.showAndWait();
        } else {
            Alert registerWarning = new Alert(Alert.AlertType.WARNING);
            registerWarning.setHeaderText("Необходимое поле не было заполнено");
            if (nameField.getText().trim().isEmpty()) {
                registerWarning.setContentText("Заполните имя библиотекаря");
                registerWarning.showAndWait();
            } else if (surnameField.getText().trim().isEmpty()) {
                registerWarning.setContentText("Заполните фамилию библиотекаря");
                registerWarning.showAndWait();
            } else if (emailField.getText().trim().isEmpty()) {
                registerWarning.setContentText("Заполните адрес электронной почты");
                registerWarning.showAndWait();
            } else if (passwordField.getText().trim().isEmpty()) {
                registerWarning.setContentText("Задайте пароль для аккаунта сотрудника");
                registerWarning.showAndWait();
            } else if (repeatPasswordField.getText().trim().isEmpty()) {
                registerWarning.setContentText("Повторите свой пароль для завершения процесса регистрации");
                registerWarning.showAndWait();
            } else if (GenderComboBox.getValue().isEmpty()) {
                registerWarning.setContentText("Выберите Ваш пол");
                registerWarning.showAndWait();
            } else if (!passwordField.getText().equals(repeatPasswordField.getText())) {
                registerWarning.setHeaderText("Пароли не совпадают");
                registerWarning.setContentText("Пожалуйста, повторите свой пароль корректно");
                registerWarning.showAndWait();
            } else {
                Date today = Calendar.getInstance().getTime();
                Gender gender;
                if (GenderComboBox.getSelectionModel().getSelectedItem().equals("Мужской")) {
                    gender = Gender.MAN;
                } else {
                    gender = Gender.WOMAN;
                }
                Person newClient = new Person(nameField.getText(), surnameField.getText(), emailField.getText(), passwordField.getText(), today, gender, true);

                //////////////////////////////////////////

                Client.getClientWriter().writeInt(13);
                Client.getClientWriter().flush();

                Client.getClientWriter().writeObject(newClient);
                Client.getClientWriter().flush();

                boolean successOperation = Client.getClientReader().readBoolean();

                //////////////////////////////////////////

                if (successOperation) {
                    Alert registerInformation = new Alert(Alert.AlertType.INFORMATION);
                    registerInformation.setHeaderText("Регистрация проведена успешно");
                    registerInformation.setContentText("Аккаунт библиотекаря был создан для сотрудника " + nameField.getText() + " " + surnameField.getText());
                    registerInformation.showAndWait();
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
        SceneAnimation.LoadScene(rootPane);
        List<String> genderArrayList = new ArrayList<String>();
        genderArrayList.add("Мужской");
        genderArrayList.add("Женский");
        ObservableList<String> genderList = FXCollections.observableList(genderArrayList);
        GenderComboBox.setItems(genderList);
    }
}
