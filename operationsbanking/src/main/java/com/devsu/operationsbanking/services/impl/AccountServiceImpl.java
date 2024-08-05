package com.devsu.operationsbanking.services.impl;


import com.devsu.operationsbanking.builders.AccountBuilder;
import com.devsu.operationsbanking.constants.OperationsBankingConstants;
import com.devsu.operationsbanking.dtos.AccountResponseDTO;
import com.devsu.operationsbanking.dtos.request.AccountRequestDTO;
import com.devsu.operationsbanking.errorhandler.OperationsBankingGenericClientException;
import com.devsu.operationsbanking.errorhandler.OperationsBankingNotFoundException;
import com.devsu.operationsbanking.models.Account;
import com.devsu.operationsbanking.models.Customer;
import com.devsu.operationsbanking.models.Master;
import com.devsu.operationsbanking.repositories.AccountRepository;
import com.devsu.operationsbanking.repositories.CustomerRepository;
import com.devsu.operationsbanking.repositories.MasterRepository;
import com.devsu.operationsbanking.services.AccountService;
import com.devsu.operationsbanking.utils.ExceptionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private MasterRepository masterRepository;

    @Override
    public List<AccountResponseDTO> getAllAccounts() {
        return accountRepository.findAll().stream().map(account -> {
            Master accountType= getAccountType(account.getAccountTypeCode());
            Customer customer = getCustomer(account.getIdCustomer());
            return AccountBuilder.accountResponseDTOBuilder(account,accountType,customer);
        }).collect(Collectors.toList());
    }
    public Master getAccountType(String accountTypeCode){
        return masterRepository.findByParentCodeAndCode(OperationsBankingConstants.PARENT_CODE_ACCOUNT_TYPE,
                accountTypeCode).orElseThrow(
                ()-> new OperationsBankingNotFoundException(ExceptionConstants.ACCOUNT_TYPE_NOT_FOUND.getMessage(),
                        ExceptionConstants.ACCOUNT_TYPE_NOT_FOUND.getCode()));
    }
    public Customer getCustomer(Long idCustomer){
        return customerRepository.getCustomertById(idCustomer).orElseThrow(
                ()-> new OperationsBankingNotFoundException(ExceptionConstants.CUSTOMER_NOT_FOUND.getMessage(),
                        ExceptionConstants.CUSTOMER_NOT_FOUND.getCode())
        );
    }

    @Override
    public AccountResponseDTO getAccountById(Long id) {
        Account account = getAccount(id);
        Customer customer = getCustomer(account.getIdCustomer());
        Master accountType = getAccountType(account.getAccountTypeCode());
        return AccountBuilder.accountResponseDTOBuilder(account,accountType,customer);
    }
    public Account getAccount(Long idAccount){
        return accountRepository.findById(idAccount).orElseThrow(
                ()-> new OperationsBankingNotFoundException(ExceptionConstants.ACCOUNT_NOT_FOUND.getMessage(),
                        ExceptionConstants.ACCOUNT_NOT_FOUND.getCode())
        );
    }
    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO) {
        Customer customer = getCustomer(accountRequestDTO.getIdCustomer());
        Master accountType =getAccountType(accountRequestDTO.getAccountTypeCode());

        if (accountNumberInUse(accountRequestDTO.getAccountNumber())){
            throw new OperationsBankingGenericClientException(ExceptionConstants.ACCOUNT_NUMBER_IN_USE.getMessage(),
                    HttpStatus.BAD_REQUEST,null, null );
        }

        Account account=AccountBuilder.accountBuilder(accountRequestDTO,customer,accountType);
        accountRepository.save(account);
        return AccountBuilder.accountResponseDTOBuilder(account,accountType,customer);
    }
    public boolean accountNumberInUse(String accountNumber){
        return accountRepository.findByAccountNumber(accountNumber).isPresent();
    }

    @Override
    public AccountResponseDTO updateAccount(AccountRequestDTO accountRequestDTO, Long idAccount) {
        Account account = getAccount(idAccount);
        Customer customer = getCustomer(account.getIdCustomer());
        Master accountType = getAccountType(accountRequestDTO.getAccountTypeCode());

        Optional<Account> existingAccountWithNumber = accountRepository.findByAccountNumber(accountRequestDTO.getAccountNumber());
        if (existingAccountWithNumber.isPresent() && !existingAccountWithNumber.get().getIdAccount().equals(idAccount)){
            throw new OperationsBankingGenericClientException(ExceptionConstants.ACCOUNT_NUMBER_IN_USE.getMessage(),
                    HttpStatus.BAD_REQUEST,null, null );
        }
        AccountBuilder.fillAccountData(account,accountRequestDTO,customer,accountType);
        accountRepository.save(account);

        return AccountBuilder.accountResponseDTOBuilder(account,accountType,customer);
    }

    @Override
    public void deleteAccount(Long idAccount) {
        Account account = getAccount(idAccount);
        accountRepository.deleteById(account.getIdAccount());
    }
}
