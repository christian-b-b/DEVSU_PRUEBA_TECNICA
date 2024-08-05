package com.devsu.customerbanking.services.impl;

import com.devsu.customerbanking.builders.CustomerBuilder;
import com.devsu.customerbanking.dtos.request.CustomerRequestDTO;
import com.devsu.customerbanking.errorhandler.CustomerBankingGenericClientException;
import com.devsu.customerbanking.errorhandler.CustomerBankingNotFoundException;
import com.devsu.customerbanking.models.Customer;
import com.devsu.customerbanking.models.Master;
import com.devsu.customerbanking.utils.CustomerBankingConstants;
import com.devsu.customerbanking.dtos.CustomerResponseDTO;
import com.devsu.customerbanking.repositories.CustomerRepository;
import com.devsu.customerbanking.repositories.MasterRepository;
import com.devsu.customerbanking.services.CustomerService;
import com.devsu.customerbanking.utils.ExceptionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private MasterRepository masterRepository;
    @Override
    public List<CustomerResponseDTO> getAllCustomers() {

        return customerRepository.findAll().stream().map(customer->
                CustomerBuilder.CustomerResponseDTOBuilder(customer)
        ).collect(Collectors.toList());
    }

    @Override
    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(
                ()-> new CustomerBankingNotFoundException(ExceptionConstants.CUSTOMER_NOT_FOUND.getMessage(),
                        ExceptionConstants.CUSTOMER_NOT_FOUND.getCode())
        );

        return CustomerBuilder.CustomerResponseDTOBuilder(customer);
    }

    @Override
    public CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO) {
        if (documentNumberInUse(customerRequestDTO.getDocumentNumber())){
            throw new CustomerBankingGenericClientException(ExceptionConstants.DOCUMENT_NUMBER_IN_USE.getMessage(),
                    HttpStatus.BAD_REQUEST,null, null );
        }
        Master gender =getGender(customerRequestDTO.getGenderCode());
        Master documentType =getDocumentType(customerRequestDTO.getDocumentTypeCode());
        Customer customer = CustomerBuilder.customerBuilder(customerRequestDTO, gender,documentType);
        customerRepository.save(customer);

        return CustomerBuilder.CustomerResponseDTOBuilder(customer);
    }
    public boolean documentNumberInUse(String documentNumber){
        return customerRepository.findByDocumentNumber(documentNumber).isPresent();
    }
    public Master getGender(String genderCode){
        return masterRepository.findByParentCodeAndCode(CustomerBankingConstants.PARENT_CODE_GENDER,
                genderCode).orElseThrow(
                ()-> new CustomerBankingNotFoundException(ExceptionConstants.GENDER_NOT_FOUND.getMessage(),
                        ExceptionConstants.GENDER_NOT_FOUND.getCode())
        );
    }
    public Master getDocumentType(String documentTypeCode){
        return masterRepository.findByParentCodeAndCode(CustomerBankingConstants.PARENT_CODE_DOCUMENT_TYPE,
                documentTypeCode).orElseThrow(
                ()-> new CustomerBankingNotFoundException(ExceptionConstants.DOCUMENT_TYPE_NOT_FOUND.getMessage(),
                        ExceptionConstants.DOCUMENT_TYPE_NOT_FOUND.getCode()));
    }

    @Override
    public CustomerResponseDTO updateCustomer(CustomerRequestDTO customerRequestDTO, Long idCustomer) {
        Customer customer = customerRepository.findById(idCustomer).orElseThrow(
                ()-> new CustomerBankingNotFoundException(ExceptionConstants.CUSTOMER_NOT_FOUND.getMessage(),
                        ExceptionConstants.CUSTOMER_NOT_FOUND.getCode())
        );

        Optional<Customer> existingCustomerWithDoc = customerRepository.findByDocumentNumber(customerRequestDTO.getDocumentNumber());
        if (existingCustomerWithDoc.isPresent() && !existingCustomerWithDoc.get().getIdPerson().equals(idCustomer)){
            throw new CustomerBankingGenericClientException(ExceptionConstants.DOCUMENT_NUMBER_IN_USE.getMessage(),
                    HttpStatus.BAD_REQUEST,null, null );
        }

        Master gender =getGender(customerRequestDTO.getGenderCode());
        Master documentType =getDocumentType(customerRequestDTO.getDocumentTypeCode());
        CustomerBuilder.fillCustomerData(customer,customerRequestDTO,gender,documentType);
        customerRepository.save(customer);
        return CustomerBuilder.CustomerResponseDTOBuilder(customer);
    }

    @Override
    public void deleteCustomer(Long idCustomer) {
        Customer customer = customerRepository.findById(idCustomer).orElseThrow(
                ()-> new CustomerBankingNotFoundException(ExceptionConstants.CUSTOMER_NOT_FOUND.getMessage(),
                        ExceptionConstants.CUSTOMER_NOT_FOUND.getCode())
        );
        customerRepository.deleteById(customer.getIdPerson());
    }
}
