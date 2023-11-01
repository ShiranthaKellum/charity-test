package com.bezkoder.spring.security.mongodb.controllers;

import com.bezkoder.spring.security.mongodb.models.Patient;
import com.bezkoder.spring.security.mongodb.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
                    patient.getMedicines(),
                    patient.getHistory(),
                    patient.getObservations()
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

    @GetMapping("/get-patient/{id}")
    public Optional<Patient> getPatient(@PathVariable String id) {
        return patientRepository.findById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable String id, @RequestBody Patient updatedPatient) {
        Patient existingPatient = patientRepository.findById(id)
                .orElse(null);

        if (existingPatient != null) {
            if (updatedPatient.getName() != null) {
                existingPatient.setName(updatedPatient.getName());
            }
            if (updatedPatient.getAge() != null) {
                existingPatient.setAge(updatedPatient.getAge());
            }
            if (updatedPatient.getGender() != null) {
                existingPatient.setGender(updatedPatient.getGender());
            }
            if (updatedPatient.getCountry() != null) {
                existingPatient.setCountry(updatedPatient.getCountry());
            }
            if (updatedPatient.getHeight() != null) {
                existingPatient.setHeight(updatedPatient.getHeight());
            }
            if (updatedPatient.getWeight() != null) {
                existingPatient.setWeight(updatedPatient.getWeight());
            }
            if (updatedPatient.getHeartRate() != null) {
                existingPatient.setHeartRate(updatedPatient.getHeartRate());
            }
            if (updatedPatient.getBloodPressure() != null) {
                existingPatient.setBloodPressure(updatedPatient.getBloodPressure());
            }
            if (updatedPatient.getSugarLevel() != null) {
                existingPatient.setSugarLevel(updatedPatient.getSugarLevel());
            }
            if (updatedPatient.getAllergies() != null) {
                existingPatient.setAllergies(updatedPatient.getAllergies());
            }
            if (updatedPatient.getSymptoms() != null) {
                existingPatient.setSymptoms(updatedPatient.getSymptoms());
            }
            if (updatedPatient.getDiseases() != null) {
                existingPatient.setDiseases(updatedPatient.getDiseases());
            }
            if (updatedPatient.getTreatments() != null) {
                existingPatient.setTreatments(updatedPatient.getTreatments());
            }
            if (updatedPatient.getMedicines() != null) {
                existingPatient.setMedicines(updatedPatient.getMedicines());
            }
            if (updatedPatient.getHistory() != null) {
                existingPatient.setHistory(updatedPatient.getHistory());
            }
            if (updatedPatient.getObservations() != null) {
                existingPatient.setObservations(updatedPatient.getObservations());
            }

            return new ResponseEntity<>(patientRepository.save(existingPatient), HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
