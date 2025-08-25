package com.sba301.vaccinex.service.spec;

import com.sba301.vaccinex.pojo.Vaccine;
import com.sba301.vaccinex.pojo.VaccineTiming;


public interface VaccineTimingService {
    VaccineTiming getVaccineTimingById(Integer id);

    VaccineTiming getVaccineTimingByVaccine(Vaccine vaccine, int doseNo);
}
