package com.example.butim.domain.region.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(
        name = "region",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_region_sido_sigungu",
                        columnNames = {"sido_code", "sigungu_code"}
                )
        }
)
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Long regionId;

    @Column(name = "sido_code", nullable = false)
    private String sidoCode;

    @Column(name = "sido_name", nullable = false)
    private String sidoName;

    @Column(name = "sigungu_code")
    private String sigunguCode;

    @Column(name = "sigungu_name")
    private String sigunguName;
}