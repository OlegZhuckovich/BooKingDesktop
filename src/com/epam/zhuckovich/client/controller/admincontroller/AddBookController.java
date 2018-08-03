package com.epam.zhuckovich.client.controller.admincontroller;

import com.epam.zhuckovich.client.Client;
import com.epam.zhuckovich.client.action.SceneAnimation;
import com.epam.zhuckovich.entity.Book;
import com.epam.zhuckovich.entity.Genre;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddBookController implements Initializable{

    @FXML private AnchorPane rootPane;
    @FXML private Button MainMenuButton;
    @FXML private Button AddBookButton;
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField quantityField;
    @FXML private TextField publishingHouseField;
    @FXML private TextField yearField;
    @FXML private TextField pagesField;
    @FXML private ComboBox<String> genreComboBox;

    @FXML
    void AddBookInLibrary(ActionEvent event) throws SQLException, IOException {
        Alert addBookWarning = new Alert(Alert.AlertType.WARNING);
        addBookWarning.setHeaderText("Необходимое поле не было заполнено");
        if (titleField.getText().trim().isEmpty()) {
            addBookWarning.setContentText("Заполните название книги");
            addBookWarning.showAndWait();
        } else if (authorField.getText().trim().isEmpty()) {
            addBookWarning.setContentText("Введите автора книги");
            addBookWarning.showAndWait();
        } else if (quantityField.getText().trim().isEmpty()) {
            addBookWarning.setContentText("Введите количество экземпляров книги");
            addBookWarning.showAndWait();
        } else if (publishingHouseField.getText().trim().isEmpty()) {
            addBookWarning.setContentText("Введите значение для поля 'Издательство'");
            addBookWarning.showAndWait();
        } else if (yearField.getText().trim().isEmpty()) {
            addBookWarning.setContentText("Введите год издательства книг");
            addBookWarning.showAndWait();
        } else if (pagesField.getText().trim().isEmpty()) {
            addBookWarning.setContentText("Введите количество страниц в книге");
            addBookWarning.showAndWait();
        } else if (genreComboBox.getValue().isEmpty()) {
            addBookWarning.setContentText("Выберите жанр книги");
            addBookWarning.showAndWait();
        } else {
            Pattern numberRegex = Pattern.compile("[0-9]+");
            Pattern authorRegex = Pattern.compile("[^0-9]+");

            boolean yearMatch;
            boolean pagesMatch;
            boolean quantityMatch;
            boolean authorMatch;

            Matcher authorMatcher = authorRegex.matcher(authorField.getText());
            authorMatch = authorMatcher.matches();

            Matcher yearMatcher = numberRegex.matcher(yearField.getText());
            yearMatch = yearMatcher.matches();

            Matcher pagesMatcher = numberRegex.matcher(pagesField.getText());
            pagesMatch = pagesMatcher.matches();

            Matcher quantityMatcher = numberRegex.matcher(quantityField.getText());
            quantityMatch = quantityMatcher.matches();

            if (!authorMatch) {
                Alert registerError = new Alert(Alert.AlertType.WARNING);
                registerError.setTitle("");
                registerError.setHeaderText("Введено некорректное значение");
                registerError.setContentText("Значение 'Автор' не может содержать цифры");
                registerError.showAndWait();
            } else if (!pagesMatch) {
                Alert registerError = new Alert(Alert.AlertType.WARNING);
                registerError.setTitle("");
                registerError.setHeaderText("Введено некорректное значение");
                registerError.setContentText("Значение 'Количество страниц' может содержать только цифры");
                registerError.showAndWait();
            } else if (!quantityMatch) {
                Alert registerError = new Alert(Alert.AlertType.WARNING);
                registerError.setTitle("");
                registerError.setHeaderText("Введено некорректное значение");
                registerError.setContentText("Значение 'Количество' может содержать только цифры");
                registerError.showAndWait();
            } else if (!yearMatch) {
                Alert registerError = new Alert(Alert.AlertType.WARNING);
                registerError.setTitle("");
                registerError.setHeaderText("Введено некорректное значение");
                registerError.setContentText("Значение 'Год' может содержать только цифры");
                registerError.showAndWait();
            } else {
                Book newBook = new Book(titleField.getText(), authorField.getText(), Genre.valueOf(genreComboBox.getValue()), publishingHouseField.getText(),
                        Integer.valueOf(yearField.getText()), Integer.valueOf(pagesField.getText()), Integer.valueOf(quantityField.getText()));

                //////////////////////////////////////////

                Client.getClientWriter().writeInt(9);
                Client.getClientWriter().flush();

                Client.getClientWriter().writeObject(newBook);
                Client.getClientWriter().flush();

                boolean successOperation = Client.getClientReader().readBoolean();

                if (successOperation) {
                    Alert registerInformation = new Alert(Alert.AlertType.INFORMATION);
                    registerInformation.setHeaderText("Добавление произведено успешно");
                    registerInformation.setContentText("Книга " + titleField.getText() + " была успешно добавлена в библиотеку");
                    registerInformation.showAndWait();
                } else {
                    Alert registerError = new Alert(Alert.AlertType.ERROR);
                    registerError.setHeaderText("Ошибка добавления");
                    registerError.setContentText("Книга с таким названием и автором уже есть в библиотеке");
                    registerError.showAndWait();
                }
            }
        }
    }

    @FXML
    void goToMainMenu(ActionEvent event) {
        SceneAnimation.ChangeScene(rootPane,"../scenes/LibrarianMenu.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SceneAnimation.LoadScene(rootPane);
        ObservableList<String> genreArrayList = FXCollections.observableArrayList();
        genreArrayList.add("Приключения");
        genreArrayList.add("Проза");
        genreArrayList.add("Каталог");
        genreArrayList.add("Детская");
        genreArrayList.add("Детектив");
        genreArrayList.add("Экономика");
        genreArrayList.add("Фантастика");
        genreArrayList.add("Домашняя");
        genreArrayList.add("Модерн");
        genreArrayList.add("Стихи");
        genreArrayList.add("Научная");
        genreComboBox.setItems(genreArrayList);
    }
}
