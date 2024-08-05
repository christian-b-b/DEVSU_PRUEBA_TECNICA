package com.devsu.operationsbanking.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionConstants {
    ARGUMENT_NOT_VALID("CLI","400","Bad Request: {}","Fields validations error"),
    NOT_FOUND("CLI","404","Entity Not Found: {}","Entity Not Found"),
    GENERIC_SERVER("SRV","500","Internal server error: {}","Internal server error"),
    GENERIC_CLIENT("CLI","400","Client Error: {}","Fields validations error"),
    GENDER_NOT_FOUND("CLI","GNF","Bad Request: {}","Gender not found"),
    ACCOUNT_NOT_FOUND("CLI","ANF","Bad Request: {}","Account not found"),
    ACCOUNT_TYPE_NOT_FOUND("CLI","ATNF","Bad Request: {}","Account type not found"),
    MOVEMENT_TYPE_NOT_FOUND("CLI","MTNF","Bad Request: {}","Movement type not found"),
    INSUFICIENT_MONEY("CLI","IM","Bad Request: {}","Saldo no disponible"),
    DOCUMENT_TYPE_NOT_FOUND("CLI","DTNF","Bad Request: {}","Document type not found"),
    CUSTOMER_NOT_FOUND("CLI","CNF","Bad Request: {}","Customer not found"),
    ACCOUNT_NUMBER_IN_USE("CLI","ANIU","Bad Request: {}","accountNumber is already in use by another account");

    private final String prefix;
    private final String code;
    private final String description;
    private final String message;

}
