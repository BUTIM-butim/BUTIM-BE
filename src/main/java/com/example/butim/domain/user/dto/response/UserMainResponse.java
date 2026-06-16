package com.example.butim.domain.user.dto.response;

public record UserMainResponse(
        Integer predictionMinDays,
        Integer predictionMaxDays,
        Integer predictionMedianDays,
        Integer actualExpectedDays,
        Integer paymentExpectedDays,
        Boolean hasSupportItems,
        Boolean hasLoanItems
) {
}