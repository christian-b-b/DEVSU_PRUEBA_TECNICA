package com.devsu.operationsbanking.errorhandler;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class OperationsBankingGenericServerException extends RuntimeException {
    private String code;
    private HttpStatus httpStatus;

    public OperationsBankingGenericServerException(String message, HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }

    public OperationsBankingGenericServerException(String message) {
        super(message);
    }
}
