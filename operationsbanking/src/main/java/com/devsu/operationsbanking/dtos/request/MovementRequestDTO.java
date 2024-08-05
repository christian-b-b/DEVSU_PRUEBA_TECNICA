package com.devsu.operationsbanking.dtos.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
public class MovementRequestDTO {

    @NotNull(message = "accountNumber cannot be null")
    private String accountNumber;
    @NotNull(message = "movementTypeCode cannot be null")
    private String movementTypeCode;
    @NotNull(message = "amount cannot be null")
    private BigDecimal amount;


}
