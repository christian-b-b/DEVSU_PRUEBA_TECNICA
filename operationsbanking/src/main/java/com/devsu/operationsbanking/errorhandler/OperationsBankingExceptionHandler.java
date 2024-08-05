package com.devsu.operationsbanking.errorhandler;


import com.devsu.operationsbanking.constants.OperationsBankingConstants;
import com.devsu.operationsbanking.utils.ExceptionConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class OperationsBankingExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(HttpStatusCodeException.class)
    protected ResponseEntity<Object> handleHttpRestClient(HttpStatusCodeException ex) {
        OperationsBankingError operationsBankingError = null;
        if (ex.getStatusCode().is4xxClientError()) {
            operationsBankingError = OperationsBankingError.builder().httpStatus(ex.getStatusCode())
                    .code(OperationsBankingConstants.PREFIX_CLIENT_ERROR).build();
        } else if (ex.getStatusCode().is5xxServerError()) {
            operationsBankingError = OperationsBankingError.builder().httpStatus(ex.getStatusCode())
                    .code(OperationsBankingConstants.PREFIX_SERVER_ERROR).build();
        }
        operationsBankingError.setMessage(ex.getMessage());
        log.error("Error HTTP Request Client: {}", ex.getMessage());
        return buildResponseEntity(operationsBankingError);
    }

    @ExceptionHandler(OperationsBankingNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(OperationsBankingNotFoundException ex) {
        log.error(ExceptionConstants.NOT_FOUND.getDescription(), ex.getMessage());
        if (ex.getCode()!=null){
            return buildResponseEntity(OperationsBankingError.builder().httpStatus(HttpStatus.NOT_FOUND)
                    .code(ex.getCode())
                    .message(ex.getMessage()).build());

        }else{
            return buildResponseEntity(OperationsBankingError.builder().httpStatus(HttpStatus.NOT_FOUND)
                    .code(ExceptionConstants.NOT_FOUND.getPrefix() + ExceptionConstants.NOT_FOUND.getCode())
                    .message(ex.getMessage()).build());
        }

    }

    @ExceptionHandler(OperationsBankingGenericServerException.class)
    protected ResponseEntity<Object> handleGenericServerError(OperationsBankingGenericServerException ex) {
        OperationsBankingError operationsBankingError = null;
        if (ex.getCode() != null) {
            operationsBankingError = OperationsBankingError.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .code(ex.getCode()).build();
        } else {
            operationsBankingError = OperationsBankingError.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .code(ExceptionConstants.GENERIC_SERVER.getPrefix() + ExceptionConstants.GENERIC_SERVER.getCode())
                    .build();
        }
        operationsBankingError.setMessage(ex.getMessage());
        log.error(ExceptionConstants.GENERIC_SERVER.getDescription(), ex.getMessage());
        return buildResponseEntity(operationsBankingError);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ExceptionConstants.ARGUMENT_NOT_VALID.getDescription(), ex.getBindingResult().getFieldError().toString());
        return buildResponseEntity(OperationsBankingError.builder().httpStatus(HttpStatus.BAD_REQUEST)
                .code(OperationsBankingConstants.PREFIX_CLIENT_ERROR + OperationsBankingConstants.BAD_REQUEST)
                .message(ex.getBindingResult().getFieldError().toString())
                .subErrors(fillValidationErrorsFrom(ex)).build());
    }

    @ExceptionHandler(OperationsBankingGenericClientException.class)
    protected ResponseEntity<Object> handleGenericClientException(OperationsBankingGenericClientException ex) {
        log.error(ExceptionConstants.GENERIC_CLIENT.getDescription(), ex.getMessage());
        return buildResponseEntity(OperationsBankingError.builder().httpStatus(ex.getHttpStatus())
                .code(OperationsBankingConstants.PREFIX_CLIENT_ERROR).message(ex.getMessage())
                .subErrors(ex.getSubErrors()).build());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleError(Exception ex) {
        log.error("Server Error: {}", ex.getMessage());
        return buildResponseEntity(OperationsBankingError.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .code(OperationsBankingConstants.PREFIX_SERVER_ERROR + OperationsBankingConstants.INTERNAL_SERVER_ERROR)
                .message(ex.getMessage()).build());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        return buildResponseEntity(OperationsBankingError.builder().httpStatus(HttpStatus.BAD_REQUEST)
                .code(OperationsBankingConstants.PREFIX_CLIENT_ERROR + OperationsBankingConstants.BAD_REQUEST)
                .message(ex.getMessage()).build());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        String message = ex.getParameterName() + " parameter is missing";
        log.error("Bad Request: {}", message);
        return buildResponseEntity(OperationsBankingError.builder().httpStatus(HttpStatus.BAD_REQUEST)
                .code(OperationsBankingConstants.PREFIX_CLIENT_ERROR + OperationsBankingConstants.BAD_REQUEST)
                .message(message).build());
    }

    private ResponseEntity<Object> buildResponseEntity(OperationsBankingError operationsBankingError) {
        return new ResponseEntity<>(operationsBankingError, operationsBankingError.getHttpStatus());
    }

    protected List<OperationsBankingSubError> fillValidationErrorsFrom(
            MethodArgumentNotValidException argumentNotValid) {
        List<OperationsBankingSubError> subErrorCollection = new ArrayList<>();
        argumentNotValid.getBindingResult().getFieldErrors().get(0).getRejectedValue();
        argumentNotValid.getBindingResult().getFieldErrors().stream().forEach((objError) -> {
            subErrorCollection.add(OperationsBankingValidationError.builder().object(objError.getObjectName())
                    .field(objError.getField()).rejectValue(objError.getRejectedValue())
                    .message(objError.getDefaultMessage()).build());
        });
        return subErrorCollection;
    }
}
