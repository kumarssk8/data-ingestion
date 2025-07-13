package com.assignment.portal26.ingestion.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(InvalidHeaderException.class)
    public ResponseEntity<ApiErrorResponse> headerError(InvalidHeaderException invalidHeaderException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(invalidHeaderException.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        "invalid_header",
                        invalidHeaderException.header));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        log.info("Internal Server Error {0}", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorResponse("Sorry, Something went wrong",
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "internal_error",
                        null
                        ));
    }



}
