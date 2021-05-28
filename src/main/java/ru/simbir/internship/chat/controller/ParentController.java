package ru.simbir.internship.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.simbir.internship.chat.exception.NotFoundException;

import java.util.HashMap;

public class ParentController {
    static final String NULL_ID_REQUEST_EXCEPTION = "Id cannot be null";
    static final String NULL_CREATE_OBJECT_REQUEST_EXCEPTION = "Instance that must be created not found in request body";
    static final String NULL_UPDATE_OBJECT_REQUEST_EXCEPTION = "Instance that must be updated not found in request body";
    static final String NULL_PATCH_OBJECT_REQUEST_EXCEPTION = "Instance that must be patch not found in request body";

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public HashMap<String, String> handleNotFoundExceptions(Exception ex) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("error", ex.getClass().getSimpleName());
        return response;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public HashMap<String, String> handleIntervalServerExceptions(Exception ex) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("error", ex.getClass().getSimpleName());
        return response;
    }
}
