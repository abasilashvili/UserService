package com.abasilashvili.user_service.exceptions;

public class CooldownPeriodException extends RuntimeException{
    public CooldownPeriodException(String message) {
        super(message);
    }
}
