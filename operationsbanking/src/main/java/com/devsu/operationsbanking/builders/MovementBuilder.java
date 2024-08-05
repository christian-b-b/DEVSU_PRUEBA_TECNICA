package com.devsu.operationsbanking.builders;

import com.devsu.operationsbanking.constants.OperationsBankingConstants;
import com.devsu.operationsbanking.dtos.MovementResponseDTO;
import com.devsu.operationsbanking.dtos.request.MovementRequestDTO;
import com.devsu.operationsbanking.models.Account;
import com.devsu.operationsbanking.models.Master;
import com.devsu.operationsbanking.models.Movement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MovementBuilder {

    public static Movement movementBuilder(MovementRequestDTO movementRequestDTO, Master movementType,
                                           BigDecimal initialBalance, Account account){
        Movement movement = Movement.builder().build();
        movement.setMovementTypeCode(movementType.getCode());
        movement.setMovementDate(LocalDate.now());
        movement.setInitialBalance(initialBalance);
        movement.setAccount(account);
        movement.setAmount(movementRequestDTO.getAmount());
        movement.setState(OperationsBankingConstants.ACTIVE_STATE);
        movement.setRegistrationDate(LocalDateTime.now());
        return movement;
    }

    public static MovementResponseDTO movementResponseDTO(Movement movement,Account account,
                                                          Master accountType,Master movementType){
        return MovementResponseDTO.builder()
                .accountNumber(account.getAccountNumber())
                .accountType(accountType.getDescription())
                .initialBalance(movement.getInitialBalance())
                .state(movement.getState()== OperationsBankingConstants.ACTIVE_STATE?true:false)
                .movement(movementType.getDescription()+" de "+ movement.getAmount())
                .build();
    }
}
