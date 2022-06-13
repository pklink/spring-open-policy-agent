package net.einself.opa.config;

import net.einself.opa.exception.OpaAssertionError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OpaExceptionHandler {

    @ExceptionHandler(OpaAssertionError.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String assertionError() {
        return ":-(";
    }

}
