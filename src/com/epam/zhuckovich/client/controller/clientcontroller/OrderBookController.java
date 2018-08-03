package com.epam.zhuckovich.client.controller.clientcontroller;

import com.epam.zhuckovich.client.Client;
import com.epam.zhuckovich.client.action.GetClassController;
import com.epam.zhuckovich.client.action.SceneAnimation;
import com.epam.zhuckovich.client.controller.StartPageController;
import com.epam.zhuckovich.entity.Book;
import com.epam.zhuckovich.entity.Genre;
import com.epam.zhuckovich.server.dao.LibraryDAO;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrderBookController implements Initializable {

    private ObservableList<Book> bookList;

    @FXML private AnchorPane rootPane;
    @FXML private Button MainMenuButton;
    @FXML private ComboBox<String> SearchParameterBox;
    @FXML private TextField SearchTextField;
    @FXML private Button OrderBookButton;
    @FXML private Button FindBookButton;
    @FXML private TableView<Book> BookTable;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, Genre> genreColumn;
    @FXML private TableColumn<Book, String> publishingColumn;
    @FXML private TableColumn<Book, Integer> yearColumn;
    @FXML private TableColumn<Book, Integer> pagesColumn;
    @FXML private TableColumn<Book, Integer> quantityColumn;


    @FXML
    void goToMainMenu(ActionEvent event) {
        SceneAnimation.ChangeScene(rootPane,"../scenes/ClientMenu.fxml");
    }

    @FXML
    void findBook(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
        Alert searchWarning = new Alert(Alert.AlertType.WARNING);
        searchWarning.setTitle("");
        String searchParameter = SearchParameterBox.getSelectionModel().getSelectedItem();
        String searchValue = SearchTextField.getText();
        if(searchParameter==null){
            searchWarning.setHeaderText("Параметр поиска не найден");
            searchWarning.setContentText("Выберите тип поиска книги");
            searchWarning.showAndWait();
        } else if (searchValue.isEmpty()){
            searchWarning.setHeaderText("Значение для поиска не введено");
            searchWarning.setContentText("Введите значение для поиска книги");
            searchWarning.showAndWait();
        } else {

            //////////////////////////////////////////

            Client.getClientWriter().writeInt(3);
            Client.getClientWriter().flush();

            switch(searchParameter){
                case "По названию": Client.getClientWriter().writeObject("Title"); break;
                case "По автору": Client.getClientWriter().writeObject("Author"); break;
                case "По жанру": Client.getClientWriter().writeObject("Genre"); break;
                case "По году издания": Client.getClientWriter().writeObject("Genre"); break;
            }

            Client.getClientWriter().flush();
            Client.getClientWriter().writeObject(searchValue);
            Client.getClientWriter().flush();

            ArrayList<Book> foundedBooks = (ArrayList<Book>) Client.getClientReader().readObject();

            /////////////////////////////////////////

            if(foundedBooks.isEmpty()){
                searchWarning.setHeaderText("Книги не найдены");
                searchWarning.setContentText("По заданным значениям книги не найдены");
                searchWarning.showAndWait();
            } else {
                SearchParameterBox.setLayoutY(80);
                SearchTextField.setLayoutY(80);
                bookList.clear();
                for (Book currentBook:foundedBooks){
                    bookList.add(currentBook);
                }
                BookTable.setItems(bookList);
                BookTable.setOpacity(1);
            }
        }
    }


    @FXML
    void orderBook(ActionEvent event) throws SQLException, IOException {
        Book orderedBook = BookTable.getSelectionModel().getSelectedItem();
        if(orderedBook==null){
            Alert orderBookSuccess = new Alert(Alert.AlertType.ERROR);
            orderBookSuccess.setTitle("");
            orderBookSuccess.setHeaderText("Ошибка заказа книги");
            orderBookSuccess.setContentText("Прежде чем заказать книгу, воспользуйтесь поиском и выберите книгу из таблицы");
            orderBookSuccess.showAndWait();
        } else {
            GetClassController<StartPageController> controller = new GetClassController<>();
            StartPageController startPageController = null;//получить данные клиента из бд по email и паролю со стартовой страницы
            try { startPageController = controller.getController("StartPage.fxml");
            } catch (IOException e) { e.printStackTrace();}

            //////////////////////////////////////////

            Client.getClientWriter().writeInt(4);
            Client.getClientWriter().flush();

            Client.getClientWriter().writeObject(orderedBook);
            Client.getClientWriter().flush();

            Client.getClientWriter().writeObject(startPageController.getPersonEmail());
            Client.getClientWriter().flush();

            boolean orderBook = Client.getClientReader().readBoolean();

            //////////////////////////////////////////

            if (orderBook) {
                Alert orderBookSuccess = new Alert(Alert.AlertType.INFORMATION);
                orderBookSuccess.setTitle("");
                orderBookSuccess.setHeaderText("Книга успешно заказана");
                orderBookSuccess.setContentText("Просмотреть взятые книги вы можете в разделе 'Моя библиотека'");
                orderBookSuccess.showAndWait();
            } else {
                Alert orderBookError = new Alert(Alert.AlertType.ERROR);
                orderBookError.setTitle("");
                orderBookError.setHeaderText("Ошибка при заказе книги");
                orderBookError.setContentText("Возможно Вы уже взяли данную книгу в библиотеке. Для проверки войдите в раздел 'Моя библиотека'");
                orderBookError.showAndWait();
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initBookTable();
        initSearchParameterBox();
        SceneAnimation.LoadScene(rootPane);
    }

    public void initBookTable(){
        bookList = FXCollections.observableArrayList();
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        publishingColumn.setCellValueFactory(new PropertyValueFactory<>("publishingHouse"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        pagesColumn.setCellValueFactory(new PropertyValueFactory<>("pages"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    public void initSearchParameterBox(){
        ObservableList<String> parameterList = FXCollections.observableArrayList();
        parameterList.add("По названию");
        parameterList.add("По автору");
        parameterList.add("По жанру");
        parameterList.add("По году издания");
        SearchParameterBox.setItems(parameterList);
    }
}
