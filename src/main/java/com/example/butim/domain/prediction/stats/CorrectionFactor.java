package com.example.butim.domain.prediction.stats;

public record CorrectionFactor(
        double businessSizeMultiplier,
        double ageMultiplier
) {
    public double combined() {
        return businessSizeMultiplier * ageMultiplier;
    }
}
