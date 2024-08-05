package com.devsu.customerbanking.services;

import com.devsu.customerbanking.dtos.CustomerResponseDTO;
import com.devsu.customerbanking.models.Customer;
import com.devsu.customerbanking.repositories.CustomerRepository;
import com.devsu.customerbanking.services.impl.CustomerServiceImpl;
import com.devsu.customerbanking.utils.CustomerBankingConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {
    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    private Customer customer;

    private CustomerResponseDTO customerResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = Customer.builder().build();
        customer.setIdPerson(1L);
        customer.setNames("Christian");
        customer.setFirstLastName("Baldeón");
        customer.setSecondLastName("Baldeón");
        customer.setGenderCode("M");
        customer.setDocumentTypeCode("DNI");
        customer.setDocumentNumber("44524600");
        customer.setAddress("Asoc. Ciudad Magisterial F 10");
        customer.setCellphone("956460840");
        customer.setPassword("123456");
        customer.setState(1);

        customerResponseDTO = CustomerResponseDTO.builder().build();
        customerResponseDTO.setIdCustomer(customer.getIdPerson());
        customerResponseDTO.setNames(customer.getNames() +" "+ customer.getFirstLastName());
        customerResponseDTO.setAddress(customer.getAddress());
        customerResponseDTO.setCellphone(customer.getCellphone());
        customerResponseDTO.setPassword(customer.getPassword());
        customerResponseDTO.setState(customer.getState()== CustomerBankingConstants.ACTIVE_STATE?true:false);
    }

    @Test
    @DisplayName("Return customer when search by id")
    void testGetCustomerById() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        CustomerResponseDTO result = customerService.getCustomerById(1L);

        assertEquals(customerResponseDTO.getPassword(), result.getPassword());
        assertEquals(customerResponseDTO.getNames(), result.getNames());
        assertEquals(customerResponseDTO.getAddress(), result.getAddress());
        assertEquals(customerResponseDTO.getCellphone(), result.getCellphone());

    }
}
