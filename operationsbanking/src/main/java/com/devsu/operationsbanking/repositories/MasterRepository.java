package com.devsu.operationsbanking.repositories;


import com.devsu.operationsbanking.models.Master;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MasterRepository extends JpaRepository<Master,Long> {
    public Optional<Master> findByParentCodeAndCode(String parentCode,String code);

}
