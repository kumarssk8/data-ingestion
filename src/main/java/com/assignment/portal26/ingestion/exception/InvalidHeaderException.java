package com.assignment.portal26.ingestion.exception;

public class InvalidHeaderException extends RuntimeException {

    String header;
    String msg;

    public InvalidHeaderException(String header, String msg){
        this.header = header;
        this.msg = msg;
    }

    public String getMessage(){
        return msg;
    }

}
