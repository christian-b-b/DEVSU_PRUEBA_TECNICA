package com.devsu.customerbanking.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionConstants {
    ARGUMENT_NOT_VALID("CLI","400","Bad Request: {}","Fields validations error"),
    NOT_FOUND("CLI","404","Entity Not Found: {}","Entity Not Found"),
    GENERIC_CLIENT("CLI","400","Client Error: {}","Fields validations error"),
    GENDER_NOT_FOUND("CLI","GNF","Bad Request: {}","Gender not found"),
    DOCUMENT_TYPE_NOT_FOUND("CLI","DTNF","Bad Request: {}","Document type not found"),
    CUSTOMER_NOT_FOUND("CLI","CNF","Bad Request: {}","Customer not found"),
    DOCUMENT_NUMBER_IN_USE("CLI","DNIU","Bad Request: {}","documentNumber is already in use by another customer");

    private final String prefix;
    private final String code;
    private final String description;
    private final String message;

}
