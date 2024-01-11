package com.emse.spring.automacorp.model.controllers;

import com.emse.spring.automacorp.model.dao.SensorDao1;
import com.emse.spring.automacorp.model.entities.SensorEntity;
import com.emse.spring.automacorp.model.mappers.SensorMapper;
import com.emse.spring.automacorp.model.records.dao.Sensor;
import com.emse.spring.automacorp.model.records.dto.SensorCommand;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/sensors")
@Transactional
public class SensorController {
    private final SensorDao1 sensorDao;

    public SensorController(SensorDao1 sensorDao) {
        this.sensorDao = sensorDao;
    }

    @GetMapping
    public List<Sensor> findAll() {
        return sensorDao.findAll()
                .stream()
                .map(SensorMapper::of)
                .sorted(Comparator.comparing(Sensor::name))
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public Sensor findById(@PathVariable Long id) {
        return sensorDao.findById(id).map(SensorMapper::of).orElse(null);
    }

    @PostMapping
    public ResponseEntity<Sensor> create(@RequestBody SensorCommand sensor) {
        SensorEntity entity = new SensorEntity(sensor.sensorType(), sensor.name());
        entity.setValue(sensor.value());
        SensorEntity saved = sensorDao.save(entity);
        return ResponseEntity.ok(SensorMapper.of(saved));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Sensor> update(@PathVariable Long id, @RequestBody SensorCommand sensor) {
        SensorEntity entity = sensorDao.findById(id).orElse(null);
        if (entity == null) {
            return ResponseEntity.badRequest().build();
        }
        entity.setValue(sensor.value());
        entity.setName(sensor.name());
        entity.setSensorType(sensor.sensorType());

        sensorDao.save(entity);

        return ResponseEntity.ok(SensorMapper.of(entity));
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        sensorDao.deleteById(id);
    }
}
