package com.epam.zhuckovich.client.controller.admincontroller;

import com.epam.zhuckovich.client.action.GetClassController;
import com.epam.zhuckovich.client.action.SceneAnimation;
import com.epam.zhuckovich.client.controller.StartPageController;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class LibrarianMenuController implements Initializable{

    private ColorAdjust[] makeLightningDarker;

    @FXML private AnchorPane rootPane;
    @FXML private Label LibrarianLabel;
    @FXML private Rectangle MenuItemBlock;
    @FXML private Label MenuItemLabel;
    @FXML private Label DateLabel;
    @FXML private ImageView ViewUserDataButton;
    @FXML private ImageView AddBookButton;
    @FXML private ImageView ControlBookButton;
    @FXML private ImageView GraphicAnalyticsButton;
    @FXML private ImageView RegisterNewLibrarianButton;
    @FXML private ImageView ExitButton;


    public static void makeDarkerID(ColorAdjust makeLightningDarker){
        Timeline makeDarkerTimeline = new Timeline(new KeyFrame(Duration.seconds(0),
                new KeyValue(makeLightningDarker.brightnessProperty(), makeLightningDarker.brightnessProperty().getValue(), Interpolator.LINEAR)),
                new KeyFrame(Duration.seconds(1), new KeyValue(makeLightningDarker.brightnessProperty(), -0.3, Interpolator.LINEAR)));
        makeDarkerTimeline.setCycleCount(1);
        makeDarkerTimeline.setAutoReverse(false);
        makeDarkerTimeline.play();
    }

    public static void makeDefaultID(ColorAdjust makeLightningDarker){
        Timeline makeDefaultTimeline = new Timeline(new KeyFrame(Duration.seconds(0),
                new KeyValue(makeLightningDarker.brightnessProperty(), makeLightningDarker.brightnessProperty().getValue(), Interpolator.LINEAR)),
                new KeyFrame(Duration.seconds(1), new KeyValue(makeLightningDarker.brightnessProperty(), 0, Interpolator.LINEAR)));
        makeDefaultTimeline.setCycleCount(1);
        makeDefaultTimeline.setAutoReverse(false);
        makeDefaultTimeline.play();
    }


    @FXML
    void makeDarker(MouseEvent event) {
        MenuItemBlock.setOpacity(1);
        if(event.getSource().toString().contains("ViewUserDataButton")){
            makeDarkerID(makeLightningDarker[0]);
            MenuItemLabel.setText("Просмотреть зарегистрированных читателей");
        } else if (event.getSource().toString().contains("AddBookButton")){
            makeDarkerID(makeLightningDarker[1]);
            MenuItemLabel.setText("Добавить новую книгу");
        } else if (event.getSource().toString().contains("ControlBookButton")){
            makeDarkerID(makeLightningDarker[2]);
            MenuItemLabel.setText("Управление библиотекой");
        } else if (event.getSource().toString().contains("GraphicAnalyticsButton")){
            makeDarkerID(makeLightningDarker[3]);
            MenuItemLabel.setText("Статистика библиотеки");
        } else if (event.getSource().toString().contains("RegisterNewLibrarianButton")){
            makeDarkerID(makeLightningDarker[4]);
            MenuItemLabel.setText("Зарегистрировать нового библиотекаря");
        } else if (event.getSource().toString().contains("ExitButton")){
            makeDarkerID(makeLightningDarker[5]);
            MenuItemLabel.setText("Выйти");
        }
    }

    @FXML
    void makeDefault(MouseEvent event) {
        MenuItemBlock.setOpacity(0);
        if(event.getSource().toString().contains("ViewUserDataButton")){
            makeDefaultID(makeLightningDarker[0]);
            MenuItemLabel.setText("");
        } else if (event.getSource().toString().contains("AddBookButton")){
            makeDefaultID(makeLightningDarker[1]);
            MenuItemLabel.setText("");
        } else if (event.getSource().toString().contains("ControlBookButton")){
            makeDefaultID(makeLightningDarker[2]);
            MenuItemLabel.setText("");
        } else if (event.getSource().toString().contains("GraphicAnalyticsButton")){
            makeDefaultID(makeLightningDarker[3]);
            MenuItemLabel.setText("");
        } else if (event.getSource().toString().contains("RegisterNewLibrarianButton")){
            makeDefaultID(makeLightningDarker[4]);
            MenuItemLabel.setText("");
        } else if (event.getSource().toString().contains("ExitButton")){
            makeDefaultID(makeLightningDarker[5]);
            MenuItemLabel.setText("");
        }
    }

    @FXML
    void OpenUserViewer(MouseEvent event) {
        SceneAnimation.ChangeScene(rootPane,"../scenes/ViewUserData.fxml");
    }

    @FXML
    void OpenAddNewBook(MouseEvent event) {
        SceneAnimation.ChangeScene(rootPane,"../scenes/AddNewBook.fxml");
    }

    @FXML
    void OpenLibraryControl(MouseEvent event) {
        SceneAnimation.ChangeScene(rootPane,"../scenes/LibraryControl.fxml");
    }

    @FXML
    void OpenLibraryStatystic(MouseEvent event) {
        SceneAnimation.ChangeScene(rootPane,"../scenes/BuildGraphAdmin.fxml");
    }

    @FXML
    void OpenRegisterLibrarian(MouseEvent event) {
        SceneAnimation.ChangeScene(rootPane,"../scenes/RegisterLibrarian.fxml");
    }

    @FXML
    void goToStartPage(MouseEvent event) {
        SceneAnimation.ChangeScene(rootPane,"../scenes/StartPage.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SceneAnimation.LoadScene(rootPane);
        GetClassController<StartPageController> controller = new GetClassController<>();
        StartPageController startPageController = null;//получить данные клиента из бд по email и паролю со стартовой страницы
        try { startPageController = controller.getController("StartPage.fxml");
        } catch (IOException e) { e.printStackTrace();}
        LibrarianLabel.setText("Библиотекарь: " + startPageController.getPersonName() + " " + startPageController.getPersonSurname());//устанавливаем имя пользователя
        makeLightningDarker = new ColorAdjust[6];
        for(int temp=0; temp<makeLightningDarker.length;temp++){
            makeLightningDarker[temp] = new ColorAdjust();
            makeLightningDarker[temp].setBrightness(0.0);
        }
        ViewUserDataButton.setEffect(makeLightningDarker[0]);
        AddBookButton.setEffect(makeLightningDarker[1]);
        ControlBookButton.setEffect(makeLightningDarker[2]);
        GraphicAnalyticsButton.setEffect(makeLightningDarker[3]);
        RegisterNewLibrarianButton.setEffect(makeLightningDarker[4]);
        ExitButton.setEffect(makeLightningDarker[5]);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String currentDate = dateFormat.format(date);

        MenuItemLabel.setTextAlignment(TextAlignment.CENTER);
        DateLabel.setText(currentDate);
    }
}