package com.example.kiobuildingdata.controller;

import com.example.kiobuildingdata.model.Building;
import com.example.kiobuildingdata.service.BuildingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/building")
public class BuildingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuildingController.class);

    private final BuildingService buildingService;

    @Autowired
    public BuildingController(final BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @GetMapping("/{buildingId}")
    public Mono<ResponseEntity<Building>> getBuildingById(final Long buildingId) {
        LOGGER.info("Getting building with id: {}", buildingId);
        return buildingService.getBuildingData(buildingId)
                .map(building -> {
                    LOGGER.info("Building information retrieved: {}", building);
                    return ResponseEntity.ok(building);
                })
                .onErrorResume(e -> {
                    LOGGER.error("Error retrieving building with id: {}", buildingId, e);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }


}
