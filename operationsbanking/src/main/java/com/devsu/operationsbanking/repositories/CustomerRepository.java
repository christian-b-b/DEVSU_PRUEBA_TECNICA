package com.devsu.operationsbanking.repositories;

import com.devsu.operationsbanking.models.Customer;

import java.util.Optional;

public interface CustomerRepository {
    public Optional<Customer> getCustomertById(Long id);
}
