package ru.cr.hw.rest.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("message");
    }
}
