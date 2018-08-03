package com.epam.zhuckovich.client.controller;

import com.epam.zhuckovich.client.Client;
import com.epam.zhuckovich.server.Mail;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ForgetPasswordController implements Initializable{

    @FXML private Button forgetPasswordButton;
    @FXML private TextField forgetPasswordTextField;
    @FXML private TextField numberField;
    @FXML private Button numberButton;

    private int newPassword;


    @FXML
    void getNumberCode(ActionEvent event) throws MessagingException {
        if(forgetPasswordTextField.getText().isEmpty()){
            Alert orderBookSuccess = new Alert(Alert.AlertType.WARNING);
            orderBookSuccess.setTitle("");
            orderBookSuccess.setHeaderText("Текстовое поле не заполнено");
            orderBookSuccess.setContentText("Для восстановления доступа к аккаунту введите сначала email");
            orderBookSuccess.showAndWait();
        } else {
            boolean emailMatch = false;
            Pattern emailRegex = Pattern.compile("[a-zA-Z0-9]+@[a-z]{2,10}\\.[a-z]{2,10}");
            Matcher emailMatcher = emailRegex.matcher(forgetPasswordTextField.getText());
            emailMatch = emailMatcher.matches();
            if(emailMatch) {
                newPassword = (int) (Math.random() * 10000000);
                String mailTitle = "Восстановление доступа к аккаунту";
                String mailMessage = "Здравствуйте, для Вашего аккаунта была вызвана операция восстановления пароля в системе BooKingApp.\n Ваш числовой код - " + String.valueOf(newPassword) +
                        ".\n Если Вы получили это письмо по ошибке, пожалуйста, проигнорируйте его";
                Mail.Send("bookinglibrarybelarus", "booking2048", forgetPasswordTextField.getText(), mailTitle, mailMessage);
            } else {
                Alert orderBookSuccess = new Alert(Alert.AlertType.WARNING);
                orderBookSuccess.setTitle("");
                orderBookSuccess.setHeaderText("Некорректный формат email");
                orderBookSuccess.setContentText("Введённый email должен содержать только английские символы, а также символы '@' и .");
                orderBookSuccess.showAndWait();
            }
        }
    }

    @FXML
    void restoreAccess(ActionEvent event) throws MessagingException, IOException {
        if(!numberField.getText().equals(String.valueOf(newPassword))){
            Alert orderBookSuccess = new Alert(Alert.AlertType.ERROR);
            orderBookSuccess.setTitle("");
            orderBookSuccess.setHeaderText("Ошибка восстановления пароля");
            orderBookSuccess.setContentText("Числовой код введён неверно");
            orderBookSuccess.showAndWait();
        } else {
            Client.getClientWriter().writeInt(16);
            Client.getClientWriter().flush();

            Client.getClientWriter().writeObject(forgetPasswordTextField.getText());
            Client.getClientWriter().flush();

            Client.getClientWriter().writeInt(newPassword);
            Client.getClientWriter().flush();

            boolean successOperation = Client.getClientReader().readBoolean();
            if(successOperation){
                Alert orderBookSuccess = new Alert(Alert.AlertType.INFORMATION);
                orderBookSuccess.setTitle("");
                orderBookSuccess.setHeaderText("Операция восстановления пароля проведена успешно");
                orderBookSuccess.setContentText("Временный пароль - это числовой код отправленный ранее на email");
                orderBookSuccess.showAndWait();
            } else {
                Alert orderBookSuccess = new Alert(Alert.AlertType.ERROR);
                orderBookSuccess.setTitle("");
                orderBookSuccess.setHeaderText("Ошибка восстановления пароля");
                orderBookSuccess.setContentText("Во время операции восстановления пароля произошёл техничский сбой");
                orderBookSuccess.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
