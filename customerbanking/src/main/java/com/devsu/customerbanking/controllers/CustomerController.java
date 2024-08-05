package com.devsu.customerbanking.controllers;

import com.devsu.customerbanking.dtos.CustomerResponseDTO;
import com.devsu.customerbanking.dtos.request.CustomerRequestDTO;
import com.devsu.customerbanking.services.CustomerService;
import com.devsu.customerbanking.utils.CustomerBankingConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(CustomerBankingConstants.API_VERSION + CustomerBankingConstants.RESOURCE_CUSTOMER)
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping()
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers(){
        return new ResponseEntity<>(customerService.getAllCustomers(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomById(@PathVariable Long id){
        return new ResponseEntity<>(customerService.getCustomerById(id),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerRequestDTO customerRequestDTO){
        return new ResponseEntity<>(customerService.createCustomer(customerRequestDTO),HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable Long id,@Valid @RequestBody CustomerRequestDTO customerRequestDTO){
        return new ResponseEntity<>(customerService.updateCustomer(customerRequestDTO,id),HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id){
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
