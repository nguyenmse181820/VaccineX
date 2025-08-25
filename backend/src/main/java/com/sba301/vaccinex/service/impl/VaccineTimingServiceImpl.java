package com.sba301.vaccinex.service.impl;

import com.sba301.vaccinex.exception.ElementNotFoundException;
import com.sba301.vaccinex.pojo.Vaccine;
import com.sba301.vaccinex.pojo.VaccineTiming;
import com.sba301.vaccinex.repository.VaccineTimingRepository;
import com.sba301.vaccinex.service.spec.VaccineTimingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VaccineTimingServiceImpl implements VaccineTimingService {

    private final VaccineTimingRepository vaccineTimingRepository;

    @Override
    public VaccineTiming getVaccineTimingById(Integer id) {
        return vaccineTimingRepository.findByIdAndDeletedIsFalse(id).orElseThrow(
                () -> new ElementNotFoundException("Không tìm thấy thời điểm tiêm chủng với ID: " + id)
        );
    }

    @Override
    public VaccineTiming getVaccineTimingByVaccine(Vaccine vaccine, int doseNo) {
        return vaccineTimingRepository.findByVaccineAndDoseNoAndDeletedIsFalse(vaccine, doseNo).orElseThrow(
                () -> new ElementNotFoundException("Không tìm thấy thời điểm tiêm chủng với vaccine: " + vaccine)
        );
    }
}
