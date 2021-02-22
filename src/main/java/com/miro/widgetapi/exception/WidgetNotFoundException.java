package com.miro.widgetapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class WidgetNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 325784728563803072L;

    public WidgetNotFoundException(String message) {
        super(message);
    }

}

