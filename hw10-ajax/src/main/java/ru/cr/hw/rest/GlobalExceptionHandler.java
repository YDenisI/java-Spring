package ru.cr.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.cr.hw.rest.exceptions.NotFoundException;

@RequiredArgsConstructor
@RestControllerAdvice  // Автоматически добавляет @ResponseBody для JSON
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        String errorText = messageSource.getMessage("book-not-found-error", null,
                LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"" + errorText + "\"}");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        String errorText = messageSource.getMessage("general-error", new Object[]{ex.getMessage()},
                "Internal server error: " + ex.getMessage(), LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"" + errorText + "\"}");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(Exception ex) {
        String errorText = messageSource.getMessage("general-error", new Object[]{ex.getMessage()},
                "Bad Request: " + ex.getMessage(), LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"" + errorText + "\"}");
    }
}