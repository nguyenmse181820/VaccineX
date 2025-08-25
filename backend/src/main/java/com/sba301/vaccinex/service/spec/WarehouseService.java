package com.sba301.vaccinex.service.spec;

import com.sba301.vaccinex.dto.request.ExportVaccineRequest;
import com.sba301.vaccinex.dto.response.VaccineReportResponseDTO;
import com.sba301.vaccinex.exception.BadRequestException;

import java.time.LocalDate;
import java.util.List;

public interface WarehouseService {
    List<VaccineReportResponseDTO> getVaccineReports(Integer doctorId, String shift, LocalDate date);

    Object requestVaccineExport(ExportVaccineRequest request) throws BadRequestException;
}
