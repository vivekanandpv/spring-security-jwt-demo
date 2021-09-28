package com.example.springsecurityjwtdemo.controllers;

import com.example.springsecurityjwtdemo.exceptions.GeneralAuthenticationException;
import com.example.springsecurityjwtdemo.exceptions.LoginFailedException;
import com.example.springsecurityjwtdemo.exceptions.RecordNotFoundException;
import com.example.springsecurityjwtdemo.viewmodels.GeneralServerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppExceptionHandlerAdvice {
    @ExceptionHandler(GeneralAuthenticationException.class)
    public ResponseEntity<GeneralServerResponse> handleGeneralAuthenticationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GeneralServerResponse(false, "Authentication failed"));
    }

    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<GeneralServerResponse> handleLoginFailedException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GeneralServerResponse(false, "Could not process login"));
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<GeneralServerResponse> handleRecordNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GeneralServerResponse(false, "Could not find the resource"));
    }
}
