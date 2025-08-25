package com.sba301.vaccinex.dto.paging;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sba301.vaccinex.pojo.Batch;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFilter("dynamicFilter")
public class BatchPagingResponse {
    Integer id;
    String batchCode;
    Integer batchSize;
    Integer quantity;
    LocalDateTime imported;
    LocalDateTime manufactured;
    LocalDateTime expiration;
    String distributer;
    Integer vaccineId;
    String vaccineName;
    String vaccineDescription;
    String vaccineCode;
    String vaccineManufacturer;
    Double vaccinePrice;
    Long vaccineExpiresInDays;
    Integer vaccineMinAge;
    Integer vaccineMaxAge;
    Integer vaccineDose;

    public static BatchPagingResponse fromEntity(Batch b) {
        return BatchPagingResponse.builder()
                .id(b.getId())
                .batchCode(b.getBatchCode())
                .batchSize(b.getBatchSize())
                .quantity(b.getQuantity())
                .imported(b.getImported())
                .expiration(b.getExpiration())
                .manufactured(b.getManufactured())
                .distributer(b.getDistributer())
                .vaccineId(b.getVaccine().getId())
                .vaccineName(b.getVaccine().getName())
                .vaccineDescription(b.getVaccine().getDescription())
                .vaccineCode(b.getVaccine().getVaccineCode())
                .vaccineManufacturer(b.getVaccine().getManufacturer())
                .vaccinePrice(b.getVaccine().getPrice())
                .vaccineExpiresInDays(b.getVaccine().getExpiresInDays())
                .vaccineMinAge(b.getVaccine().getMinAge())
                .vaccineMaxAge(b.getVaccine().getMaxAge())
                .vaccineDose(b.getVaccine().getDose())
                .build();
    }

    public static Specification<Batch> filterByFields(Map<String, String> params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            params.forEach((fieldName, value) -> {
                switch (fieldName) {
                    // Integer-based fields
                    case "id", "batchSize", "quantity" ->
                            predicates.add(cb.equal(root.get(fieldName), Integer.parseInt(value)));

                    case "vaccineId" ->
                            predicates.add(cb.equal(root.get("vaccine").get("id"), Integer.parseInt(value)));

                    // String-based fields (LIKE query)
                    case "batchCode", "distributer" ->
                            predicates.add(cb.like(cb.lower(root.get(fieldName)), "%" + value.toLowerCase() + "%"));

                    case "vaccineName" ->
                            predicates.add(cb.like(cb.lower(root.get("vaccine").get("name")), "%" + value.toLowerCase() + "%"));

                    case "vaccineDescription" ->
                            predicates.add(cb.like(cb.lower(root.get("vaccine").get("description")), "%" + value.toLowerCase() + "%"));

                    case "vaccineCode" ->
                            predicates.add(cb.like(cb.lower(root.get("vaccine").get("vaccineCode")), "%" + value.toLowerCase() + "%"));

                    case "vaccineManufacturer" ->
                            predicates.add(cb.like(cb.lower(root.get("vaccine").get("manufacturer")), "%" + value.toLowerCase() + "%"));

                    // Date-based fields (greater than or equal)
                    case "imported", "expiration", "manufactured" -> {
                        LocalDateTime date = LocalDateTime.parse(value);
                        predicates.add(cb.greaterThanOrEqualTo(root.get(fieldName), date));
                    }

//                    default -> System.out.println("Skipping unknown filter: " + fieldName);
                }
            });

            predicates.add(cb.equal(root.get("deleted"), false));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
