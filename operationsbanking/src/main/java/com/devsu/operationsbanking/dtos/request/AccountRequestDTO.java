package com.devsu.operationsbanking.dtos.request;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Data
@Builder
public class AccountRequestDTO {

    @NotNull(message = "idCustomer cannot be null")
    private Long idCustomer;
    @NotNull(message = "accountNumber cannot be null")
    private String accountNumber;
    @NotNull(message = "accountTypeCode cannot be null")
    private String accountTypeCode;
    @NotNull(message = "initialBalance cannot be null")
    private BigDecimal initialBalance;

}
