package com.devsu.operationsbanking.repositories;

import com.devsu.operationsbanking.models.Account;
import com.devsu.operationsbanking.models.Movement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovementRepository extends JpaRepository<Movement,Long> {
    List<Movement> findAllByAccount(Account account);
}
