package com.abasilashvili.user_service.exceptions;

public class SkillDoesntExistException extends RuntimeException {
    public SkillDoesntExistException(String message) {
        super(message);
    }
}
