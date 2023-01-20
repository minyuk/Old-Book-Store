package com.personal.oldbookstore.application.controller;

import com.personal.oldbookstore.util.error.ErrorResponse;
import com.personal.oldbookstore.util.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity customExceptionHandler(CustomException exception){
        return ResponseEntity
                .status(exception.getResponse().getHttpStatus())
                .body(exception.getResponse());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity constraintViolationExceptionHandler(ConstraintViolationException exception){
        String cause = "";
        String message = "";
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            cause = constraintViolation.getPropertyPath().toString();
            message = constraintViolation.getMessage();
        }
        cause = cause.split("\\.")[1];

        ErrorResponse errorResponse = ErrorResponse.builder()
                .cause(cause)
                .code("parameter.validated")
                .message(message)
                .httpStatus(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception){
        System.out.println("==========================");
        BindingResult bindingResult = exception.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        List<ErrorResponse> errorResponseList = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .cause(fieldError.getField())
                    .code(fieldError.getCode())
                    .message(fieldError.getDefaultMessage())
                    .httpStatus(HttpStatus.BAD_REQUEST).build();
            errorResponseList.add(errorResponse);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseList);
    }
}
