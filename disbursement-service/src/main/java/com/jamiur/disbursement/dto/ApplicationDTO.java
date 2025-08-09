package com.jamiur.disbursement.dto;

import com.jamiur.disbursement.model.Application;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDTO {
    private Application application;
    private String coDecision;
    private String roDecision;
}
