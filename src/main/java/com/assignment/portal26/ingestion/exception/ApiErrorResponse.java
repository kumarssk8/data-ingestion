package com.assignment.portal26.ingestion.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ApiErrorResponse {
    String msg;
    int httpStatusCode;
    String errorCode;
    String attribute;
}
