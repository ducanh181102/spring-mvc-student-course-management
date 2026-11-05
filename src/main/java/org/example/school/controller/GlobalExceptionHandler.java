package org.example.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBusinessError(
            IllegalArgumentException exception,
            Model model
    ) {
        model.addAttribute("errorMessage", exception.getMessage());
        model.addAttribute("breadcrumb", "Thông báo lỗi");
        model.addAttribute("activeNav", "");
        return "error/error";
    }
}
