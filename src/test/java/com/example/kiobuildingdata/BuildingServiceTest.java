package com.example.kiobuildingdata;

import com.example.kiobuildingdata.model.Building;
import com.example.kiobuildingdata.service.BuildingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.client.WebClient.*;

@SpringBootTest
public class BuildingServiceTest {

    @Mock
    private Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RequestHeadersSpec requestHeadersSpec;

    @Mock
    private ResponseSpec responseSpec;

    private BuildingService buildingService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(webClientBuilder.baseUrl(any(String.class))).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class), any(Long.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);


        buildingService = new BuildingService(webClientBuilder, "https://localhost:8080");
    }

    @Test
    public void testGetBuildingData() {
        when(webClientBuilder.baseUrl(any(String.class))).thenReturn(webClientBuilder);

        JsonNode buildingNode = setupBuildingNode();

        when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(Mono.just(buildingNode));

        Building building = buildingService.getBuildingData(1L).block();

        assertEquals(1L, building.getId());
        assertEquals("Building 1", building.getName());
        assertEquals("123 Street", building.getAddress());

        assertEquals(1, building.getFloors().size());
        assertEquals(1L, building.getFloors().get(0).getId());
        assertEquals(1, building.getFloors().get(0).getLevel());
        assertEquals("geojson", building.getFloors().get(0).getImageXyGeojson());
        assertEquals(Arrays.asList("property1", "property2"), building.getFloors().get(0).getProperties());
    }

    private static JsonNode setupBuildingNode() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode buildingNode = objectMapper.createObjectNode()
                .put("id", 1L)
                .put("name", "Building 1")
                .put("address", "123 Street");

        ObjectNode floorNode = objectMapper.createObjectNode()
                .put("id", 1L)
                .put("level", 1)
                .put("imageXyGeojson", "geojson");

        // Create an ArrayNode object that represents properties
        ArrayNode propertiesNode = objectMapper.createArrayNode();
        propertiesNode.add("property1").add("property2");

        // Add the propertiesNode object to the floorNode object
        floorNode.set("properties", propertiesNode);

        ArrayNode floorsNode = objectMapper.createArrayNode();
        floorsNode.add(floorNode);

        buildingNode.set("floors", floorsNode);
        return buildingNode;
    }

    @Test
    public void testGetBuildingDataReturnsEmpty() {
        when(webClientBuilder.baseUrl(any(String.class))).thenReturn(webClientBuilder);

        when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(Mono.empty());

        Building building = buildingService.getBuildingData(1L).block();

        assertNull(building);
    }
}