package com.emse.spring.automacorp.model.controllers;

import com.emse.spring.automacorp.model.dao.SensorDao1;
import com.emse.spring.automacorp.model.entities.SensorEntity;
import com.emse.spring.automacorp.model.SensorType;
import com.emse.spring.automacorp.model.records.dto.SensorCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
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


@WebMvcTest(SensorController.class)
class SensorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SensorDao1 sensorDao;

    SensorEntity createSensorEntity(Long id, String name) {
        SensorEntity sensorEntity = new SensorEntity(SensorType.TEMPERATURE, name);
        sensorEntity.setId(id);
        sensorEntity.setValue(24.2);
        return sensorEntity;
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldFindAll() throws Exception {
        Mockito.when(sensorDao.findAll()).thenReturn(List.of(
                createSensorEntity(1L, "Temperature room 1"),
                createSensorEntity(2L, "Temperature room 2")
        ));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/sensors").accept(MediaType.APPLICATION_JSON))
                // check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                // the content can be tested with Json path
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath("[*].name")
                                .value(Matchers.containsInAnyOrder("Temperature room 1", "Temperature room 2"))
                );
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldReturnNullWhenFindByUnknownId() throws Exception {
        Mockito.when(sensorDao.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/sensors/999").accept(MediaType.APPLICATION_JSON))
                // check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                // the content can be tested with Json path
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldFindById() throws Exception {
        SensorEntity sensorEntity = createSensorEntity(1L, "Temperature room 1");
        Mockito.when(sensorDao.findById(999L)).thenReturn(Optional.of(sensorEntity));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/sensors/999").accept(MediaType.APPLICATION_JSON))
                // check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                // the content can be tested with Json path
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Temperature room 1"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldNotUpdateUnknownEntity() throws Exception {
        SensorEntity sensorEntity = createSensorEntity(1L, "Temperature room 1");
        SensorCommand expectedSensor = new SensorCommand(sensorEntity.getName(), sensorEntity.getValue(), sensorEntity.getSensorType());
        String json = objectMapper.writeValueAsString(expectedSensor);

        Mockito.when(sensorDao.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/sensors/1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .with(csrf())
                )
                // check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldUpdate() throws Exception {
        SensorEntity sensorEntity = createSensorEntity(1L, "Temperature room 1");
        SensorCommand expectedSensor = new SensorCommand(sensorEntity.getName(), sensorEntity.getValue(), sensorEntity.getSensorType());
        String json = objectMapper.writeValueAsString(expectedSensor);

        Mockito.when(sensorDao.findById(1L)).thenReturn(Optional.of(sensorEntity));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/sensors/1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .with(csrf())
                )
                // check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Temperature room 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldCreate() throws Exception {
        SensorEntity sensorEntity = createSensorEntity(1L, "Temperature room 1");
        SensorCommand expectedSensor = new SensorCommand(sensorEntity.getName(), sensorEntity.getValue(), sensorEntity.getSensorType());
        String json = objectMapper.writeValueAsString(expectedSensor);

        Mockito.when(sensorDao.existsById(1L)).thenReturn(false);
        Mockito.when(sensorDao.save(Mockito.any(SensorEntity.class))).thenReturn(sensorEntity);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/sensors")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .with(csrf())
                )
                // check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Temperature room 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/sensors/999").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}