package com.devsu.operationsbanking.services.impl;


import com.devsu.operationsbanking.builders.MovementBuilder;
import com.devsu.operationsbanking.constants.OperationsBankingConstants;
import com.devsu.operationsbanking.dtos.MovementResponseDTO;
import com.devsu.operationsbanking.dtos.request.MovementRequestDTO;
import com.devsu.operationsbanking.errorhandler.OperationsBankingGenericClientException;
import com.devsu.operationsbanking.errorhandler.OperationsBankingNotFoundException;
import com.devsu.operationsbanking.models.Account;
import com.devsu.operationsbanking.models.Master;
import com.devsu.operationsbanking.models.Movement;
import com.devsu.operationsbanking.repositories.AccountRepository;
import com.devsu.operationsbanking.repositories.MasterRepository;
import com.devsu.operationsbanking.repositories.MovementRepository;
import com.devsu.operationsbanking.services.MovementService;
import com.devsu.operationsbanking.utils.ExceptionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class MovementServiceImpl implements MovementService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MasterRepository masterRepository;
    @Autowired
    private MovementRepository movementRepository;

    @Override
    public MovementResponseDTO createMovement(MovementRequestDTO movementRequestDTO) {
        Account account = getAccountByAccountNumber(movementRequestDTO.getAccountNumber());
        Master movementType = getMovementTypeCode(movementRequestDTO.getMovementTypeCode());
        BigDecimal initialBalance = account.getInitialBalance();

        if (movementIsWithdraw(movementRequestDTO.getMovementTypeCode())){
            BigDecimal finalBalance = account.getInitialBalance().subtract(movementRequestDTO.getAmount());
            if (finalBalance.compareTo(BigDecimal.ZERO)< OperationsBankingConstants.ZERO)
                throw new OperationsBankingGenericClientException(ExceptionConstants.INSUFICIENT_MONEY.getMessage(),
                        HttpStatus.BAD_REQUEST,null,null);
            account.setInitialBalance(finalBalance);
            accountRepository.save(account);
        }

        if(movementIsDeposit(movementRequestDTO.getMovementTypeCode())){
            BigDecimal finalBalance = account.getInitialBalance().add(movementRequestDTO.getAmount());
            account.setInitialBalance(finalBalance);
            accountRepository.save(account);
        }

        Movement movement = MovementBuilder.movementBuilder(movementRequestDTO,movementType,initialBalance,account);
        movementRepository.save(movement);
        Master accountType = getAccountType(account.getAccountTypeCode());

        return MovementBuilder.movementResponseDTO(movement,account,accountType,movementType);
    }
    public Account getAccountByAccountNumber(String accountNumber){
        return accountRepository.findByAccountNumber(accountNumber).orElseThrow(
                ()-> new OperationsBankingNotFoundException(ExceptionConstants.ACCOUNT_NOT_FOUND.getMessage(),
                        ExceptionConstants.ACCOUNT_NOT_FOUND.getCode())
        );
    }
    public Master getMovementTypeCode(String movementTypeCode){
        return masterRepository.findByParentCodeAndCode(OperationsBankingConstants.PARENT_CODE_MOVEMENT_TYPE,
                movementTypeCode).orElseThrow(
                ()-> new OperationsBankingNotFoundException(ExceptionConstants.MOVEMENT_TYPE_NOT_FOUND.getMessage(),
                        ExceptionConstants.MOVEMENT_TYPE_NOT_FOUND.getCode()));
    }
    public boolean movementIsWithdraw(String movementTypeCode){
        return movementTypeCode.equals(OperationsBankingConstants.MOVEMENT_TYPE_WITHDRAW);
    }
    public boolean movementIsDeposit(String movementTypeCode){
        return movementTypeCode.equals(OperationsBankingConstants.MOVEMENT_TYPE_DEPOSIT);
    }
    public Master getAccountType(String accountTypeCode){
        return masterRepository.findByParentCodeAndCode(OperationsBankingConstants.PARENT_CODE_ACCOUNT_TYPE,
                accountTypeCode).orElseThrow(
                ()-> new OperationsBankingNotFoundException(ExceptionConstants.ACCOUNT_TYPE_NOT_FOUND.getMessage(),
                        ExceptionConstants.ACCOUNT_TYPE_NOT_FOUND.getCode()));
    }

}
