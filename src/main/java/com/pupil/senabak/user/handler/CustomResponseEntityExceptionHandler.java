package com.pupil.senabak.user.handler;

import com.pupil.senabak.user.exception.UserUnprocessableException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Customized ResponseEntityExceptionHandler to construct response with more human readable messages.
 */
@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    public CustomResponseEntityExceptionHandler() {
        super();
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleMissingPathVariable(ex, headers, status, request);
    }

    //working
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new LinkedList<>();
        errors.add(ex.getErrorCode());
        errors.add(ex.getPropertyName());
        ErrorDetail errorDetail = new ErrorDetail(status, errors);

        return new ResponseEntity<>(errorDetail, status);
    }

    // different from org.hibernate.exception.ConstraintViolationException !
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {

        List<String> errors = ex.getConstraintViolations().stream()
                .map(e -> e.getPropertyPath().toString().concat(": ").concat(e.getMessage()))
                .collect(Collectors.toCollection(ArrayList::new));

        ErrorDetail errorDetail = new ErrorDetail(HttpStatus.UNPROCESSABLE_ENTITY, errors);

        return super.handleExceptionInternal(ex, errorDetail, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);

    }


    @ExceptionHandler(UserUnprocessableException.class)
    public ResponseEntity<Object> handleUserUnprocessableException(UserUnprocessableException ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());
        ErrorDetail errorDetail = new ErrorDetail(HttpStatus.UNPROCESSABLE_ENTITY, errors);
        return new ResponseEntity<>(errorDetail, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
