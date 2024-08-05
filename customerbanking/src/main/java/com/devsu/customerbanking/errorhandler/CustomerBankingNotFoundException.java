package com.devsu.customerbanking.errorhandler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerBankingNotFoundException extends RuntimeException{
    private String message;
    private String code;

}
