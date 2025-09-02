package ru.cr.hw.controller;

public class NotFoundException extends RuntimeException {

    NotFoundException() {
        super("Book not found");
    }
}
