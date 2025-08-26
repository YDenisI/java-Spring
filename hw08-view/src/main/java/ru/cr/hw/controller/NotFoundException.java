package ru.cr.hw.controller;

public class NotFoundException extends RuntimeException {

    NotFoundException() {
        super("Person not found");
    }
}
