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
    private List<String> allergies;
    private List<String> symptoms;
    private List<String> diseases;
    private List<String> treatments;
    private List<String> medicines;
}
