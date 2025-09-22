package com.example.personal_backend.exception;

public class PersonelNotFoundException extends RuntimeException {
    public PersonelNotFoundException(String message) {
        super(message);
    }
}
