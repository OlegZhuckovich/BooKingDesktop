package com.epam.zhuckovich.client.controller.admincontroller;

import com.epam.zhuckovich.client.Client;
import com.epam.zhuckovich.client.action.SceneAnimation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;

public class BuildGraphAdminController implements Initializable{

    private final ObservableList<PieChart.Data> genderList = FXCollections.observableArrayList();
    private final ObservableList<PieChart.Data> genreList = FXCollections.observableArrayList();

    @FXML private AnchorPane rootPane;
    @FXML private PieChart genrePieChart;
    @FXML private PieChart genderPieChart;
    @FXML private Button MainMenuButton;
    @FXML private Button BuildChartButton;

    private HashMap<String,Integer> genderHashMap;
    private HashMap<String,Integer> genreHashMap;

    @FXML
    void goToMainMenu(ActionEvent event) {
        SceneAnimation.ChangeScene(rootPane,"../scenes/LibrarianMenu.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SceneAnimation.LoadScene(rootPane);


        genderHashMap = new HashMap<>();
        genreHashMap = new HashMap<>();

        try {
            Client.getClientWriter().writeInt(12);
            Client.getClientWriter().flush();

            genderHashMap = (HashMap<String, Integer>) Client.getClientReader().readObject();
            genreHashMap = (HashMap<String, Integer>) Client.getClientReader().readObject();

            Set<String> genderSet = genderHashMap.keySet();
            int i=0;
            for (Iterator<String> it = genderSet.iterator(); it.hasNext(); ) {
                String key = it.next();
                if(i==0) {
                    genderList.add(new PieChart.Data("Женщины", genderHashMap.get(key)));
                    i++;
                } else {
                    genderList.add(new PieChart.Data("Мужчины", genderHashMap.get(key)));
                }
            }

            Set<String> genreSet = genreHashMap.keySet();
            for (Iterator<String> it = genreSet.iterator(); it.hasNext(); ) {
                String key = it.next();
                genreList.add(new PieChart.Data(key,genreHashMap.get(key)));
            }

            genrePieChart.setData(genreList);
            genderPieChart.setData(genderList);


        } catch (IOException | ClassNotFoundException e) {

        }
    }
}
