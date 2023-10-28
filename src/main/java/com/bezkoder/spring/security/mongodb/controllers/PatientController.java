package com.bezkoder.spring.security.mongodb.controllers;

import com.bezkoder.spring.security.mongodb.models.Patient;
import com.bezkoder.spring.security.mongodb.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(allowedHeaders = "*", origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    PatientRepository patientRepository;

    @PostMapping("/create")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        try {
            Patient patient1 = patientRepository.save(new Patient(
                    patient.getId(),
                    patient.getName(),
                    patient.getAge(),
                    patient.getGender(),
                    patient.getCountry(),
                    patient.getHeight(),
                    patient.getWeight(),
                    patient.getHeartRate(),
                    patient.getBloodPressure(),
                    patient.getSugarLevel(),
                    patient.getAllergies(),
                    patient.getSymptoms(),
                    patient.getDiseases(),
                    patient.getTreatments(),
                    patient.getMedicines()
            ));
            return  new ResponseEntity<>(patient1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-all-patients")
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
}
