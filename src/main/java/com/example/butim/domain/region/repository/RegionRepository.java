package com.example.butim.domain.region.repository;

import com.example.butim.domain.region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {

    List<Region> findBySigunguCodeIsNullOrderBySidoNameAsc();

    List<Region> findBySidoCodeAndSigunguCodeIsNotNullOrderBySigunguNameAsc(String sidoCode);

    boolean existsBySidoCodeAndSigunguCode(String sidoCode, String sigunguCode);

    boolean existsBySidoCodeAndSigunguCodeIsNull(String sidoCode);

    boolean existsBySidoCode(String sidoCode);
}