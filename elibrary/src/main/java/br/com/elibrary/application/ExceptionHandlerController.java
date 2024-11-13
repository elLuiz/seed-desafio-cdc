package br.com.elibrary.application;

import br.com.elibrary.model.exception.DomainException;
import br.com.elibrary.model.validation.Error;
import br.com.elibrary.service.exception.EntityNotFound;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Error error = toError(ex.getBindingResult());
        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

    Error toError(BindingResult bindingResult) {
        Error error = new Error();
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors()
                    .forEach(objectError -> {
                        var messageCode = objectError.getDefaultMessage();
                        error.addError(((FieldError) objectError).getField(), messageCode, messageCode);
                    });
        }
        return error;
    }

    @ExceptionHandler(EntityNotFound.class)
    protected ResponseEntity<Error> handle(EntityNotFound entityNotFound) {
        Error error = new Error();
        error.addError(entityNotFound.getEntity(), entityNotFound.getMessage(), entityNotFound.getMessage());
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(error);
    }

    @ExceptionHandler(value = {EmptyResultDataAccessException.class})
    protected ResponseEntity<Error> handle(EmptyResultDataAccessException emptyResultDataAccessException) {
        logger.warn("Entity not found", emptyResultDataAccessException);
        Error body = new Error();
        body.addError(null, "entity.not.found", "entity.not.found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(value = DomainException.class)
    protected ResponseEntity<Error> handleDomainException(DomainException domainException) {
        logger.warn("Domain exception", domainException);
        Error error = new Error();
        error.addError(null, domainException.getMessage(), domainException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}