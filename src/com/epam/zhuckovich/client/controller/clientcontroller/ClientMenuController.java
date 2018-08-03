package com.epam.zhuckovich.client.controller.clientcontroller;

import com.epam.zhuckovich.client.action.GetClassController;
import com.epam.zhuckovich.client.action.SceneAnimation;
import com.epam.zhuckovich.client.controller.StartPageController;
import com.qoppa.pdfViewerFX.PDFViewer;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class ClientMenuController implements Initializable{

    private ColorAdjust[] makeLightningDarker;

    @FXML private AnchorPane rootPane;
    @FXML private ImageView OrderBookButton;
    @FXML private ImageView EditLibraryCardButton;
    @FXML private ImageView ReadBookButton;
    @FXML private ImageView BuildGraphButton;
    @FXML private ImageView InternetLibraryButton;
    @FXML private ImageView ExitButton;
    @FXML private Rectangle MenuItemBlock;
    @FXML private Label DateLabel;
    @FXML private Label MenuItemLabel;
    @FXML private Label UserLabel;


    @FXML
    void OpenBuildGraph(MouseEvent event) { SceneAnimation.ChangeScene(rootPane,"../scenes/BuildGraphMenu.fxml"); }

    @FXML
    void OpenOrderBook(MouseEvent event) {
        SceneAnimation.ChangeScene(rootPane,"../scenes/OrderBookMenu.fxml");
    }

    @FXML
    void OpenEditLibraryCard(MouseEvent event) { SceneAnimation.ChangeScene(rootPane,"../scenes/EditLibraryCardMenu.fxml"); }

    @FXML
    void OpenMyLibrary(MouseEvent event) {
        SceneAnimation.ChangeScene(rootPane,"../scenes/MyLibrary.fxml");
    }


    @FXML
    void OpenReader(MouseEvent event) {
        Stage readerStage = new Stage();
        PDFViewer m_PDFViewer = new PDFViewer();
        Pane readerPane = new Pane(m_PDFViewer);
        readerPane.setPrefSize(900,560);
        Scene readerScene = new Scene(readerPane);
        readerStage.setScene(readerScene);
        readerStage.show();
    }

    @FXML
    void goToStartPage(MouseEvent event) {
        SceneAnimation.ChangeScene(rootPane,"../scenes/StartPage.fxml");
    }

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
        if(event.getSource().toString().contains("OrderBookButton")){
            makeDarkerID(makeLightningDarker[0]);
            MenuItemLabel.setText("Заказать книгу");
        } else if (event.getSource().toString().contains("EditLibraryCardButton")){
            makeDarkerID(makeLightningDarker[1]);
            MenuItemLabel.setText("Редактировать данные аккаунта");
        } else if (event.getSource().toString().contains("ReadBookButton")){
            makeDarkerID(makeLightningDarker[2]);
            MenuItemLabel.setText("Прочитать книгу");
        } else if (event.getSource().toString().contains("BuildGraphButton")){
            makeDarkerID(makeLightningDarker[3]);
            MenuItemLabel.setText("Просмотреть статистику");
        } else if (event.getSource().toString().contains("InternetLibraryButton")){
            makeDarkerID(makeLightningDarker[4]);
            MenuItemLabel.setText("Моя библиотека");
        } else if (event.getSource().toString().contains("ExitButton")){
            makeDarkerID(makeLightningDarker[5]);
            MenuItemLabel.setText("Выйти");
        }
    }

    @FXML
    void makeDefault(MouseEvent event) {
        MenuItemBlock.setOpacity(0);
        if(event.getSource().toString().contains("OrderBookButton")){
            makeDefaultID(makeLightningDarker[0]);
            MenuItemLabel.setText("");
        } else if (event.getSource().toString().contains("EditLibraryCardButton")){
            makeDefaultID(makeLightningDarker[1]);
            MenuItemLabel.setText("");
        } else if (event.getSource().toString().contains("ReadBookButton")){
            makeDefaultID(makeLightningDarker[2]);
            MenuItemLabel.setText("");
        } else if (event.getSource().toString().contains("BuildGraphButton")){
            makeDefaultID(makeLightningDarker[3]);
            MenuItemLabel.setText("");
        } else if (event.getSource().toString().contains("InternetLibraryButton")){
            makeDefaultID(makeLightningDarker[4]);
            MenuItemLabel.setText("");
        } else if (event.getSource().toString().contains("ExitButton")){
            makeDefaultID(makeLightningDarker[5]);
            MenuItemLabel.setText("");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GetClassController<StartPageController> controller = new GetClassController<>();
        StartPageController startPageController = null;//получить данные клиента из бд по email и паролю со стартовой страницы
        try { startPageController = controller.getController("StartPage.fxml");
        } catch (IOException e) { e.printStackTrace();}
        UserLabel.setText("Читатель: " + startPageController.getPersonName() + " " + startPageController.getPersonSurname());//устанавливаем имя пользователя
        makeLightningDarker = new ColorAdjust[6];
        for(int temp=0; temp<makeLightningDarker.length;temp++){
            makeLightningDarker[temp] = new ColorAdjust();
            makeLightningDarker[temp].setBrightness(0.0);
        }
        OrderBookButton.setEffect(makeLightningDarker[0]);
        EditLibraryCardButton.setEffect(makeLightningDarker[1]);
        ReadBookButton.setEffect(makeLightningDarker[2]);
        BuildGraphButton.setEffect(makeLightningDarker[3]);
        InternetLibraryButton.setEffect(makeLightningDarker[4]);
        ExitButton.setEffect(makeLightningDarker[5]);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String currentDate = dateFormat.format(date);

        MenuItemLabel.setTextAlignment(TextAlignment.CENTER);
        DateLabel.setText(currentDate);
        SceneAnimation.LoadScene(rootPane);
    }
}