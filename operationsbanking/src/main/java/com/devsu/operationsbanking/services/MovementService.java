package com.devsu.operationsbanking.services;

import com.devsu.operationsbanking.dtos.MovementResponseDTO;
import com.devsu.operationsbanking.dtos.request.MovementRequestDTO;

public interface MovementService {

    public MovementResponseDTO createMovement(MovementRequestDTO movementRequestDTO);
}
