package com.devsu.operationsbanking.controllers;


import com.devsu.operationsbanking.constants.OperationsBankingConstants;
import com.devsu.operationsbanking.dtos.AccountResponseDTO;
import com.devsu.operationsbanking.dtos.request.AccountRequestDTO;
import com.devsu.operationsbanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(OperationsBankingConstants.API_VERSION + OperationsBankingConstants.RESOURCE_ACCOUNT)
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping()
    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts(){
        return new ResponseEntity<>(accountService.getAllAccounts(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getAccountById(@PathVariable Long id){
        return new ResponseEntity<>(accountService.getAccountById(id),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(@Valid @RequestBody AccountRequestDTO accountRequestDTO){
        AccountResponseDTO accountResponseDTO = accountService.createAccount(accountRequestDTO);
        return new ResponseEntity<>(accountResponseDTO,HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> updateAccount(@PathVariable Long id,@Valid  @RequestBody AccountRequestDTO accountRequestDTO){
        return new ResponseEntity<>(accountService.updateAccount(accountRequestDTO,id),HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id){
        accountService.deleteAccount(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
