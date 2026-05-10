package com.example.butim.domain.accident.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessSize {
    UNDER_5("5인 미만"),
    FROM_5_TO_9("5~9인"),
    FROM_10_TO_29("10~29인"),
    FROM_30_TO_49("30~49인"),
    FROM_50_TO_99("50~99인"),
    FROM_100_TO_299("100~299인"),
    FROM_300_TO_499("300~499인"),
    FROM_500_TO_999("500~999인"),
    OVER_1000("1000인 이상");

    private final String label;
}
