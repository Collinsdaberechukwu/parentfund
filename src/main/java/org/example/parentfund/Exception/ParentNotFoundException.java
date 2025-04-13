package org.example.parentfund.Exception;

public class ParentNotFoundException extends RuntimeException {

    public ParentNotFoundException(String message) {
        super("Parent not found: " + message);
    }


}
