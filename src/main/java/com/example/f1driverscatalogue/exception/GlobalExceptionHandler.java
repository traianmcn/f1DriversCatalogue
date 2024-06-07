package com.example.f1driverscatalogue.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ContactNotFoundException.class)
    public ResponseEntity<ApiError> handleContactNotFound(ContactNotFoundException e, HttpServletRequest request) {
        StringBuilder requestUrl = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString(); // ==> result null
        String path = requestUrl.append('?').append(queryString).toString();
        ApiError apiError = new ApiError(400, e.getMessage(), path);
        return new ResponseEntity<ApiError>(apiError, HttpStatus.BAD_REQUEST);
    }
}
