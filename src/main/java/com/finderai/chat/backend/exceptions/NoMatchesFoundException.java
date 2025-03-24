package com.finderai.chat.backend.exceptions;

public class NoMatchesFoundException extends RuntimeException {
    public NoMatchesFoundException(String message) {
        super(message);
    }
}
