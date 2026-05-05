package com.example.butim.domain.region.service;

import com.example.butim.domain.region.csv.RegionCsvImporter;
import com.example.butim.domain.region.dto.SidoResponse;
import com.example.butim.domain.region.dto.SigunguResponse;
import com.example.butim.domain.region.entity.Region;
import com.example.butim.domain.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;
    private final RegionCsvImporter regionCsvImporter;

    @Transactional(readOnly = true)
    public List<SidoResponse> getSidoList() {
        return regionRepository.findBySigunguCodeIsNullOrderBySidoNameAsc()
                .stream()
                .map(region -> new SidoResponse(
                        region.getSidoCode(),
                        region.getSidoName()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SigunguResponse> getSigunguList(String sidoCode) {
        return regionRepository.findBySidoCodeAndSigunguCodeIsNotNullOrderBySigunguNameAsc(sidoCode)
                .stream()
                .map(region -> new SigunguResponse(
                        region.getSigunguCode(),
                        region.getSigunguName()
                ))
                .toList();
    }

    @Transactional
    public void syncRegionsFromCsv() {
        List<Region> regions = regionCsvImporter.readRegionsFromCsv();

        for (Region region : regions) {
            if (region.getSigunguCode() == null) {
                boolean exists = regionRepository.existsBySidoCodeAndSigunguCodeIsNull(region.getSidoCode());

                if (!exists) {
                    regionRepository.save(region);
                }
            } else {
                boolean exists = regionRepository.existsBySidoCodeAndSigunguCode(
                        region.getSidoCode(),
                        region.getSigunguCode()
                );

                if (!exists) {
                    regionRepository.save(region);
                }
            }
        }
    }
}