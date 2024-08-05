package com.devsu.customerbanking.dtos.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Builder
public class CustomerRequestDTO {
    @NotNull(message = "names cannot be null")
    private String names;
    @NotNull(message = "firstLastName cannot be null")
    private String firstLastName;
    @NotNull(message = "secondLastName cannot be null")
    private String secondLastName;
    @NotNull(message = "genderCode cannot be null")
    private String genderCode;
    @NotNull(message = "birthDate cannot be null")
    @PastOrPresent(message = "birthDate cannot be in the future")
    private LocalDate birthDate;
    @NotNull(message = "documentTypeCode cannot be null")
    private String documentTypeCode;
    @NotNull(message = "documentNumber cannot be null")
    private String documentNumber;
    @NotNull(message = "address cannot be null")
    private String address;
    @NotNull(message = "cellphone cannot be null")
    @Pattern(regexp = "\\d{9}", message = "Cellphone must be composed of 9 digits")
    private String cellphone;
    @NotNull(message = "password cannot be null")
    private String password;
}
