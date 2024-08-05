package com.devsu.operationsbanking.errorhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@AllArgsConstructor
public class OperationsBankingGenericClientException extends RuntimeException{
    private String message;
    private HttpStatus httpStatus;
    private byte[] body;
    private List<OperationsBankingSubError> subErrors;
}
