package com.example.butim.domain.accident.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "diagnosis_code")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiagnosisCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 10)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Builder
    public DiagnosisCode(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
