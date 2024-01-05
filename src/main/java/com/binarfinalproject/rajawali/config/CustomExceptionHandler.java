package com.binarfinalproject.rajawali.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.binarfinalproject.rajawali.util.ResponseMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
class InputError {
    public String field;
    public String message;
}

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
            WebRequest request) {
        List<InputError> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            InputError errObj = new InputError(fieldName, message);
            errors.add(errObj);
        });
        return ResponseMapper.generateResponseFailed(HttpStatus.BAD_REQUEST,
                "Value of some fields doesn't match the requirements.",
                errors);
    }

    @ExceptionHandler(value = { TypeMismatchException.class })
    public ResponseEntity<Object> handleTypeMismatchException(TypeMismatchException ex, WebRequest request) {
        return ResponseMapper.generateResponseFailed(HttpStatus.BAD_REQUEST,
                "Value of params '" + ex.getPropertyName() + "'' should be "
                        + ex.getRequiredType().getSimpleName());
    }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest request) {
        log.error("Error : " + ex);
        return ResponseMapper.generateResponseFailed(HttpStatus.INTERNAL_SERVER_ERROR,
                "There is something wrong : " + ex.getMessage());
    }

}
