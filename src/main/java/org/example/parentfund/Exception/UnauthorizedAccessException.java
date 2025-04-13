package org.example.parentfund.Exception;

public class UnauthorizedAccessException extends RuntimeException{

    public UnauthorizedAccessException(String message){
        super("Unauthorized access: "+message);
    }
}
