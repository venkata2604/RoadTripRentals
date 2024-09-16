package com.carrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarInspectionDto {
    private Long inspectionId;
    private Long carId;
    private Date inspectionDate;
    private String inspectionResult;
    // Add any additional fields as needed
}
