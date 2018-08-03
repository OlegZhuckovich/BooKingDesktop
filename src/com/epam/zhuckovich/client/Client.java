package com.epam.zhuckovich.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.ImageIcon;
import java.io.*;
import java.net.Socket;
import java.net.URL;

public class Client extends Application {

    private static ObjectInputStream clientReader;
    private static ObjectOutputStream clientWriter;
    private String BooKingLogo = "resources/BooKingLogo.png";
    private static Socket serverSocket;

    @Override
    public void start(Stage mainStage) throws Exception{
        //only for Mac OS
        URL dockIconURL = Client.class.getResource(BooKingLogo);
        java.awt.Image dockIcon = new ImageIcon(dockIconURL).getImage();
        com.apple.eawt.Application.getApplication().setDockIconImage(dockIcon);
        ////////////////////////////////////////////////////////

        serverSocket = new Socket("localhost",4242);
        //clientReader = new ObjectInputStream(serverSocket.getInputStream());
        clientWriter = new ObjectOutputStream(serverSocket.getOutputStream());
        clientReader = new ObjectInputStream(serverSocket.getInputStream());
        Parent root = FXMLLoader.load(getClass().getResource("scenes/StartPage.fxml"));
        mainStage.setTitle("BooKing - King library for everyone");
        mainStage.getIcons().add(new Image(getClass().getResourceAsStream(BooKingLogo)));
        mainStage.setScene(new Scene(root, 900, 560));
        mainStage.setResizable(false);
        mainStage.show();
    }

    @Override
    public void stop() throws IOException {
        clientWriter.writeInt(-1);
        clientWriter.flush();
    }

    public static ObjectInputStream getClientReader(){
        return clientReader;
    }

    public static ObjectOutputStream getClientWriter(){
        return clientWriter;
    }

    public static Socket getServerSocket(){
        return serverSocket;
    }

    public static void main(String[] args) {
        launch(args);
    }
}