package com.finderai.chat.backend.exceptions;

public class AIServiceException extends RuntimeException {
    public AIServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
