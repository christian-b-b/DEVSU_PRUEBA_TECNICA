package com.devsu.operationsbanking.services.impl;

import com.devsu.operationsbanking.constants.OperationsBankingConstants;
import com.devsu.operationsbanking.dtos.MovementReportResponseDTO;
import com.devsu.operationsbanking.errorhandler.OperationsBankingNotFoundException;
import com.devsu.operationsbanking.models.Account;
import com.devsu.operationsbanking.models.Customer;
import com.devsu.operationsbanking.models.Master;
import com.devsu.operationsbanking.models.Movement;
import com.devsu.operationsbanking.repositories.AccountRepository;
import com.devsu.operationsbanking.repositories.CustomerRepository;
import com.devsu.operationsbanking.repositories.MasterRepository;
import com.devsu.operationsbanking.repositories.MovementRepository;
import com.devsu.operationsbanking.services.ReportMovementService;
import com.devsu.operationsbanking.utils.ExceptionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportMovementServiceImpl implements ReportMovementService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MovementRepository movementRepository;
    @Autowired
    private MasterRepository masterRepository;
    @Override
    public List<MovementReportResponseDTO> movementReport(LocalDate startMovementDate, LocalDate endMovementDate, Long idCustomer) {
        List<MovementReportResponseDTO> report = new ArrayList<>();
        Customer customer = getCustomer(idCustomer);
        List<Account> accounts = accountRepository.findAccountsByIdCustomer(idCustomer);

        accounts.forEach(account -> {

            List<Movement> movements = movementRepository.findAllByAccount(account);
            movements.forEach(movement -> {
                MovementReportResponseDTO movementReportResponseDTO = MovementReportResponseDTO.builder().build();
                movementReportResponseDTO.setMovementDate(movement.getMovementDate());
                movementReportResponseDTO.setCustomer(customer.getNames());
                movementReportResponseDTO.setAccountNumber(account.getAccountNumber());
                Master accountType = getAccountType(account.getAccountTypeCode());

                movementReportResponseDTO.setAccountType(accountType.getDescription());
                movementReportResponseDTO.setInitialBalance(movement.getInitialBalance());
                movementReportResponseDTO.setState(movement.getState()== OperationsBankingConstants.ACTIVE_STATE?true:false);
                if (movement.getMovementTypeCode().equals(OperationsBankingConstants.MOVEMENT_TYPE_WITHDRAW)){
                    movementReportResponseDTO.setMovement("-"+movement.getAmount());
                    movementReportResponseDTO.setAvailableBalance(movement.getInitialBalance().subtract(movement.getAmount()));
                }else {
                    movementReportResponseDTO.setMovement(""+movement.getAmount());
                    movementReportResponseDTO.setAvailableBalance(movement.getInitialBalance().add(movement.getAmount()));
                }
                report.add(movementReportResponseDTO);

            });
        });
        return getFilterReport(report,startMovementDate,endMovementDate);
    }
    public Customer getCustomer(Long idCustomer){
        return customerRepository.getCustomertById(idCustomer).orElseThrow(
                ()-> new OperationsBankingNotFoundException(ExceptionConstants.CUSTOMER_NOT_FOUND.getMessage(),
                        ExceptionConstants.CUSTOMER_NOT_FOUND.getCode())
        );
    }
    public Master getAccountType(String accountTypeCode){
        return masterRepository.findByParentCodeAndCode(OperationsBankingConstants.PARENT_CODE_ACCOUNT_TYPE,
                accountTypeCode).orElseThrow(
                ()-> new OperationsBankingNotFoundException(ExceptionConstants.ACCOUNT_TYPE_NOT_FOUND.getMessage(),
                        ExceptionConstants.ACCOUNT_TYPE_NOT_FOUND.getCode()));
    }

    public List<MovementReportResponseDTO> getFilterReport(List<MovementReportResponseDTO> report,LocalDate startMovementDate,
                                                           LocalDate endMovementDate){
        return report.stream().filter(movRep->movRep.getMovementDate().compareTo(startMovementDate) >= 0 && movRep
                .getMovementDate().compareTo(endMovementDate)<=0).collect(Collectors.toList());
    }
}
