package com.example.kiobuildingdata;

import com.example.kiobuildingdata.controller.BuildingController;
import com.example.kiobuildingdata.model.Building;
import com.example.kiobuildingdata.service.BuildingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BuildingController.class)
@Import(BuildingController.class)
public class BuildingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BuildingService buildingService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBuilding() throws Exception {
        Building building = new Building();
        when(buildingService.getBuildingData(1L)).thenReturn(Mono.just(building));

        mockMvc.perform(MockMvcRequestBuilders.get("/building/1"))
                .andExpect(status().isOk());
    }


}