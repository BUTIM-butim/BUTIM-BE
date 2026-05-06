package com.example.butim.domain.industry.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "industry")
public class Industry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "industry_id")
    private Long id;

    @Column(name = "industry_name", nullable = false)
    private String industryName;

    @Column(name = "industry_code", nullable = false, unique = true)
    private String industryCode;

    @Builder
    public Industry(String industryName, String industryCode) {
        this.industryName = industryName;
        this.industryCode = industryCode;
    }

    public void updateIndustryName(String industryName) {
        this.industryName = industryName;
    }
}