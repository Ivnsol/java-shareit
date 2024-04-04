package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ValidationException;
import java.util.Map;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {

    @Test
    void testHandleValidationException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ValidationException ex = new ValidationException("Validation error");
        ResponseEntity<Map<String, String>> responseEntity = handler.handleValidationException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Error", responseEntity.getBody().get("error"));
    }

    @Test
    void testHandleNoSuchElementException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        NoSuchElementException ex = new NoSuchElementException("No such element");
        NoSuchElementException result = handler.handleResponseStatusException(ex);
        assertEquals("No such element", result.getMessage());
    }

    @Test
    void testHandleEntityNotFoundException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        handler.handleEntityNotFoundException();
    }

    @Test
    void testHandlerIllegalStateException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        handler.handlerIllegalStateException();
    }

    @Test
    void testHandleIncorrectState() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        IllegalArgumentException ex = new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS");
        ResponseEntity<Map<String, String>> responseEntity = handler.handleIncorrectState(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Unknown state: UNSUPPORTED_STATUS", responseEntity.getBody().get("error"));
    }
}
