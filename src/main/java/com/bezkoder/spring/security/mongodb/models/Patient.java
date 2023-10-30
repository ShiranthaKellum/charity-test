package com.bezkoder.spring.security.mongodb.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "patient")
public class Patient {
    @Id
    private String id;
    private String name;
    private String age;
    private String gender;
    private String country;
    private String height;
    private String weight;
    private String heartRate;
    private String bloodPressure;
    private String sugarLevel;
    private String allergies;
    private String symptoms;
    private String diseases;
    private String treatments;
    private String medicines;
    private String history;
    private String observations;
}
