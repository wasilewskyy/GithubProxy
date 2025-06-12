package com.wasilewskyy.github_proxy.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {

    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
}
