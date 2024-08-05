package com.devsu.customerbanking.errorhandler;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class CustomerBankingGenericServerException extends RuntimeException {
    private String code;
    private HttpStatus httpStatus;

    public CustomerBankingGenericServerException(String message, HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }

    public CustomerBankingGenericServerException(String message) {
        super(message);
    }
}
