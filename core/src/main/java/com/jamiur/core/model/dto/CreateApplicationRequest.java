package com.jamiur.core.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateApplicationRequest {
    private String applicationType;
    private BigDecimal amount;
    private String description;
    private String applicationData;

}