package com.devsu.operationsbanking.repositories.impl;

import com.devsu.operationsbanking.constants.OperationsBankingEndpointsCustomer;
import com.devsu.operationsbanking.errorhandler.OperationsBankingGenericClientException;
import com.devsu.operationsbanking.errorhandler.OperationsBankingGenericServerException;
import com.devsu.operationsbanking.models.Customer;
import com.devsu.operationsbanking.repositories.CustomerRepository;
import com.devsu.operationsbanking.utils.ExceptionConstants;
import com.devsu.operationsbanking.utils.OperationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OperationUtil operationUtil;
    @Override
    public Optional<Customer> getCustomertById(Long id) {
        URI uri = operationUtil.createURI(OperationsBankingEndpointsCustomer.BASE_URI_CUSTOMER + "/"+id
                ).orElseThrow(
                () -> new OperationsBankingGenericServerException("Get Customer in customerbanking failed"));

        RequestEntity<Void> requestEntity = RequestEntity.get(uri).build();
        operationUtil.printUriRequestJson("CustomerRepositoryImpl ==> getCustomertById",uri.toString(),requestEntity);

        try {
            ResponseEntity<Customer> response = restTemplate.exchange(requestEntity, Customer.class);
            return Optional.of(response.getBody());
        }catch (OperationsBankingGenericClientException ex){
            if (ex.getHttpStatus().equals(HttpStatus.NOT_FOUND) )
                throw new OperationsBankingGenericClientException(ExceptionConstants.CUSTOMER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND, null, null);
            else throw ex;
        }

    }
}
