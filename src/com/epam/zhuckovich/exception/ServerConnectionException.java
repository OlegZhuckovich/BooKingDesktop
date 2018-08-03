package com.epam.zhuckovich.exception;

import java.io.IOException;

public class ServerConnectionException extends IOException{

    public ServerConnectionException(){
        super();
    }

    public ServerConnectionException(String message){
        super(message);
    }

    public ServerConnectionException(Throwable e){
        super(e);
    }

    public ServerConnectionException(String message, Throwable e){
        super(message,e);
    }
}
