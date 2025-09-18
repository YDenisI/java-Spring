package ru.cr.hw.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.cr.hw.dto.ErrorDto;
import ru.cr.hw.rest.exceptions.NotFoundException;

@RequiredArgsConstructor
@RestControllerAdvice  // Автоматически добавляет @ResponseBody для JSON
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final MessageSource messageSource;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundException(NotFoundException ex) {
        logger.error("NotFoundException occurred: ", ex);
        String errorText = messageSource.getMessage("book-not-found-error", null,
                LocaleContextHolder.getLocale());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto(errorText, null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGeneralException(Exception ex) {
        logger.error("General Exception occurred: ", ex);
        String errorText = messageSource.getMessage("general-error", new Object[]{ex.getMessage()},
                "Internal server error: " + ex.getMessage(), LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto(errorText, ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleIllegalArgumentException(Exception ex) {
        logger.error("IllegalArgumentException occurred: ", ex);
        String errorText = messageSource.getMessage("general-error", new Object[]{ex.getMessage()},
                "Bad Request: " + ex.getMessage(), LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto(errorText, ex.getMessage()));
    }
}