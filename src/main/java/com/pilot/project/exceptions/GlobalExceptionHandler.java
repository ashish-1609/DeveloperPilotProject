package com.pilot.project.exceptions;

import com.pilot.project.payloads.ApiResponse;
import org.springframework.batch.item.file.transform.IncorrectTokenCountException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        return new ResponseEntity<>(
                new ApiResponse(
                        ex.getMessage().toUpperCase())
                , HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> genericExceptionHandler(Exception ex) {
        return new ResponseEntity<>(new ApiResponse(ex.getMessage().toUpperCase()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
        return new ResponseEntity<>(getErrors(errors),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IncorrectTokenCountException.class)
    public ResponseEntity<ApiResponse> incorrectTokenCountExceptionHandler(IncorrectTokenCountException ex) {
        return new ResponseEntity<>(new ApiResponse(ex.getMessage().toUpperCase()), HttpStatus.BAD_REQUEST);
    }
    public Map<String, List<String>> getErrors(List<String> fieldErrors) {
        Map<String, List<String>> errors = new HashMap<>();
        errors.put("errors", fieldErrors);
        return errors;
    }
}
