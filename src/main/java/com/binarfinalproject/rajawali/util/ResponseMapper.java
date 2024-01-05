package com.binarfinalproject.rajawali.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseMapper {
    public static ResponseEntity<Object> generateResponseSuccess(HttpStatus status,
            String message, Object data) {
        Map<String, Object> mappedRess = new HashMap<String, Object>();
        mappedRess.put("success", true);
        mappedRess.put("message", message);
        mappedRess.put("data", data);

        return new ResponseEntity<Object>(mappedRess, status);
    }

    public static ResponseEntity<Object> generateResponseSuccess(HttpStatus status,
            String message) {
        Map<String, Object> mappedRess = new HashMap<String, Object>();
        mappedRess.put("success", true);
        mappedRess.put("message", message);

        return new ResponseEntity<Object>(mappedRess, status);
    }

    public static ResponseEntity<Object> generateResponseFailed(HttpStatus status,
            String message, Object data) {
        Map<String, Object> mappedRess = new HashMap<String, Object>();
        mappedRess.put("success", false);
        mappedRess.put("status", status.value());
        mappedRess.put("message", message);
        mappedRess.put("data", data);

        return new ResponseEntity<Object>(mappedRess, status);
    }

    public static ResponseEntity<Object> generateResponseFailed(HttpStatus status,
            String message) {
        Map<String, Object> mappedRess = new HashMap<String, Object>();
        mappedRess.put("success", false);
        mappedRess.put("status", status.value());
        mappedRess.put("message", message);

        return new ResponseEntity<Object>(mappedRess, status);
    }
}
