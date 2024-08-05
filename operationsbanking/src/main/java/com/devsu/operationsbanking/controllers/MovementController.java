package com.devsu.operationsbanking.controllers;


import com.devsu.operationsbanking.constants.OperationsBankingConstants;
import com.devsu.operationsbanking.dtos.MovementResponseDTO;
import com.devsu.operationsbanking.dtos.request.MovementRequestDTO;
import com.devsu.operationsbanking.services.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping(OperationsBankingConstants.API_VERSION + OperationsBankingConstants.RESOURCE_MOVEMENT)
public class MovementController {
    @Autowired
    private MovementService movementService;

    @PostMapping
    public ResponseEntity<MovementResponseDTO> createMovement(@Valid  @RequestBody MovementRequestDTO movementRequestDTO){
        return new ResponseEntity<>(movementService.createMovement(movementRequestDTO),HttpStatus.OK);
    }

}
