package com.rs.elasticsearchservice.config;

import com.rs.elasticsearchservice.exception.NotFoundException;
import com.rs.elasticsearchservice.util.ErrorObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.EmptyStackException;

@ControllerAdvice
public class ErrorHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    ErrorObject handleNotFoundException(HttpServletRequest request, NotFoundException ex) {
        return new ErrorObject(request, ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EmptyStackException.class)
    @ResponseBody
    ErrorObject handleNotFoundException(HttpServletRequest request, EmptyStackException ex) {
        return new ErrorObject(request, "You haven't provided a query.", HttpStatus.BAD_REQUEST);
    }
}