package com.epam.zhuckovich.client.controller.clientcontroller;

import com.epam.zhuckovich.client.Client;
import com.epam.zhuckovich.client.action.GetClassController;
import com.epam.zhuckovich.client.action.SceneAnimation;
import com.epam.zhuckovich.client.controller.StartPageController;
import com.epam.zhuckovich.entity.Person;
import com.epam.zhuckovich.server.dao.LibraryDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditLibraryCardController implements Initializable{

    private FXMLLoader loader;

    @FXML private AnchorPane rootPane;
    @FXML private Button mainMenuButton;
    @FXML private TextField editNameField;
    @FXML private TextField editSurnameField;
    @FXML private TextField editEmailField;
    @FXML private PasswordField editPasswordField;
    @FXML private Button DeleteAccountButtton;
    @FXML private Button SaveChangesButton;


    @FXML
    void saveChanges(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {

        Pattern emailRegex = Pattern.compile("[a-zA-Z0-9]+@[a-z]{2,10}\\.[a-z]{2,10}");
        Pattern nameSurnameRegex = Pattern.compile("[a-zA-Zа-яА-Я]+");
        boolean emailMatch;
        boolean nameMatch;
        boolean surnameMatch;
        Matcher emailMatcher = emailRegex.matcher(editEmailField.getText());
        emailMatch = emailMatcher.matches();
        Matcher nameMatcher = nameSurnameRegex.matcher(editNameField.getText());
        nameMatch = nameMatcher.matches();
        Matcher surnameMatcher = nameSurnameRegex.matcher(editSurnameField.getText());
        surnameMatch = surnameMatcher.matches();

            LibraryDAO saveChangesDAO = new LibraryDAO();//получение контроллера
            GetClassController<StartPageController> controller = new GetClassController<>();
            StartPageController startPageController = controller.getController("StartPage.fxml");//получить данные клиента из бд по email и паролю со стартовой страницы

            //////////////////////////////////////////

            Client.getClientWriter().writeInt(5);
            Client.getClientWriter().flush();

            Client.getClientWriter().writeObject(startPageController.getPersonEmail());
            Client.getClientWriter().flush();

            Client.getClientWriter().writeObject(startPageController.getPersonPassword());
            Client.getClientWriter().flush();

            Person changeablePerson = (Person) Client.getClientReader().readObject();
            if (!editNameField.getText().isEmpty()) {
                if(!nameMatch){
                    Alert registerError = new Alert(Alert.AlertType.WARNING);
                    registerError.setTitle("");
                    registerError.setHeaderText("Введено некорректное значение");
                    registerError.setContentText("Имя может содержать только кириллические и английские символы");
                    registerError.showAndWait();
                } else {
                    changeablePerson.setName(editNameField.getText());
                }
            }
            if (!editSurnameField.getText().isEmpty()) {
                if (!surnameMatch){
                    Alert registerError = new Alert(Alert.AlertType.WARNING);
                    registerError.setTitle("");
                    registerError.setHeaderText("Введено некорректное значение");
                    registerError.setContentText("Фамилия может содержать только кириллические и английские символы");
                    registerError.showAndWait();
                } else {
                    changeablePerson.setSurname(editSurnameField.getText());
                }
            }
            if (!editEmailField.getText().isEmpty()) {
                if(!emailMatch){
                    Alert registerError = new Alert(Alert.AlertType.WARNING);
                    registerError.setTitle("");
                    registerError.setHeaderText("Неправильный формат email");
                    registerError.setContentText("Email может содержать только английские символы и обзательно дожен содержать символы @ и .");
                    registerError.showAndWait();
                } else {
                    changeablePerson.setEmail(editEmailField.getText());
                }
            }
            if (!editPasswordField.getText().isEmpty()) {
                changeablePerson.setPassword(editPasswordField.getText());
            }

            startPageController.setCurrentPerson(changeablePerson.getEmail(), changeablePerson.getPassword());

            Client.getClientWriter().writeObject(changeablePerson);
            Client.getClientWriter().flush();

            boolean successOperation = Client.getClientReader().readBoolean();

            if (successOperation) {
                Alert orderBookSuccess = new Alert(Alert.AlertType.INFORMATION);
                orderBookSuccess.setTitle("");
                orderBookSuccess.setHeaderText("Операция проведена успешно");
                orderBookSuccess.setContentText("Данные клиента успешно изменены");
                orderBookSuccess.showAndWait();
            } else {
                Alert orderBookSuccess = new Alert(Alert.AlertType.ERROR);
                orderBookSuccess.setTitle("");
                orderBookSuccess.setHeaderText("Ошибка проведения операции");
                orderBookSuccess.setContentText("Во время изменения данных клиента произошла ошибка");
                orderBookSuccess.showAndWait();
            }

            editEmailField.setText("");
            editNameField.setText("");
            editPasswordField.setText("");
            editSurnameField.setText("");
    }

    @FXML
    void goToMainMenu(ActionEvent event) {
        SceneAnimation.ChangeScene(rootPane,"../scenes/ClientMenu.fxml");
    }

    @FXML
    void deleteAccount(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("");
        alert.setHeaderText("Подтверждение операции");
        alert.setContentText("Вы точно хотите удалить аккаунт?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            GetClassController<StartPageController> controller = new GetClassController<>();
            StartPageController startPageController = null;
            try { startPageController = controller.getController("StartPage.fxml");
            } catch (IOException e) { e.printStackTrace();}
            String personEmail =  startPageController.getPersonEmail();

            Client.getClientWriter().writeInt(15);
            Client.getClientWriter().flush();

            Client.getClientWriter().writeObject(personEmail);
            Client.getClientWriter().flush();

            boolean successOperation = Client.getClientReader().readBoolean();
            if(successOperation){
                Alert orderBookSuccess = new Alert(Alert.AlertType.INFORMATION);
                orderBookSuccess.setTitle("");
                orderBookSuccess.setHeaderText("Операция проведена успешно");
                orderBookSuccess.setContentText("Аккаунт успешно удалён");
                orderBookSuccess.showAndWait();
                SceneAnimation.ChangeScene(rootPane,"../scenes/StartPage.fxml");
            } else{
                Alert orderBookSuccess = new Alert(Alert.AlertType.ERROR);
                orderBookSuccess.setTitle("");
                orderBookSuccess.setHeaderText("Ошибка проведения операции");
                orderBookSuccess.setContentText("Аккаунт не был удалён по техническим причинам");
                orderBookSuccess.showAndWait();
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SceneAnimation.LoadScene(rootPane);
    }
}