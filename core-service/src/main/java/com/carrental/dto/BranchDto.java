package com.carrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchDto {
    private Long branchId;
    private String branchName;
    private String location;
    private String contactInfo;
    private Long branchManagerId;
    private String branchManagerName;

}
