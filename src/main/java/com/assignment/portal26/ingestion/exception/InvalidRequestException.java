package com.assignment.portal26.ingestion.exception;

import lombok.Getter;

@Getter
public class InvalidRequestException extends  RuntimeException {

    String msg;
    String attribute;
    public InvalidRequestException(String msg, String attribute){
        this.msg = msg;
        this.attribute = attribute;
    }

}
