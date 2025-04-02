package com.abasilashvili.user_service.exceptions;

public class NoSuchMentorshipRequest extends RuntimeException{
    public NoSuchMentorshipRequest(String message) {
        super(message);
    }
}
