package com.devsu.operationsbanking.dtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountResponseDTO {
    private String accountNumber;
    private String accountType;
    private BigDecimal initialBalance;
    private boolean state;
    private String customer;

}
