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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class ViewUserDataController implements Initializable{

    private ObservableList<Person> userList;

    @FXML private AnchorPane rootPane;
    @FXML private TableView<Person> UserTable;
    @FXML private TableColumn<Person, Integer> idColumn;
    @FXML private TableColumn<Person, String> nameColumn;
    @FXML private TableColumn<Person, String> surnameColumn;
    @FXML private TableColumn<Person, String> emailColumn;
    @FXML private TableColumn<Person, Date> dateColumn;
    @FXML private TableColumn<Person, Gender> genderColumn;
    @FXML private TableColumn<Person, Integer> bookColumn;
    @FXML private Button MainMenuButton;

    @FXML
    void goToMainMenu(ActionEvent event) {
        SceneAnimation.ChangeScene(rootPane,"../scenes/LibrarianMenu.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SceneAnimation.LoadScene(rootPane);
        try { initUserTable();
        } catch (IOException | ClassNotFoundException | SQLException e) { e.printStackTrace();}
    }

    public void changeIDCellEvent(TableColumn.CellEditEvent edittedCell){
        Person selectedPerson = UserTable.getSelectionModel().getSelectedItem();
        selectedPerson.setName(edittedCell.getNewValue().toString());
        System.out.println(userList.get(0).getName());
    }

    public void initUserTable() throws SQLException, IOException, ClassNotFoundException {
        userList = FXCollections.observableArrayList();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("personID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        bookColumn.setCellValueFactory(new PropertyValueFactory<>("bookOrdered"));

        UserTable.setEditable(true);
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        //////////////////////////////////////////

        Client.getClientWriter().writeInt(8);
        Client.getClientWriter().flush();
        ArrayList<Person> foundedUsersList = (ArrayList<Person>) Client.getClientReader().readObject();

        //////////////////////////////////////////

        if(foundedUsersList.isEmpty()) {
            Alert searchWarning = new Alert(Alert.AlertType.WARNING);
            searchWarning.setTitle("");
            searchWarning.setHeaderText("Пользователи не найдены");
            searchWarning.showAndWait();
        } else {
            for(Person currentPerson: foundedUsersList){
                userList.add(currentPerson);
            }
            UserTable.setItems(userList);
        }
    }
}