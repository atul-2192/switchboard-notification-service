package com.SwitchBoard.NotificationService.Exception;


public class EmailSendException extends RuntimeException {
    public EmailSendException(String message, Throwable cause) {
        super(message, cause);
    }
}

