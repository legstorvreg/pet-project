package ru.streltsova.pet_project.exceptions;

public class CurrencyNotFoundException extends RuntimeException {
    public CurrencyNotFoundException(String msg) {
        super(msg);
    }
}
