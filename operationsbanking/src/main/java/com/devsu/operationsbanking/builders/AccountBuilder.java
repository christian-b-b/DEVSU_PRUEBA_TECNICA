package com.devsu.operationsbanking.builders;

import com.devsu.operationsbanking.constants.OperationsBankingConstants;
import com.devsu.operationsbanking.dtos.AccountResponseDTO;
import com.devsu.operationsbanking.dtos.request.AccountRequestDTO;
import com.devsu.operationsbanking.models.Account;
import com.devsu.operationsbanking.models.Customer;
import com.devsu.operationsbanking.models.Master;

import java.time.LocalDateTime;

public class AccountBuilder {
    public static Account accountBuilder(AccountRequestDTO accountRequestDTO, Customer customer, Master accountType){
        Account account = Account.builder().build();
        fillAccountData(account,accountRequestDTO,customer,accountType);
        account.setState(OperationsBankingConstants.ACTIVE_STATE);
        account.setRegistrationDate(LocalDateTime.now());
        return account;
    }
    public static void fillAccountData(Account account,AccountRequestDTO accountRequestDTO,Customer customer,Master accountType){
        account.setIdCustomer(customer.getIdCustomer());
        account.setAccountNumber(accountRequestDTO.getAccountNumber());
        account.setAccountTypeCode(accountType.getCode());
        account.setInitialBalance(accountRequestDTO.getInitialBalance());
    }
    public static AccountResponseDTO accountResponseDTOBuilder(Account account, Master accountType, Customer customer){

        AccountResponseDTO accountResponseDTO = AccountResponseDTO.builder().build();
        accountResponseDTO.setAccountNumber(account.getAccountNumber());
        accountResponseDTO.setAccountType(accountType.getDescription());
        accountResponseDTO.setInitialBalance(account.getInitialBalance());
        accountResponseDTO.setState(account.getState()== OperationsBankingConstants.ACTIVE_STATE?true:false);
        accountResponseDTO.setCustomer(customer.getNames());

        return accountResponseDTO;
    }

}
