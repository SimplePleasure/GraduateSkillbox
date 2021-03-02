package com.graduate.exceptionHandler;

import com.graduate.base.IResponse;
import com.graduate.exceptionHandler.exceptions.ComponentNotFoundException;
import com.graduate.exceptionHandler.exceptions.PostNotFoundException;
import com.graduate.exceptionHandler.exceptions.UnknownInputStatusException;
import com.graduate.exceptionHandler.exceptions.UserNotAuthException;
import com.graduate.response.ActionResultTemplate;
import com.graduate.response.ActionResultTemplateWithErrors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({UserNotAuthException.class, MultipartException.class, PostNotFoundException.class,
            UnknownInputStatusException.class, ComponentNotFoundException.class})
    public ResponseEntity<IResponse> handleException (Exception ex, WebRequest request) {

        HttpHeaders headers = new HttpHeaders();

        if (ex instanceof UserNotAuthException) {
            UserNotAuthException e = (UserNotAuthException) ex;
            HttpStatus status = HttpStatus.UNAUTHORIZED;
            return handleUserNotAuth(e, headers, status, request);
        } else if (ex instanceof MultipartException) {
            MultipartException e = (MultipartException) ex;
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleMultipartException(e, headers, status, request);
        } else if (ex instanceof PostNotFoundException) {
            PostNotFoundException e = (PostNotFoundException) ex;
            HttpStatus status = HttpStatus.NOT_FOUND;
            return mainHandler(e, null, headers, status, request);
        } else if (ex instanceof UnknownInputStatusException) {
            UnknownInputStatusException e = (UnknownInputStatusException) ex;
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleUnknownInputStatusException(e, headers, status, request);
        } else if (ex instanceof ComponentNotFoundException) {
            ComponentNotFoundException e = (ComponentNotFoundException) ex;
            HttpStatus status = HttpStatus.NOT_FOUND;
            return handleComponentNotFoundException(e, headers, status, request);
        } else {
            ActionResultTemplateWithErrors err = new ActionResultTemplateWithErrors(false);
            err.addError("server", "unprocessed exception");
            return mainHandler(ex, err, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }

    private ResponseEntity<IResponse> handleComponentNotFoundException (ComponentNotFoundException ex, HttpHeaders headers,
                                                                         HttpStatus status, WebRequest request) {
        ActionResultTemplateWithErrors result = new ActionResultTemplateWithErrors(false);
        result.addError("component", ex.getMessage());
        return mainHandler(ex, result, headers, status, request);
    }

    private ResponseEntity<IResponse> handleUnknownInputStatusException (UnknownInputStatusException ex, HttpHeaders headers,
    HttpStatus status, WebRequest request) {
        ActionResultTemplateWithErrors result = new ActionResultTemplateWithErrors(false);
        result.addError("status", ex.getMessage());
        return mainHandler(ex, result, headers, status, request);
    }

    private ResponseEntity<IResponse> handleMultipartException (MultipartException ex, HttpHeaders headers,
                                                                    HttpStatus status, WebRequest request) {
        ActionResultTemplateWithErrors result = new ActionResultTemplateWithErrors(false);
        result.addError("image", ex.getLocalizedMessage());
        return mainHandler(ex, result, headers, status, request);
    }

    private ResponseEntity<IResponse> handleUserNotAuth (UserNotAuthException ex, HttpHeaders headers,
                                                                    HttpStatus status, WebRequest request) {
        ActionResultTemplate result = new ActionResultTemplate(false);
        return mainHandler(ex, result, headers, status, request);
    }

    private ResponseEntity<IResponse> mainHandler (Exception ex, IResponse body, HttpHeaders headers,
                                                  HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(body, headers, status);
    }
}
