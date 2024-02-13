package com.example.kiobuildingdata.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Building {

    @Id
    private Long id;

    @Size(min = 1, message = "Name must not be empty")
    private String name;

    @Size(min = 1, message = "Address must not be empty")
    private String address;

    @NotEmpty
    @OneToMany
    private List<Floor> floors;
}
