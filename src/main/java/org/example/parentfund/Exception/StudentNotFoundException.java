package org.example.parentfund.Exception;

public class StudentNotFoundException extends RuntimeException{

    public StudentNotFoundException(String message){
        super("Student not found: "+message);
    }
}
