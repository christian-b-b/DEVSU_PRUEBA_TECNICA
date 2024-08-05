package com.devsu.operationsbanking.controllers;

import com.devsu.operationsbanking.constants.OperationsBankingConstants;
import com.devsu.operationsbanking.dtos.MovementReportResponseDTO;
import com.devsu.operationsbanking.services.ReportMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(OperationsBankingConstants.API_VERSION + OperationsBankingConstants.RESOURCE_MOVEMENT_REPORT)
public class MovementReportController {
    @Autowired
    private ReportMovementService reportMovementService;
    @GetMapping()
    public ResponseEntity<List<MovementReportResponseDTO>> movementReport(@RequestParam("startDate") String startMovementDate,
                                                                          @RequestParam("endDate") String endMovementDate,
                                                                          @RequestParam("idCustomer")Long idCustomer){
        return new ResponseEntity<>(reportMovementService.movementReport(LocalDate.parse(startMovementDate),
                LocalDate.parse(endMovementDate),idCustomer), HttpStatus.OK);
    }


}
