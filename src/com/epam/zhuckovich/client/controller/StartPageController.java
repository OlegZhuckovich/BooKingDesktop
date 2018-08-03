package com.epam.zhuckovich.client.controller;

import com.epam.zhuckovich.client.Client;
import com.epam.zhuckovich.client.action.SceneAnimation;
import com.epam.zhuckovich.server.dao.LibraryDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StartPageController implements Initializable{

    private static String personName;
    private static String personSurname;
    private static String personEmail;
    private static String personPassword;

    @FXML private AnchorPane rootPane;
    @FXML private Button enterButton;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button registerButton;
    @FXML private Hyperlink forgetPasswordLink;


    @FXML
    void forgetPassword(ActionEvent event) throws IOException {
        Parent fxmlRoot = FXMLLoader.load(getClass().getResource("../scenes/ForgetPassword.fxml"));
        Scene newScene = new Scene(fxmlRoot);
        Stage registerForm = new Stage();
        registerForm.setScene(newScene);
        registerForm.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SceneAnimation.LoadScene(rootPane);
    }

    @FXML
    void enterToMenu(ActionEvent event) throws IOException, SQLException, InterruptedException { //вход в программу

        //////////////////////////////////////////

        Client.getClientWriter().writeInt(1);
        Client.getClientWriter().flush();

        Client.getClientWriter().writeObject(emailField.getText());
        Client.getClientWriter().flush();

        Client.getClientWriter().writeObject(passwordField.getText());
        Client.getClientWriter().flush();

        int enterInt = Client.getClientReader().readInt();

        /////////////////////////////////////////

        if(enterInt==0){
            Alert entranceError = new Alert(Alert.AlertType.ERROR);
            entranceError.setTitle("Ошибка входа");
            entranceError.setHeaderText("Вы ввели неправильный логин или пароль");
            entranceError.setContentText("Если вы забыли пароль, нажмите кнопку, расположенную ниже");
            entranceError.showAndWait();
        } else if(enterInt==1){
            SceneAnimation.ChangeScene(rootPane,"../scenes/ClientMenu.fxml");
            setCurrentPerson(emailField.getText(),passwordField.getText());
            personName = new LibraryDAO().getClientData(personEmail,personPassword).getName();
            personSurname = new LibraryDAO().getClientData(personEmail,personPassword).getSurname();
        } else {
            SceneAnimation.ChangeScene(rootPane,"../scenes/LibrarianMenu.fxml");
            setCurrentPerson(emailField.getText(),passwordField.getText());
            personName = new LibraryDAO().getClientData(personEmail,personPassword).getName();
            personSurname = new LibraryDAO().getClientData(personEmail,personPassword).getSurname();
        }
    }

    @FXML
    void registerClient(ActionEvent event) throws IOException {
        Parent fxmlRoot = FXMLLoader.load(getClass().getResource("../scenes/RegisterDialog.fxml"));
        Scene newScene = new Scene(fxmlRoot);
        Stage registerForm = new Stage();
        registerForm.setScene(newScene);
        registerForm.show();
    }

    public void setCurrentPerson(String email, String password) {
        this.personEmail=email;
        this.personPassword=password;
    }

    public String getPersonName(){ return personName;}

    public String getPersonSurname(){ return personSurname;}

    public String getPersonEmail(){ return personEmail;}

    public String getPersonPassword(){return personPassword;}
}
