package com.devsu.operationsbanking.services;

import com.devsu.operationsbanking.dtos.MovementReportResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReportMovementService {


    public List<MovementReportResponseDTO> movementReport(LocalDate startMovementDate,LocalDate endMovementDate,Long idCustomer);


}
