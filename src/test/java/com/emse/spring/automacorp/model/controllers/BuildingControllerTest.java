package com.emse.spring.automacorp.model.controllers;

import com.emse.spring.automacorp.model.SensorType;
import com.emse.spring.automacorp.model.dao.*;
import com.emse.spring.automacorp.model.entities.*;
import com.emse.spring.automacorp.model.records.dto.BuildingCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(BuildingController.class)
class BuildingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BuildingDao buildingDao;

    @MockBean
    private RoomDao roomDao;

    @MockBean
    private WindowDao windowDao;

    @MockBean
    private HeaterDao heaterDao;

    @MockBean
    private SensorDao1 sensorDao1;

    SensorEntity createSensorEntity() {
        SensorEntity sensorEntity = new SensorEntity(SensorType.TEMPERATURE, "Sensor 1");
        sensorEntity.setId(1L);
        sensorEntity.setValue(24.2);
        return sensorEntity;
    }

    BuildingEntity createBuildingEntity(Long id, String name, SensorEntity outsideTemp) {
        BuildingEntity building = new BuildingEntity(name, outsideTemp, List.of());
        building.setId(id);
        return building;
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldFindAll() throws Exception {
        BuildingEntity building1 = createBuildingEntity(1L, "Building 1", new SensorEntity());
        BuildingEntity building2 = createBuildingEntity(2L, "Building 2", new SensorEntity());

        Mockito.when(buildingDao.findAll()).thenReturn(List.of(building1, building2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/buildings").accept(MediaType.APPLICATION_JSON))
                // check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                // the content can be tested with Json path
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id").value(Matchers.containsInAnyOrder(1, 2)));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldReturnNullWhenFindByUnknownId() throws Exception {
        Mockito.when(buildingDao.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/buildings/999").accept(MediaType.APPLICATION_JSON))
                // check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                // the content can be tested with Json path
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldFindById() throws Exception {
        BuildingEntity building = createBuildingEntity(1L, "Building 1", new SensorEntity());
        Mockito.when(buildingDao.findById(1L)).thenReturn(Optional.of(building));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/buildings/1").accept(MediaType.APPLICATION_JSON))
                // check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                // the content can be tested with Json path
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldNotUpdateUnknownEntity() throws Exception {
        BuildingEntity buildingEntity = createBuildingEntity(1L, "Building 1", new SensorEntity());
        BuildingCommand expectedBuilding = new BuildingCommand(buildingEntity.getName(), buildingEntity.getOutsideTemperature().getValue());
        String json = objectMapper.writeValueAsString(expectedBuilding);

        Mockito.when(buildingDao.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/buildings/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .with(csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldCreate() throws Exception {
        // Setup initial entities
        SensorEntity sensorEntity = new SensorEntity(SensorType.TEMPERATURE, "Outside temperature Building 1");
        BuildingEntity buildingEntity = new BuildingEntity("Building 1", sensorEntity, List.of());

        Double outsideTemperature = 15.0;
        BuildingCommand expectedBuilding = new BuildingCommand("Building 1", outsideTemperature);
        String json = objectMapper.writeValueAsString(expectedBuilding);

        // Mocking the save methods
        Mockito.when(sensorDao1.save(Mockito.any(SensorEntity.class))).thenReturn(sensorEntity);
        Mockito.when(buildingDao.save(Mockito.any(BuildingEntity.class))).thenReturn(buildingEntity);

        // Perform the POST request
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/buildings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .with(csrf())
                )
                // Check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Building 1"));

        // Verify that the sensorDao1.save method was called with the new sensor entity
        ArgumentCaptor<SensorEntity> sensorEntityCaptor = ArgumentCaptor.forClass(SensorEntity.class);
        Mockito.verify(sensorDao1).save(sensorEntityCaptor.capture());
        SensorEntity createdSensorEntity = sensorEntityCaptor.getValue();

        // Assert the new sensor's value reflects the specified outside temperature
        Assertions.assertThat(createdSensorEntity.getValue()).isEqualTo(outsideTemperature);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldUpdate() throws Exception {
        // Setup initial entities
        SensorEntity sensorEntity = createSensorEntity();
        BuildingEntity buildingEntity = createBuildingEntity(1L, "Building 1", sensorEntity);

        Double newOutsideTemperature = 18.0;
        BuildingCommand updatedBuilding = new BuildingCommand("Building 1 Updated", newOutsideTemperature);
        String json = objectMapper.writeValueAsString(updatedBuilding);

        // Mocking the findById and save methods
        Mockito.when(buildingDao.findById(1L)).thenReturn(Optional.of(buildingEntity));
        Mockito.when(sensorDao1.findById(sensorEntity.getId())).thenReturn(Optional.of(sensorEntity));
        Mockito.when(buildingDao.save(Mockito.any(BuildingEntity.class))).thenReturn(buildingEntity);

        // Perform the PUT request
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/buildings/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .with(csrf())
                )
                // Check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Building 1 Updated"));

        // Verify that the sensorDao1.save method was called with the updated sensor entity
        ArgumentCaptor<SensorEntity> sensorEntityCaptor = ArgumentCaptor.forClass(SensorEntity.class);
        Mockito.verify(sensorDao1).save(sensorEntityCaptor.capture());
        SensorEntity updatedSensorEntity = sensorEntityCaptor.getValue();

        // Assert the sensor's value reflects the updated building's outside temperature
        Assertions.assertThat(updatedSensorEntity.getValue()).isEqualTo(newOutsideTemperature);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldDeleteBuildingAndAssociatedEntities() throws Exception {
        SensorEntity sensor = createSensorEntity();
        sensor.setId(1L);

        RoomEntity room1 = new RoomEntity();
        RoomEntity room2 = new RoomEntity();
        room1.setId(1L);
        room2.setId(2L);

        WindowEntity window1 = new WindowEntity();
        WindowEntity window2 = new WindowEntity();
        window1.setId(1L);
        window2.setId(2L);

        HeaterEntity heater1 = new HeaterEntity();
        HeaterEntity heater2 = new HeaterEntity();
        heater1.setId(1L);
        heater2.setId(2L);

        room1.setWindows(List.of(window1));
        room1.setHeaters(List.of(heater1));
        room2.setWindows(List.of(window2));
        room2.setHeaters(List.of(heater2));

        BuildingEntity building = createBuildingEntity(1L, "Building 1", sensor);
        building.setRooms(List.of(room1, room2));

        Mockito.when(buildingDao.findById(1L)).thenReturn(Optional.of(building));
        Mockito.doNothing().when(sensorDao1).deleteById(Mockito.any());
        Mockito.doNothing().when(windowDao).deleteById(Mockito.any());
        Mockito.doNothing().when(heaterDao).deleteById(Mockito.any());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/buildings/1").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(windowDao).deleteByRoom(1L);
        Mockito.verify(windowDao).deleteByRoom(2L);
        Mockito.verify(heaterDao).deleteByRoom(1L);
        Mockito.verify(heaterDao).deleteByRoom(2L);
        Mockito.verify(buildingDao).deleteById(1L);
        Mockito.verify(roomDao).deleteById(1L);
        Mockito.verify(roomDao).deleteById(2L);
    }
}
