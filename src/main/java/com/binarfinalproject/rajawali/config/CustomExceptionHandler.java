package com.binarfinalproject.rajawali.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.binarfinalproject.rajawali.util.ResponseMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

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

    @ExceptionHandler(value = { MaxUploadSizeExceededException.class })
    public ResponseEntity<Object> handleMaxUploadSizeExceededException(HttpServletRequest request,
            HttpServletResponse response,
            MaxUploadSizeExceededException exception) throws IOException {

        return ResponseMapper.generateResponseFailed(HttpStatus.BAD_REQUEST, "Max File 1 mb");
    }

    @ExceptionHandler(value = { SignatureException.class })
    public ResponseEntity<Object> handleUserServiceException(SignatureException ex, WebRequest request) {
        return ResponseMapper.generateResponseFailed(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(value = { UnsupportedJwtException.class })
    public ResponseEntity<Object> handleUnsupportedJwtTokenException(UnsupportedJwtException ex, WebRequest request) {
        return ResponseMapper.generateResponseFailed(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(value = { ExpiredJwtException.class })
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        return ResponseMapper.generateResponseFailed(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(value = { MalformedJwtException.class })
    public ResponseEntity<Object> handleMalformedJwtException(MalformedJwtException ex, WebRequest request) {
        return ResponseMapper.generateResponseFailed(HttpStatus.FORBIDDEN,
                "Your JWT token is invalid : " + ex.getMessage());
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    public ResponseEntity<Object> handleIllegalArgumentException(MalformedJwtException ex, WebRequest request) {
        return ResponseMapper.generateResponseFailed(HttpStatus.FORBIDDEN,
                "JWT claims string is empty : " + ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> accessDeniedException(AccessDeniedException ex, WebRequest request)
            throws AccessDeniedException {
        return ResponseMapper.generateResponseFailed(HttpStatus.FORBIDDEN,
                "You don't have the required permission to access this resource " + ex.getMessage());
    }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest request) {
        log.error("Error : " + ex);
        return ResponseMapper.generateResponseFailed(HttpStatus.INTERNAL_SERVER_ERROR,
                "There is something wrong : " + ex.getMessage());
    }

}
