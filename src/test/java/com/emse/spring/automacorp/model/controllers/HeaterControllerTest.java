package com.emse.spring.automacorp.model.controllers;

import com.emse.spring.automacorp.model.HeaterStatus;
import com.emse.spring.automacorp.model.SensorType;
import com.emse.spring.automacorp.model.dao.*;
import com.emse.spring.automacorp.model.entities.*;
import com.emse.spring.automacorp.model.records.dto.HeaterCommand;
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

@WebMvcTest(HeaterController.class)
class HeaterControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HeaterDao heaterDao;

    @MockBean
    private RoomDao roomDao;

    @MockBean
    private SensorDao1 sensorDao1;

    @MockBean
    private BuildingDao buildingDao;

    HeaterEntity createHeaterEntity(Long id, String name, RoomEntity room, SensorEntity status) {
        HeaterEntity heater = new HeaterEntity(name, room, status);
        heater.setId(id);
        return heater;
    }

    SensorEntity createSensorEntity() {
        SensorEntity sensor = new SensorEntity(SensorType.TEMPERATURE, "Sensor 1");
        sensor.setId(1L);
        sensor.setValue(24.2);
        return sensor;
    }

    RoomEntity createRoomEntity(Long id, String name, SensorEntity currentTemp, int floor, double targetTemp, List<WindowEntity> windows, List<HeaterEntity> heaters, BuildingEntity building) {
        RoomEntity roomEntity = new RoomEntity(name, currentTemp, floor);
        roomEntity.setId(id);
        roomEntity.setTargetTemp(targetTemp);
        roomEntity.setWindows(windows);
        roomEntity.setHeaters(heaters);
        roomEntity.setBuilding(building);
        return roomEntity;
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldFindAll() throws Exception {
        SensorEntity sensor1 = createSensorEntity();
        RoomEntity room1 = createRoomEntity(1L, "Room 1", sensor1, 3, 21.0, List.of(), List.of(), null);

        HeaterEntity heater1 = createHeaterEntity(1L, "Heater 1", room1, sensor1);
        HeaterEntity heater2 = createHeaterEntity(2L, "Heater 2", room1, sensor1);

        Mockito.when(heaterDao.findAll()).thenReturn(List.of(heater1, heater2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/heaters").accept(MediaType.APPLICATION_JSON))
                // check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                // the content can be tested with Json path
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id").value(Matchers.containsInAnyOrder(1, 2)));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldReturnNullWhenFindByUnknownId() throws Exception {
        Mockito.when(heaterDao.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/heaters/999").accept(MediaType.APPLICATION_JSON))
                // check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                // the content can be tested with Json path
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldFindById() throws Exception {
        SensorEntity sensor1 = createSensorEntity();
        RoomEntity room1 = createRoomEntity(1L, "Room 1", sensor1, 3, 21.0, List.of(), List.of(), null);
        HeaterEntity heater = createHeaterEntity(1L, "Heater 1", room1, sensor1);

        Mockito.when(heaterDao.findById(1L)).thenReturn(Optional.of(heater));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/heaters/1").accept(MediaType.APPLICATION_JSON))
                // check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                // the content can be tested with Json path
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldNotUpdateUnknownEntity() throws Exception {
        HeaterEntity heaterEntity = createHeaterEntity(1L, "Heater 1", new RoomEntity(), new SensorEntity());
        HeaterCommand expectedHeater = new HeaterCommand(heaterEntity.getName(), HeaterStatus.ON, heaterEntity.getStatus().getId());
        String json = objectMapper.writeValueAsString(expectedHeater);

        Mockito.when(heaterDao.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/heaters/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .with(csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldUpdate() throws Exception {
        // Setup initial entities
        SensorEntity sensor1 = createSensorEntity();
        RoomEntity room = createRoomEntity(1L, "Room 1", sensor1, 3, 21.0, List.of(), List.of(), null);
        HeaterEntity heater = createHeaterEntity(1L, "Heater 1", room, sensor1);

        // Change the status for the test
        HeaterStatus newStatus = HeaterStatus.ON;
        HeaterCommand updatedHeater = new HeaterCommand(heater.getName(), newStatus, heater.getRoom().getId());
        String json = objectMapper.writeValueAsString(updatedHeater);

        // Mocking the findById and save methods
        Mockito.when(heaterDao.findById(1L)).thenReturn(Optional.of(heater));
        Mockito.when(sensorDao1.findById(1L)).thenReturn(Optional.of(sensor1));
        Mockito.when(roomDao.findById(1L)).thenReturn(Optional.of(room));
        Mockito.when(heaterDao.save(Mockito.any(HeaterEntity.class))).thenReturn(heater);

        // Perform the PUT request
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/heaters/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .with(csrf())
                )
                // Check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Heater 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"));

        // Verify that the sensorDao1.save method was called with the updated sensor entity
        ArgumentCaptor<SensorEntity> sensorEntityCaptor = ArgumentCaptor.forClass(SensorEntity.class);
        Mockito.verify(sensorDao1).save(sensorEntityCaptor.capture());
        SensorEntity updatedSensorEntity = sensorEntityCaptor.getValue();

        // Assert the sensor's value reflects the updated heater status
        double expectedSensorValue = 1.0;
        Assertions.assertThat(updatedSensorEntity.getValue()).isEqualTo(expectedSensorValue);
    }


    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldCreate() throws Exception {
        // Setup initial entities
        SensorEntity sensor = createSensorEntity();
        BuildingEntity buildingEntity = new BuildingEntity("EF", sensor, List.of());
        buildingEntity.setId(1L);
        RoomEntity room = createRoomEntity(2L, "Room 2", sensor, 1, 23.0, List.of(), List.of(), buildingEntity);
        HeaterEntity heater = createHeaterEntity(1L, "Heater 1 Room 2 EF", room, sensor);

        // Create a new HeaterCommand with a specific status
        HeaterStatus newStatus = HeaterStatus.ON;
        HeaterCommand newHeater = new HeaterCommand(heater.getName(), newStatus, heater.getRoom().getId());
        String json = objectMapper.writeValueAsString(newHeater);

        // Mocking the save methods
        Mockito.when(buildingDao.findById(1L)).thenReturn(Optional.of(buildingEntity));
        Mockito.when(roomDao.findById(2L)).thenReturn(Optional.of(room));
        Mockito.when(heaterDao.save(Mockito.any(HeaterEntity.class))).thenReturn(heater);

        // Perform the POST request
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/heaters")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .with(csrf())
                )
                // Check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Heater 1 Room 2 EF"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"));

        // Verify that the sensorDao1.save method was called with the new sensor entity
        ArgumentCaptor<SensorEntity> sensorEntityCaptor = ArgumentCaptor.forClass(SensorEntity.class);
        Mockito.verify(sensorDao1).save(sensorEntityCaptor.capture());
        SensorEntity createdSensorEntity = sensorEntityCaptor.getValue();

        // Assert the new sensor's value reflects the specified heater status
        double expectedSensorValue = 1.0;
        Assertions.assertThat(createdSensorEntity.getValue()).isEqualTo(expectedSensorValue);
    }


    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldDelete() throws Exception {
        HeaterEntity heater = new HeaterEntity();
        heater.setId(1L);
        SensorEntity status = new SensorEntity();
        status.setId(1L);
        heater.setStatus(status);

        Mockito.when(sensorDao1.findById(1L)).thenReturn(Optional.of(status));
        Mockito.when(heaterDao.findById(1L)).thenReturn(Optional.of(heater));
        Mockito.when(heaterDao.save(Mockito.any(HeaterEntity.class))).thenReturn(heater);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/heaters/1").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldSwitchHeaterStatusById() throws Exception {
        SensorEntity sensor1 = createSensorEntity();
        sensor1.setValue(0.0);
        RoomEntity room1 = createRoomEntity(1L, "Room 1", sensor1, 3, 21.0, List.of(), List.of(), null);
        HeaterEntity heater = createHeaterEntity(1L, "Heater 1", room1, sensor1);

        Mockito.when(heaterDao.findById(1L)).thenReturn(Optional.of(heater));
        Mockito.when(heaterDao.save(Mockito.any(HeaterEntity.class))).thenReturn(heater);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/heaters/1/switch").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assertions.assertThat(heater.getStatus().getValue()).isEqualTo(1.0);
    }
}
