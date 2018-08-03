package com.epam.zhuckovich.client.controller.clientcontroller;

import com.epam.zhuckovich.client.Client;
import com.epam.zhuckovich.client.action.GetClassController;
import com.epam.zhuckovich.client.action.SceneAnimation;
import com.epam.zhuckovich.client.controller.StartPageController;
import com.epam.zhuckovich.entity.Book;
import com.epam.zhuckovich.entity.Genre;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class MyLibraryController implements Initializable {

    private ObservableList<Book> bookList;

    @FXML private AnchorPane rootPane;
    @FXML private Button MainMenuButton;
    @FXML private Button ReturnBookButton;
    @FXML private TableView<Book> BookTable;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, Genre> genreColumn;
    @FXML private TableColumn<Book, String> publishingColumn;
    @FXML private TableColumn<Book, Integer> yearColumn;
    @FXML private TableColumn<Book, Integer> pageColumn;
    @FXML private TableColumn<Book, Date> orderColumn;
    @FXML private TableColumn<Book, Date> returnColumn;
    @FXML private Label libraryLabel;

    String email;


    @FXML
    void goToMainMenu(ActionEvent event) {
        SceneAnimation.ChangeScene(rootPane, "../scenes/ClientMenu.fxml");
    }

    @FXML
    void returnBook(ActionEvent event) throws IOException {
        Book returnedBook = BookTable.getSelectionModel().getSelectedItem();

        Client.getClientWriter().writeInt(14);
        Client.getClientWriter().flush();

        Client.getClientWriter().writeObject(email);
        Client.getClientWriter().flush();

        Client.getClientWriter().writeObject(returnedBook);
        Client.getClientWriter().flush();

        boolean successOperation = Client.getClientReader().readBoolean();
        if(successOperation){
            Alert orderBookSuccess = new Alert(Alert.AlertType.INFORMATION);
            orderBookSuccess.setTitle("");
            orderBookSuccess.setHeaderText("Книга успешно возвращена в библиотеку");
            orderBookSuccess.setContentText("Благодарим Вас за своевременное возвращение книг");
            orderBookSuccess.showAndWait();
            bookList.remove(returnedBook);
            BookTable.setItems(bookList);
        } else {
            Alert orderBookSuccess = new Alert(Alert.AlertType.ERROR);
            orderBookSuccess.setTitle("");
            orderBookSuccess.setHeaderText("Технический сбой во время проведения операции");
            orderBookSuccess.setContentText("Попробуйте выполнить операцию позже");
            orderBookSuccess.showAndWait();
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
        pageColumn.setCellValueFactory(new PropertyValueFactory<>("pages"));
        orderColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        returnColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        GetClassController<StartPageController> controller = new GetClassController<>();
        StartPageController startPageController = null;
        try { startPageController = controller.getController("StartPage.fxml");
        } catch (IOException e) { e.printStackTrace();}
        String personEmail =  startPageController.getPersonEmail();
        this.email = personEmail;
        ArrayList<Book> clientBooks = new ArrayList<>();
        try {
            Client.getClientWriter().writeInt(7);
            Client.getClientWriter().flush();

            Client.getClientWriter().writeObject(personEmail);
            Client.getClientWriter().flush();

            clientBooks = (ArrayList<Book>) Client.getClientReader().readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        for (Book currentBook: clientBooks){
            bookList.add(currentBook);
        }
        if(!bookList.isEmpty()) {
            BookTable.setItems(bookList);
        } else {
            rootPane.getChildren().remove(BookTable);
            rootPane.getChildren().remove(ReturnBookButton);
        }
    }
}
