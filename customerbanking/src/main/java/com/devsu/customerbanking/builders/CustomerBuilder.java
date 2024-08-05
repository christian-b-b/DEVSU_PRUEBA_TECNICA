package com.devsu.customerbanking.builders;

import com.devsu.customerbanking.dtos.CustomerResponseDTO;
import com.devsu.customerbanking.dtos.request.CustomerRequestDTO;
import com.devsu.customerbanking.models.Customer;
import com.devsu.customerbanking.models.Master;
import com.devsu.customerbanking.utils.CustomerBankingConstants;

import java.time.LocalDateTime;

public class CustomerBuilder {
    public static Customer customerBuilder(CustomerRequestDTO customerRequestDTO, Master gender, Master documentType){
        Customer customer = Customer.builder().build();
        fillCustomerData(customer,customerRequestDTO,gender,documentType);
        customer.setState(CustomerBankingConstants.ACTIVE_STATE);
        customer.setRegistrationDate(LocalDateTime.now());
        return customer;
    }
    public static void fillCustomerData(Customer customer,CustomerRequestDTO customerRequestDTO,Master gender,Master documentType ){
        customer.setNames(customerRequestDTO.getNames());
        customer.setFirstLastName(customerRequestDTO.getFirstLastName());
        customer.setSecondLastName(customerRequestDTO.getSecondLastName());
        customer.setGenderCode(gender.getCode());
        customer.setBirthDate(customerRequestDTO.getBirthDate());
        customer.setDocumentTypeCode(documentType.getCode());
        customer.setDocumentNumber(customerRequestDTO.getDocumentNumber());
        customer.setAddress(customerRequestDTO.getAddress());
        customer.setCellphone(customerRequestDTO.getCellphone());
        customer.setPassword(customerRequestDTO.getPassword());
    }
    public static CustomerResponseDTO CustomerResponseDTOBuilder(Customer customer){
        return CustomerResponseDTO.builder()
                .idCustomer(customer.getIdPerson())
                .names(customer.getNames() +" "+ customer.getFirstLastName())
                .address(customer.getAddress())
                .cellphone(customer.getCellphone())
                .password(customer.getPassword())
                .state(customer.getState()== CustomerBankingConstants.ACTIVE_STATE?true:false).build();
    }

}
