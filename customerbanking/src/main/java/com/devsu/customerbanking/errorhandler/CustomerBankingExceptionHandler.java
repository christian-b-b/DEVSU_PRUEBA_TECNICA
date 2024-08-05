package com.devsu.customerbanking.errorhandler;

import com.devsu.customerbanking.utils.CustomerBankingConstants;
import com.devsu.customerbanking.utils.ExceptionConstants;
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
public class CustomerBankingExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(HttpStatusCodeException.class)
    protected ResponseEntity<Object> handleHttpRestClient(HttpStatusCodeException ex) {
        CustomerBankingError customerBankingError = null;
        if (ex.getStatusCode().is4xxClientError()) {
            customerBankingError = CustomerBankingError.builder().httpStatus(ex.getStatusCode())
                    .code(CustomerBankingConstants.PREFIX_CLIENT_ERROR).build();
        } else if (ex.getStatusCode().is5xxServerError()) {
            customerBankingError = CustomerBankingError.builder().httpStatus(ex.getStatusCode())
                    .code(CustomerBankingConstants.PREFIX_SERVER_ERROR).build();
        }
        customerBankingError.setMessage(ex.getMessage());
        log.error("Error HTTP Request Client: {}", ex.getMessage());
        return buildResponseEntity(customerBankingError);
    }

    @ExceptionHandler(CustomerBankingNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(CustomerBankingNotFoundException ex) {
        log.error(ExceptionConstants.NOT_FOUND.getDescription(), ex.getMessage());
        if (ex.getCode()!=null){
            return buildResponseEntity(CustomerBankingError.builder().httpStatus(HttpStatus.NOT_FOUND)
                    .code(ex.getCode())
                    .message(ex.getMessage()).build());

        }else{
            return buildResponseEntity(CustomerBankingError.builder().httpStatus(HttpStatus.NOT_FOUND)
                    .code(ExceptionConstants.NOT_FOUND.getPrefix() + ExceptionConstants.NOT_FOUND.getCode())
                    .message(ex.getMessage()).build());
        }

    }


    @ExceptionHandler(CustomerBankingGenericServerException.class)
    protected ResponseEntity<Object> handleGenericServerError(CustomerBankingGenericServerException ex) {
        CustomerBankingError customerBankingError = null;
        if (ex.getCode() != null) {
            customerBankingError = CustomerBankingError.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .code(ex.getCode()).build();
        } else {
            customerBankingError = CustomerBankingError.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .code(CustomerBankingConstants.PREFIX_SERVER_ERROR + CustomerBankingConstants.INTERNAL_SERVER_ERROR)
                    .build();
        }
        customerBankingError.setMessage(ex.getMessage());
        log.error("Internal Server Error: {}", ex.getMessage());
        return buildResponseEntity(customerBankingError);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ExceptionConstants.ARGUMENT_NOT_VALID.getDescription(), ex.getBindingResult().getFieldError().toString());
        return buildResponseEntity(CustomerBankingError.builder().httpStatus(HttpStatus.BAD_REQUEST)
                .code(ExceptionConstants.ARGUMENT_NOT_VALID.getPrefix() + ExceptionConstants.ARGUMENT_NOT_VALID.getCode())
                .message(ExceptionConstants.ARGUMENT_NOT_VALID.getMessage())
                .subErrors(fillValidationErrorsFrom(ex)).build());
    }

    @ExceptionHandler(CustomerBankingGenericClientException.class)
    protected ResponseEntity<Object> handleGenericClientException(CustomerBankingGenericClientException ex) {
        log.error(ExceptionConstants.GENERIC_CLIENT.getDescription(), ex.getMessage());
        return buildResponseEntity(CustomerBankingError.builder().httpStatus(ex.getHttpStatus())
                .code(ExceptionConstants.GENERIC_CLIENT.getPrefix()).message(ex.getMessage())
                .subErrors(ex.getSubErrors()).build());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleError(Exception ex) {
        log.error("Server Error: {}", ex.getMessage());
        return buildResponseEntity(CustomerBankingError.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .code(CustomerBankingConstants.PREFIX_SERVER_ERROR + CustomerBankingConstants.INTERNAL_SERVER_ERROR)
                .message(ex.getMessage()).build());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        return buildResponseEntity(CustomerBankingError.builder().httpStatus(HttpStatus.BAD_REQUEST)
                .code(CustomerBankingConstants.PREFIX_CLIENT_ERROR + CustomerBankingConstants.BAD_REQUEST)
                .message(ex.getMessage()).build());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        String message = ex.getParameterName() + " parameter is missing";
        log.error("Bad Request: {}", message);
        return buildResponseEntity(CustomerBankingError.builder().httpStatus(HttpStatus.BAD_REQUEST)
                .code(CustomerBankingConstants.PREFIX_CLIENT_ERROR + CustomerBankingConstants.BAD_REQUEST)
                .message(message).build());
    }

    private ResponseEntity<Object> buildResponseEntity(CustomerBankingError customerBankingError) {
        return new ResponseEntity<>(customerBankingError, customerBankingError.getHttpStatus());
    }

    protected List<CustomerBankingSubError> fillValidationErrorsFrom(
            MethodArgumentNotValidException argumentNotValid) {
        List<CustomerBankingSubError> subErrorCollection = new ArrayList<>();
        argumentNotValid.getBindingResult().getFieldErrors().get(0).getRejectedValue();
        argumentNotValid.getBindingResult().getFieldErrors().stream().forEach((objError) -> {
            subErrorCollection.add(CustomerBankingValidationError.builder().object(objError.getObjectName())
                    .field(objError.getField()).rejectValue(objError.getRejectedValue())
                    .message(objError.getDefaultMessage()).build());
        });
        return subErrorCollection;
    }
}
