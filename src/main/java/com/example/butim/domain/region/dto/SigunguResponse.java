package com.example.butim.domain.region.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SigunguResponse {

    private Long regionId;
    private String sigunguCode;
    private String sigunguName;
}