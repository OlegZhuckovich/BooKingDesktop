package com.epam.zhuckovich.client.action;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class SceneAnimation {

    private static final Logger LOGGER = LogManager.getLogger(SceneAnimation.class);

    public static void ChangeScene(Pane scenePane, String fxmlPath){
        FadeTransition sceneAnimation = new FadeTransition();
        sceneAnimation.setDuration(Duration.millis(600));
        sceneAnimation.setNode(scenePane);
        sceneAnimation.setFromValue(1);
        sceneAnimation.setToValue(0);
        sceneAnimation.setOnFinished(event -> {
            try { new SceneAnimation().LoadNextScene(scenePane,fxmlPath);
            } catch (IOException e) { LOGGER.log(Level.ERROR,"Scene was changing too fast");}
        });
        sceneAnimation.play();
    }

    private void LoadNextScene(Pane scenePane, String fxmlPath) throws IOException {
        Parent fxmlRoot = (AnchorPane) FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene newScene = new Scene(fxmlRoot);
        Stage mainStage = (Stage) scenePane.getScene().getWindow();
        mainStage.setScene(newScene);
    }

    public static void LoadScene(Pane scenePane){
        scenePane.setOpacity(0);
        FadeTransition sceneAnimation = new FadeTransition();
        sceneAnimation.setDuration(Duration.millis(600));
        sceneAnimation.setNode(scenePane);
        sceneAnimation.setFromValue(0);
        sceneAnimation.setToValue(1);
        sceneAnimation.play();
    }
}