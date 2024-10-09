package br.com.elibrary.application;

import br.com.elibrary.application.dto.response.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponse error = toError(ex.getBindingResult());
        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

    ErrorResponse toError(BindingResult bindingResult) {
        ErrorResponse errorResponse = new ErrorResponse();
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors()
                    .forEach(objectError -> {
                        var messageCode = objectError.getDefaultMessage();
                        errorResponse.addError(((FieldError) objectError).getField(), messageCode, messageCode);
                    });
        }
        return errorResponse;
    }
}