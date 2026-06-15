package com.example.butim.domain.prediction.stats;

import java.util.List;

public record PrecedentStats(
        int approvedCount,
        int rejectedCount,
        List<String> caseContents
) {
    public int totalCount() {
        return approvedCount + rejectedCount;
    }

    public double approvalRate() {
        if (totalCount() == 0) return 0.0;
        return (double) approvedCount / totalCount();
    }
}
