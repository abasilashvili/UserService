package com.abasilashvili.user_service.exceptions;

public class NotEnoughOffersException extends RuntimeException {
    public NotEnoughOffersException(String message) {
        super(message);
    }
}
