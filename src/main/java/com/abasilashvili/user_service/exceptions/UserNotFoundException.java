package com.abasilashvili.user_service.exceptions;

import java.io.IOException;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
