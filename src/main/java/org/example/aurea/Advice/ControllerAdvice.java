package org.example.aurea.Advice;

import org.example.aurea.Api.ApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ControllerAdvice {

    // ApiException
    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<?> ApiException(ApiException e) {

        String message = e.getMessage();
        return ResponseEntity.status(400).body(message);
    }

    // Validation Exception
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getFieldError().getDefaultMessage();
        return ResponseEntity.status(400).body(message);
    }

    // JSON Parse Exception
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<?> HttpMessageNotReadableException(HttpMessageNotReadableException e) {
        String message = "There is an error in the JSON";
        return ResponseEntity.status(400).body(message);
    }

    // Type Mismatch Exception
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> MethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String message = e.getMessage();
        return ResponseEntity.status(400).body(message);
    }

    // Method Not Allowed
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        String message = e.getMessage();
        return ResponseEntity.status(405).body(message);
    }
}
