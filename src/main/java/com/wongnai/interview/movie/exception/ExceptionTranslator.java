package com.wongnai.interview.movie.exception;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import java.util.Date;

@ControllerAdvice
public class ExceptionTranslator {

    private static final String DEFAULT_MESSAGE = "Please contact administrator";

    @ExceptionHandler
    public ResponseEntity<?> createException(Exception ex, WebRequest request) {
        if (ex.getCause() instanceof InternalServerErrorException) {
            handleInternalServerError((InternalServerErrorException) ex, request);
        }

        return handleDefaultError(ex, request);
    }

    @ExceptionHandler({InternalServerErrorException.class})
    public ResponseEntity<ApiError> handleInternalServerError(InternalServerErrorException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);

        ApiError body = new ApiError(
            new Date(),
            ex.getEntityName(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            ex.getErrorKey(),
            ex.getMessage()
        );

        return new ResponseEntity<>(body, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({BadRequestAlertException.class})
    public ResponseEntity<ApiError> handleBadRequest(BadRequestAlertException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        ApiError body = new ApiError();

        body.setTimestamp(new Date());
        body.setEntityName(ex.getEntityName());
        body.setTitle(ex.getErrorKey());
        body.setStatus(HttpStatus.BAD_REQUEST.value());
        body.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        body.setMessage(ex.getMessage());

        return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<DefaultError> handleDefaultError(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        DefaultError body = new DefaultError();

        body.setTimestamp(new Date());
        body.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        body.setMessage(StringUtils.isNotBlank(ex.getMessage()) ? ex.getMessage() : DEFAULT_MESSAGE);

        return new ResponseEntity<>(body, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
