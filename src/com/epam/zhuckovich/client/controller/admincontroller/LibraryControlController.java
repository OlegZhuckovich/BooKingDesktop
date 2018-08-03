package com.epam.zhuckovich.client.controller.admincontroller;

import com.epam.zhuckovich.client.Client;
import com.epam.zhuckovich.client.action.SceneAnimation;
import com.epam.zhuckovich.entity.Book;
import com.epam.zhuckovich.entity.Genre;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.IntegerStringConverter;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LibraryControlController implements Initializable{

    private static final String FILE_PATH = "BooKingTextReport.txt";
    private ObservableList<Book> bookList;
    private Pattern numberRegex;

    @FXML private AnchorPane rootPane;
    @FXML private Button TextReportButton;
    @FXML private Button SaveChangesButton;
    @FXML private Button MainMenuButton;
    @FXML private TableView<Book> BookTable;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, Genre> genreColumn;
    @FXML private TableColumn<Book, String> publishingColumn;
    @FXML private TableColumn<Book, Integer> yearColumn;
    @FXML private TableColumn<Book, Integer> pagesColumn;
    @FXML private TableColumn<Book, Integer> quantityColumn;


    @FXML
    void changeTitle(TableColumn.CellEditEvent editedCell) {
        Book selectedBook = BookTable.getSelectionModel().getSelectedItem();
        selectedBook.setTitle(editedCell.getNewValue().toString());
    }

    @FXML
    void changeAuthor(TableColumn.CellEditEvent editedCell) {
        Book selectedBook = BookTable.getSelectionModel().getSelectedItem();
        selectedBook.setAuthor(editedCell.getNewValue().toString());
    }

    @FXML
    void changePublishingHouse(TableColumn.CellEditEvent editedCell) {
        Book selectedBook = BookTable.getSelectionModel().getSelectedItem();
        selectedBook.setPublishingHouse(editedCell.getNewValue().toString());
    }

    @FXML
    void changeYear(TableColumn.CellEditEvent editedCell) {
        Book selectedBook = BookTable.getSelectionModel().getSelectedItem();
        boolean numberMatch = checkExpression(editedCell.getNewValue().toString());
        if(numberMatch){
            selectedBook.setYear(Integer.valueOf(editedCell.getNewValue().toString()));
        } else {
            Alert updateBookError = new Alert(Alert.AlertType.ERROR);
            updateBookError.setHeaderText("Ошибка редактирования книги");
            updateBookError.setContentText("В ячейку введено некорректное значение");
            updateBookError.showAndWait();
        }
    }

    @FXML
    void changePages(TableColumn.CellEditEvent editedCell) {
        Book selectedBook = BookTable.getSelectionModel().getSelectedItem();
        boolean numberMatch = checkExpression(editedCell.getNewValue().toString());
        if(numberMatch){
            selectedBook.setPages(Integer.valueOf(editedCell.getNewValue().toString()));
        } else {
            Alert updateBookError = new Alert(Alert.AlertType.ERROR);
            updateBookError.setHeaderText("Ошибка редактирования книги");
            updateBookError.setContentText("В ячейку введено некорректное значение");
            updateBookError.showAndWait();
        }
    }

    @FXML
    void changeQuantity(TableColumn.CellEditEvent editedCell) {
        Book selectedBook = BookTable.getSelectionModel().getSelectedItem();
        boolean numberMatch = checkExpression(editedCell.getNewValue().toString());
        if(numberMatch){
            selectedBook.setQuantity(Integer.valueOf(editedCell.getNewValue().toString()));
        } else {
            Alert updateBookError = new Alert(Alert.AlertType.ERROR);
            updateBookError.setHeaderText("Ошибка редактирования книги");
            updateBookError.setContentText("В ячейку введено некорректное значение");
            updateBookError.showAndWait();
        }
    }

    @FXML
    void goToMainMenu(ActionEvent event) {
        SceneAnimation.ChangeScene(rootPane,"../scenes/LibrarianMenu.fxml");
    }

    @FXML
    void createTextReport(ActionEvent event) {
        try {
            FileWriter writeFile = new FileWriter(FILE_PATH, false);
            for (Book currentBook : bookList) {
                writeFile.append(currentBook.toString() + "\n");
            }
            writeFile.close();
            Alert orderBookSuccess = new Alert(Alert.AlertType.INFORMATION);
            orderBookSuccess.setTitle("");
            orderBookSuccess.setHeaderText("Операция проведена успешно");
            orderBookSuccess.setContentText("Сформированный текстовый отчёт находится в корневом каталоге и имеет имя 'BooKingTextReport.txt'");
            orderBookSuccess.showAndWait();
        } catch(IOException e){
            Alert orderBookSuccess = new Alert(Alert.AlertType.INFORMATION);
            orderBookSuccess.setTitle("");
            orderBookSuccess.setHeaderText("Ошибка выполнения операции");
            orderBookSuccess.setContentText("Произошёл технический сбой");
            orderBookSuccess.showAndWait();
        }

    }

    @FXML
    void saveChanges(ActionEvent event) throws IOException {

        //////////////////////////////////////////

        Client.getClientWriter().writeInt(11);
        Client.getClientWriter().flush();

        ArrayList<Book> bookArrayList = new ArrayList<>();
        for(Book currentBook:bookList){
            bookArrayList.add(currentBook);
        }

        Client.getClientWriter().writeObject(bookArrayList);
        Client.getClientWriter().flush();

        int errorCounter = Client.getClientReader().readInt();

        //////////////////////////////////////////

        if(errorCounter==0){
            Alert successUpdateBook = new Alert(Alert.AlertType.INFORMATION);
            successUpdateBook.setHeaderText("Обновление данных библиотеки");
            successUpdateBook.setContentText("Данные книг успешно обновлены");
            successUpdateBook.showAndWait();
        } else if (errorCounter==-1){
            Alert updateBookError = new Alert(Alert.AlertType.ERROR);
            updateBookError.setHeaderText("Сбой обновления данных");
            updateBookError.setContentText("Произошла ошибка во время обновления данных");
            updateBookError.showAndWait();
        } else {
            Alert updateBookError = new Alert(Alert.AlertType.ERROR);
            updateBookError.setHeaderText("Для одной из книг введены некорректные числовые значения");
            updateBookError.setContentText("Значения для года издания, кол-ва страниц и количества экземпляров не могут быть отрицательными. Для книги обновление данных не выполняется");
            updateBookError.showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SceneAnimation.LoadScene(rootPane);
        bookList = FXCollections.observableArrayList();
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        publishingColumn.setCellValueFactory(new PropertyValueFactory<>("publishingHouse"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        pagesColumn.setCellValueFactory(new PropertyValueFactory<>("pages"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        //////////////////////////////////////////

        try {
            Client.getClientWriter().writeInt(10);
            Client.getClientWriter().flush();
            ArrayList<Book> foundedBooks = (ArrayList<Book>) Client.getClientReader().readObject();
            if(foundedBooks.isEmpty()) {
                Alert searchWarning = new Alert(Alert.AlertType.WARNING);
                searchWarning.setTitle("");
                searchWarning.setHeaderText("В библиотеке нет ни одной книги");
                searchWarning.showAndWait();
            } else {
                for (Book currentBook: foundedBooks){
                    bookList.add(currentBook);
                }
                BookTable.setItems(bookList);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        //////////////////////////////////////////

        BookTable.setEditable(true);
        titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        authorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        publishingColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        yearColumn.setCellFactory(TextFieldTableCell.<Book, Integer>forTableColumn(new IntegerStringConverter()));
        pagesColumn.setCellFactory(TextFieldTableCell.<Book, Integer>forTableColumn(new IntegerStringConverter()));
        quantityColumn.setCellFactory(TextFieldTableCell.<Book, Integer>forTableColumn(new IntegerStringConverter()));
    }


    boolean checkExpression(String expression){
        boolean numberMatch = false;
        numberRegex = Pattern.compile("[0-9]+");
        Matcher regexMatcher = numberRegex.matcher(expression);
        numberMatch = regexMatcher.find();
        return numberMatch;
    }
}
