package com.example.kiobuildingdata.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Floor {

    @Id
    private Long id;

    private Integer level;

    @Lob
    private String floorPlan;

    @ElementCollection
    private List<String> properties;

}
