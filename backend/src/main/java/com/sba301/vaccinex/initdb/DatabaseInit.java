//package com.sba301.vaccinex.initdb;
//
//import com.sba301.vaccinex.pojo.*;
//import com.sba301.vaccinex.pojo.composite.VaccineComboId;
//import com.sba301.vaccinex.pojo.enums.EnumRoleNameType;
//import com.sba301.vaccinex.repository.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Component
//@RequiredArgsConstructor
//public class DatabaseInit {
//
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//    private final VaccineRepository vaccineRepository;
//    private final VaccineUseRepository vaccineUseRepository;
//    private final VaccineTimingRepository vaccineTimingRepository;
//    private final BatchRepository batchRepository;
//    private final VaccineComboRepository vaccineComboRepository;
//    private final ComboRepository comboRepository;
//
//    @Bean
//    public CommandLineRunner database() {
//        return args -> {
//
//            if (roleRepository.count() == 0) {
//                Role roleAdmin = new Role(null, EnumRoleNameType.ROLE_ADMIN, "ADMIN", null);
//                Role roleDoctor = new Role(null, EnumRoleNameType.ROLE_DOCTOR, "DOCTOR", null);
//                Role roleUser = new Role(null, EnumRoleNameType.ROLE_USER, "USER", null);
//
//                roleRepository.save(roleAdmin);
//                roleRepository.save(roleDoctor);
//                roleRepository.save(roleUser);
//            }
//
//            if (userRepository.count() == 0) {
//                Role roleUser = roleRepository.getRoleByRoleName(EnumRoleNameType.ROLE_USER);
//                Role roleAdmin = roleRepository.getRoleByRoleName(EnumRoleNameType.ROLE_ADMIN);
//                Role roleDoctor = roleRepository.getRoleByRoleName(EnumRoleNameType.ROLE_DOCTOR);
//
//                User user = User.builder()
//                        .firstName("User")
//                        .lastName("User")
//                        .accessToken(null)
//                        .refreshToken(null)
//                        .email("user@gmail.com")
//                        .password(bCryptPasswordEncoder.encode("123456"))
//                        .enabled(true)
//                        .nonLocked(true)
//                        .role(roleUser)
//                        .build();
//                userRepository.save(user);
//
//                User admin = User.builder()
//                        .firstName("Admin")
//                        .lastName("Admin")
//                        .accessToken(null)
//                        .refreshToken(null)
//                        .email("admin@gmail.com")
//                        .password(bCryptPasswordEncoder.encode("123456"))
//                        .enabled(true)
//                        .nonLocked(true)
//                        .role(roleAdmin)
//                        .build();
//                userRepository.save(admin);
//
//                User doctor = User.builder()
//                        .firstName("Doctor")
//                        .lastName("Doctor")
//                        .accessToken(null)
//                        .refreshToken(null)
//                        .email("doctor@gmail.com")
//                        .password(bCryptPasswordEncoder.encode("123456"))
//                        .enabled(true)
//                        .nonLocked(true)
//                        .role(roleDoctor)
//                        .build();
//                userRepository.save(doctor);
//            }
//
//
//            if (vaccineRepository.count() == 0) {
//                List<Vaccine> vaccines = List.of(
//                        new Vaccine(null, "Pfizer-BioNTech COVID-19", "Description 1", "Code1", "Pfizer", 20.5, 120L, 12, 100, 2, null, null, null, null, null),
//                        new Vaccine(null, "Moderna COVID-19", "Description 2", "Code2", "Moderna", 18.0, 10L, 12, 100, 1, null, null, null, null, null),
//                        new Vaccine(null, "Johnson & Johnson COVID-19", "Description 3", "Code3", "Johnson & Johnson", 15.0, 15L, 18, 100, 1, null, null, null, null, null),
//                        new Vaccine(null, "AstraZeneca COVID-19", "Description 2", "Code4", "AstraZeneca", 17.5, 19L, 18, 100, 2, null, null, null, null, null),
//                        new Vaccine(null, "Sinopharm COVID-19", "Description 2", "Code5", "Sinopharm", 12.0, 19L, 3, 100, 2, null, null, null, null, null),
//                        new Vaccine(null, "Sinovac COVID-19", "Description 2", "Code6", "Sinovac", 11.5, 19L, 3, 100, 2, null, null, null, null, null),
//                        new Vaccine(null, "Hepatitis B", "Description 2", "Code7", "GSK", 25.0, 19L, 0, 100, 3, null, null, null, null, null),
//                        new Vaccine(null, "Hepatitis A", "Description 2", "Code8", "Sanofi", 22.5, 19L, 1, 100, 2, null, null, null, null, null),
//                        new Vaccine(null, "MMR (Measles, Mumps, Rubella)", "Description 2", "Code9", "Merck", 30.0, 19L, 1, 100, 2, null, null, null, null, null),
//                        new Vaccine(null, "Varicella (Chickenpox)", "Description 2", "Code10", "Merck", 28.5, 19L, 1, 100, 2, null, null, null, null, null),
//                        new Vaccine(null, "Polio (IPV)", "Description 2", "Code11", "Sanofi", 19.5, 19L, 0, 100, 4, null, null, null, null, null),
//                        new Vaccine(null, "DTaP (Diphtheria, Tetanus, Pertussis)", "Description 2", "Code12", "GSK", 35.0, 19L, 2, 100, 5, null, null, null, null, null),
//                        new Vaccine(null, "Tdap (Tetanus, Diphtheria, Pertussis)", "Description 2", "Code13", "Sanofi", 27.0, 19L, 10, 100, 1, null, null, null, null, null),
//                        new Vaccine(null, "HPV (Human Papillomavirus)", "Description 2", "Code14", "Merck", 50.0, 19L, 9, 100, 2, null, null, null, null, null),
//                        new Vaccine(null, "Meningococcal ACWY", "Description 2", "Code15", "Pfizer", 40.0, 19L, 11, 100, 1, null, null, null, null, null),
//                        new Vaccine(null, "Meningococcal B", "Description 2", "Code16", "GSK", 42.5, 19L, 11, 100, 2, null, null, null, null, null),
//                        new Vaccine(null, "Pneumococcal Conjugate (PCV13)", "Description 2", "Code17", "Pfizer", 38.0, 19L, 2, 100, 4, null, null, null, null, null),
//                        new Vaccine(null, "Pneumococcal Polysaccharide (PPSV23)", "Description 2", "Code18", "Merck", 32.5, 19L, 65, 100, 1, null, null, null, null, null),
//                        new Vaccine(null, "Influenza (Flu)", "Description 2", "Code20", "Sanofi", 15.0, 19L, 6, 100, 1, null, null, null, null, null)
//                );
//                vaccineRepository.saveAll(vaccines);
//            }
//
//            if (vaccineUseRepository.count() == 0) {
//                List<VaccineUse> vaccineUses = List.of(
//                        new VaccineUse(null, "Vaccination", "General vaccination for disease prevention.", null),
//                        new VaccineUse(null, "Booster Shot", "Additional dose to enhance immunity.", null),
//                        new VaccineUse(null, "Travel Requirement", "Vaccination required for international travel.", null),
//                        new VaccineUse(null, "Work Requirement", "Mandatory vaccination for workplace safety.", null),
//                        new VaccineUse(null, "School Admission", "Vaccination required for school enrollment.", null),
//                        new VaccineUse(null, "Health Checkup", "Vaccination as part of routine health checkup.", null),
//                        new VaccineUse(null, "Epidemic Prevention", "Vaccination for outbreak and epidemic control.", null),
//                        new VaccineUse(null, "Military Service", "Required vaccination for military personnel.", null),
//                        new VaccineUse(null, "Elderly Care", "Recommended vaccines for senior citizens.", null),
//                        new VaccineUse(null, "Child Immunization", "Essential vaccines for newborns and children.", null)
//                );
//                vaccineUseRepository.saveAll(vaccineUses);
//            }
//
//            if (vaccineRepository.count() > 0 && vaccineUseRepository.count() > 0) {
//                List<Vaccine> vaccines = vaccineRepository.findAll();
//                List<VaccineUse> vaccineUses = vaccineUseRepository.findAll();
//
//                Random random = new Random();
//
//                for (Vaccine vaccine : vaccines) {
//                    Set<VaccineUse> assignedUses = new HashSet<>();
//                    int numUses = random.nextInt(2) + 2;
//
//                    while (assignedUses.size() < numUses) {
//                        assignedUses.add(vaccineUses.get(random.nextInt(vaccineUses.size())));
//                    }
//                    vaccine.setUses(new ArrayList<>(assignedUses));
//
//                    for (VaccineUse vaccineUse : assignedUses) {
//                        vaccineUse.getVaccines().add(vaccine);
//                    }
//                }
//                vaccineRepository.saveAll(vaccines);
//                vaccineUseRepository.saveAll(vaccineUses);
//            }
//
//            if (vaccineTimingRepository.count() == 0) {
//                List<Vaccine> vaccines = vaccineRepository.findAll();
//                List<VaccineTiming> vaccineTimings = new ArrayList<>();
//
//                for (Vaccine vaccine : vaccines) {
//                    int numberOfDoses = vaccine.getDose();
//                    int daysGap = 30;
//
//                    for (int doseNo = 1; doseNo <= numberOfDoses; doseNo++) {
//
//                        if (doseNo == 1) {
//                            continue;
//                        }
//
//                        VaccineTiming vaccineTiming = new VaccineTiming();
//                        vaccineTiming.setDoseNo(doseNo);
//                        vaccineTiming.setVaccine(vaccine);
////                        vaccineTiming.setDaysAfterPreviousDose(doseNo == 1 ? 0 : daysGap);
//                        vaccineTiming.setDaysAfterPreviousDose(daysGap);
//
//                        vaccineTimings.add(vaccineTiming);
//                    }
//                }
//
//                vaccineTimingRepository.saveAll(vaccineTimings);
//            }
//
//            if (vaccineComboRepository.count() == 0) {
//                // Fetch all vaccines from the database
//                List<Vaccine> vaccines = vaccineRepository.findAll();
//                Map<String, Vaccine> vaccineMap = vaccines.stream()
//                        .collect(Collectors.toMap(Vaccine::getName, v -> v));
//
//                // Define common combos with descriptions, prices, and age limits
//                List<Combo> combos = List.of(
//                        new Combo(null, "MMR Combo", "Measles, Mumps, and Rubella protection", 50.0, 1, 12, null, null, null),
//                        new Combo(null, "DTaP Combo", "Diphtheria, Tetanus, and Pertussis protection", 45.0, 2, 6, null, null, null),
//                        new Combo(null, "COVID-19 + Flu Combo", "Protection against COVID-19 and seasonal flu", 70.0, 12, 100, null, null, null),
//                        new Combo(null, "Meningococcal Combo", "Protection against meningococcal disease", 60.0, 11, 25, null, null, null),
//                        new Combo(null, "Pneumococcal Combo", "Protection against pneumococcal infections", 55.0, 2, 65, null, null, null)
//                );
//
//                // Save combos to DB
//                comboRepository.saveAll(combos);
//
//                // Fetch saved combos from DB
//                Map<String, Combo> comboMap = comboRepository.findAll().stream()
//                        .collect(Collectors.toMap(Combo::getName, c -> c));
//
//                // Create vaccine combos
////                List<VaccineCombo> vaccineCombos = List.of(
////                    new VaccineCombo(new VaccineComboId(vaccineMap.get("MMR (Measles, Mumps, Rubella)").getId(), comboMap.get("MMR Combo").getId()),
////                            vaccineMap.get("MMR (Measles, Mumps, Rubella)").getDose(),
////                            comboMap.get("MMR Combo"),
////                            vaccineMap.get("MMR (Measles, Mumps, Rubella)")),
////                    new VaccineCombo(new VaccineComboId(vaccineMap.get("Hepatitis A").getId(), comboMap.get("MMR Combo").getId()),
////                            vaccineMap.get("Hepatitis A").getDose(),
////                            comboMap.get("MMR Combo"),
////                            vaccineMap.get("Hepatitis A")),
////
////                    new VaccineCombo(new VaccineComboId(vaccineMap.get("DTaP (Diphtheria, Tetanus, Pertussis)").getId(), comboMap.get("DTaP Combo").getId()),
////                            vaccineMap.get("DTaP (Diphtheria, Tetanus, Pertussis)").getDose(),
////                            comboMap.get("DTaP Combo"),
////                            vaccineMap.get("DTaP (Diphtheria, Tetanus, Pertussis)")),
////
////                    new VaccineCombo(new VaccineComboId(vaccineMap.get("Hepatitis B").getId(), comboMap.get("DTaP Combo").getId()),
////                            vaccineMap.get("Hepatitis B").getDose(),
////                            comboMap.get("DTaP Combo"),
////                            vaccineMap.get("Hepatitis B")),
////
////                    new VaccineCombo(new VaccineComboId(vaccineMap.get("Pfizer-BioNTech COVID-19").getId(), comboMap.get("COVID-19 + Flu Combo").getId()),
////                            vaccineMap.get("Pfizer-BioNTech COVID-19").getDose(),
////                            comboMap.get("COVID-19 + Flu Combo"),
////                            vaccineMap.get("Pfizer-BioNTech COVID-19")),
////
////                    new VaccineCombo(new VaccineComboId(vaccineMap.get("Influenza (Flu)").getId(), comboMap.get("COVID-19 + Flu Combo").getId()),
////                            vaccineMap.get("Influenza (Flu)").getDose(),
////                            comboMap.get("COVID-19 + Flu Combo"),
////                            vaccineMap.get("Influenza (Flu)")),
////
////                    new VaccineCombo(new VaccineComboId(vaccineMap.get("HPV (Human Papillomavirus)").getId(), comboMap.get("Meningococcal Combo").getId()),
////                            vaccineMap.get("HPV (Human Papillomavirus)").getDose(),
////                            comboMap.get("Meningococcal Combo"),
////                            vaccineMap.get("Influenza (Flu)"))
////
////                );
////
////
////                // Save vaccine combos
////                vaccineComboRepository.saveAll(vaccineCombos);
//            }
//
//            if (batchRepository.count() == 0) {
//                List<Batch> batches = new ArrayList<>();
//
//                for (Vaccine v : vaccineRepository.findAll()) {
//                    batches.add(Batch.builder()
//                            .id(null)
//                            .batchCode("BATCH-" + v.getVaccineCode())
//                            .batchSize(500)
//                            .quantity(500)
//                            .imported(LocalDateTime.now())
//                            .expiration(LocalDateTime.now().plusMonths(6))
//                            .distributer("Distributor for " + v.getName())
//                            .vaccine(v)
//                            .transactions(null)
//                            .vaccineSchedules(null)
//                            .build());
//                }
//
//                batchRepository.saveAll(batches);
//            }
//        };
//    }
//}
