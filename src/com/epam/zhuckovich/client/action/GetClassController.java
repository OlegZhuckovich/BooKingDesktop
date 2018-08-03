package com.epam.zhuckovich.client.action;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.File;
import java.io.IOException;

public class GetClassController<T> {

    private FXMLLoader loader;
    private Parent root;
    private T controller;

    public T getController(String fxmlName) throws IOException {
        loader = new FXMLLoader();
        loader.setLocation(new File("/Users/Oleg/IdeaProjects/BooKing/src/com/epam/zhuckovich/client/scenes/"+fxmlName).toURI().toURL());
        root = (Parent) loader.load();
        controller = loader.getController();
        return controller;
    }
}