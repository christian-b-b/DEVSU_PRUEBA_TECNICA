package com.devsu.customerbanking.errorhandler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CustomerBankingValidationError extends CustomerBankingSubError {
    private String object;
    private String field;
    private Object rejectValue;
    private String message;
}
