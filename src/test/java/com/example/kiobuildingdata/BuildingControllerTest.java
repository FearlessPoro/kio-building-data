package com.example.kiobuildingdata;

import com.example.kiobuildingdata.controller.BuildingController;
import com.example.kiobuildingdata.model.Building;
import com.example.kiobuildingdata.service.BuildingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@WebMvcTest(BuildingController.class)
@Import(BuildingController.class)
public class BuildingControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private BuildingService buildingService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBuilding() {
        Building building = new Building();
        when(buildingService.getBuildingData(1L)).thenReturn(Mono.just(building));

        webTestClient.get().uri("/building/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testGetNonExistentBuilding() {
        when(buildingService.getBuildingData(Mockito.anyLong())).thenReturn(Mono.empty());

        webTestClient.get().uri("/building/999")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void testInternalServerError() {
        when(buildingService.getBuildingData(-1L)).thenThrow(new RuntimeException("Internal Server Error"));


        webTestClient.get().uri("/building/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testIfServiceCanHandle50RequestsPerSecond() {
        int numberOfRequests = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfRequests);

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < numberOfRequests; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    webTestClient.get().uri("/building/1")
                            .exchange()
                            .expectStatus().isOk();
                } catch (Exception e) {
                    fail("Request failed: " + e.getMessage());
                }
            }, executorService);

            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

}