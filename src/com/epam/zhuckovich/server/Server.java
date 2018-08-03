package com.epam.zhuckovich.server;

import com.epam.zhuckovich.exception.ServerConnectionException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Server {

    private final static Logger LOGGER = LogManager.getLogger(Server.class);
    private static ServerSocket bookingServerSocket;
    public static int clientCounter = 0;

    public static void main(String[] args) throws ServerConnectionException, IOException, SQLException {
        try {
            bookingServerSocket = new ServerSocket(4242);
            LOGGER.log(Level.INFO,"ServerSocket was initialized");
            while (true){
                Socket client = bookingServerSocket.accept();
                clientCounter++;
                LOGGER.log(Level.INFO, "Client " + clientCounter + " is connected");
                ServerThread clientThread = new ServerThread(client);
                clientThread.start();
            }
        } catch (IOException e) {
            throw new ServerConnectionException("ServerSocket is not initialized",e);
        } finally {
                bookingServerSocket.close();
        }
    }
}
