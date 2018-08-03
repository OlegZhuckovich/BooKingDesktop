package com.epam.zhuckovich.client.controller.clientcontroller;

import com.epam.zhuckovich.client.Client;
import com.epam.zhuckovich.client.action.SceneAnimation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BuildGraphClientController implements Initializable{

    @FXML private AnchorPane rootPane;
    @FXML private Button mainMenuButton;
    @FXML private BarChart<String, Number> barChart;
    @FXML private Button BuilChartGraphic;

    @FXML
    void buildChart(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
        barChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();

        //////////////////////////////////////////

        Client.getClientWriter().writeInt(6);
        Client.getClientWriter().flush();

        ArrayList<String> titleBookList = (ArrayList<String>) Client.getClientReader().readObject();
        ArrayList<Number> quantityBookList = (ArrayList<Number>) Client.getClientReader().readObject();

        //////////////////////////////////////////

        for(int i=0; i<titleBookList.size(); i++) {
            series.getData().add(new XYChart.Data<>(titleBookList.get(i), quantityBookList.get(i)));
        }
        barChart.getData().add(series);
    }


    @FXML
    void goToMainMenu(ActionEvent event) {
        SceneAnimation.ChangeScene(rootPane,"../scenes/ClientMenu.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SceneAnimation.LoadScene(rootPane);
    }
}
