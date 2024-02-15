package com.carbon.deliverytracker.exception;

import io.r2dbc.spi.R2dbcException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;

import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<APIError> handleBindException(WebExchangeBindException ex, ServerWebExchange request) {
        BindingResult bindingResult = ex.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().orElse(null);
        String defaultMessage = (objectError != null) ? objectError.getDefaultMessage() : "Validation failed. Bad Request";
        APIError error = getAPIError(defaultMessage, request);
        return ResponseEntity.badRequest().body(error);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<APIError> handleConstraintViolationException(ConstraintViolationException ex,ServerWebExchange request) {
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        StringBuilder messages = new StringBuilder();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            messages.append(constraintViolation.getMessage()).append(" ");
        }
        APIError apiError = getAPIError(messages.toString(), request);
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIError> handleAPIException(APIException e, ServerWebExchange request) {
        return ResponseEntity.status(HttpStatusCode.valueOf(Integer.parseInt(e.getStatus()))).body(getAPIError(e, request));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<APIError> handleBindException(BindException ex, ServerWebExchange request) {
        BindingResult bindingResult = ex.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().orElse(null);
        String defaultMessage = (objectError != null) ? objectError.getDefaultMessage() : "Validation failed. Bad Request";
        APIError error = getAPIError(defaultMessage, request);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<APIError> handleIllegalStateException(IllegalStateException e, ServerWebExchange request) {
        return ResponseEntity.badRequest().body(getAPIError(e.getMessage(), request));
    }

    @ExceptionHandler(R2dbcException.class)
    public ResponseEntity<APIError> handleR2dbcException(R2dbcException e, ServerWebExchange request) {
        return ResponseEntity.internalServerError().body(getAPIError(e.getMessage(), request));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<APIError> handleDataAccessException(DataAccessException e, ServerWebExchange request) {
        return ResponseEntity.internalServerError().body(getAPIError(e.getMessage(), request));
    }

    @ExceptionHandler(DataRetrievalFailureException.class)
    public ResponseEntity<APIError> handleDataRetrievalFailureException(DataRetrievalFailureException e, ServerWebExchange request) {
        return ResponseEntity.internalServerError().body(getAPIError(e.getMessage(), request));
    }

    private APIError getAPIError(APIException e, ServerWebExchange request) {
        String error = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
        String path = request.getRequest().getURI().getPath();
        HttpMethod method = request.getRequest().getMethod();
        return APIError.builder()
                .error(error)
                .message(e.getMessage())
                .path(path)
                .status(e.getStatus())
                .method(method)
                .build();
    }


    private APIError getAPIError(String message, ServerWebExchange request) {
        String path = request.getRequest().getURI().getPath();
        HttpMethod method = request.getRequest().getMethod();
        String status = HttpStatus.BAD_REQUEST.toString();
        return APIError.builder()
                .error(message)
                .message(message)
                .path(path)
                .status(status)
                .method(method)
                .build();
    }

}
