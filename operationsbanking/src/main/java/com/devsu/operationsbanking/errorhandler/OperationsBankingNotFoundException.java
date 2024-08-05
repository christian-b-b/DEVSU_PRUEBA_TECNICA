package com.devsu.operationsbanking.errorhandler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OperationsBankingNotFoundException extends RuntimeException{
    private String message;
    private String code;

}
