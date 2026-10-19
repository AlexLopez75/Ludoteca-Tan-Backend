package com.ccsw.tutorial.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String,String>> BandleBusinessException(BusinessException ex) {
        System.out.println("HANDLER EJECUTADO");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "code", ex.getCode(),
                "message", ex.getMessage()
        ));
    }
}
