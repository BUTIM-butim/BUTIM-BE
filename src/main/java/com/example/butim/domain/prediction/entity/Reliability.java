package com.example.butim.domain.prediction.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Reliability {
    HIGH("높음"),
    MEDIUM("보통"),
    LOW("낮음");

    private final String label;
}
