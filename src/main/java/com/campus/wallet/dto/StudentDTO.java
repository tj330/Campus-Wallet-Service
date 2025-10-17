package com.campus.wallet.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {
    private String admissionNo;
    private String name;
    private String department;
    private String email;
    private Double balance;
}
