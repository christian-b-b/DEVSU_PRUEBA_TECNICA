package com.devsu.operationsbanking.errorhandler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OperationsBankingValidationError extends OperationsBankingSubError {
    private String object;
    private String field;
    private Object rejectValue;
    private String message;
}
