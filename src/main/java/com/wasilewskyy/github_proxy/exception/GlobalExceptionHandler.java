package com.wasilewskyy.github_proxy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RepositoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleGithubRepositoryNotFound(RepositoryNotFoundException ex) {
        return new ErrorMessage(
                404,
                "Not Found",
                ex.getMessage(),
                LocalDateTime.now()
        );
    }
}
