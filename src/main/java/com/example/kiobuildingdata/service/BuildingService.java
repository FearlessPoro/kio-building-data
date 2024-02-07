package com.example.kiobuildingdata.service;


import com.example.kiobuildingdata.model.Building;
import com.example.kiobuildingdata.model.Floor;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class BuildingService {

    private final WebClient webClient;
    Logger LOGGER = LoggerFactory.getLogger(BuildingService.class);

    @Autowired
    public BuildingService(final WebClient.Builder webClientBuilder, @Value("${kontakt.io.url}") String kontaktIoUrl) {
        this.webClient = webClientBuilder.baseUrl(kontaktIoUrl).build();
    }

    public Mono<Building> getBuildingData(final Long buildingId) {
        return webClient.get()
                .uri("locations/buildings/{buildingId}", buildingId)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(this::mapToBuilding);
    }

    private Building mapToBuilding(JsonNode jsonNode) {
        LOGGER.info("Mapping building data to Building object.");
        Building building = new Building();
        building.setId(jsonNode.get("id").asLong());
        building.setName(jsonNode.get("name").asText());
        building.setAddress(jsonNode.get("address").asText());

        List<Floor> floors = new ArrayList<>();
        jsonNode.get("floors").forEach(floorNode -> {
            Floor floor = new Floor();
            floor.setId(floorNode.get("id").asLong());
            floor.setLevel(floorNode.get("level").asInt());
            floor.setImageXyGeojson(floorNode.get("imageXyGeojson").asText());
            List<String> properties = new ArrayList<>();
            floorNode.get("properties").elements().forEachRemaining(propertyNode -> properties.add(propertyNode.asText()));
            floor.setProperties(properties);
            floors.add(floor);
        });
        building.setFloors(floors);

        return building;
    }
}
