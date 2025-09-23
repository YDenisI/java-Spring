package ru.cr.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handeNotFoundException(NotFoundException ex) {
        String errorText = messageSource.getMessage("Not-found-error", null,
                LocaleContextHolder.getLocale());
        ModelAndView mav = new ModelAndView("customError", "errorText", errorText);
        mav.setStatus(HttpStatusCode.valueOf(404));
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneralException(Exception ex) {
        String errorText = messageSource.getMessage("general-error", new Object[]{ex.getMessage()},
                "Internal server error: " + ex.getMessage(), LocaleContextHolder.getLocale());
        ModelAndView mav = new ModelAndView("customError", "errorText", errorText);
        mav.setStatus(HttpStatusCode.valueOf(500));
        return mav;
    }
}
