package com.emse.spring.automacorp.model.controllers;

import com.emse.spring.automacorp.model.HeaterStatus;
import com.emse.spring.automacorp.model.SensorType;
import com.emse.spring.automacorp.model.dao.HeaterDao;
import com.emse.spring.automacorp.model.dao.RoomDao;
import com.emse.spring.automacorp.model.dao.SensorDao1;
import com.emse.spring.automacorp.model.entities.HeaterEntity;
import com.emse.spring.automacorp.model.entities.SensorEntity;
import com.emse.spring.automacorp.model.mappers.HeaterMapper;
import com.emse.spring.automacorp.model.records.dao.Heater;
import com.emse.spring.automacorp.model.records.dto.HeaterCommand;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/heaters")
@Transactional
public class HeaterController {
    private final HeaterDao heaterDao;
    private final RoomDao roomDao;
    private final SensorDao1 sensorDao1;

    public HeaterController(HeaterDao heaterDao, RoomDao roomDao, SensorDao1 sensorDao1) {
        this.heaterDao = heaterDao;
        this.roomDao = roomDao;
        this.sensorDao1 = sensorDao1;
    }

    @GetMapping
    public List<Heater> findAll() {
        return heaterDao.findAll()
                .stream()
                .map(HeaterMapper::of)
                .sorted(Comparator.comparing(Heater::name))
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/custom/{roomName}")
    public List<Heater> findHeatersByRoomName(@PathVariable String roomName) {
        return heaterDao.findAllHeatersByRoomName(roomName)
                .stream()
                .map(HeaterMapper::of)
                .sorted(Comparator.comparing(Heater::name))
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public Heater findById(@PathVariable Long id) {
        return heaterDao.findById(id).map(HeaterMapper::of).orElse(null);
    }

    @PostMapping
    public ResponseEntity<Heater> create(@RequestBody HeaterCommand heaterCommand) {
        SensorEntity sensorEntity = new SensorEntity(SensorType.STATUS, "Status " + heaterCommand.name());
        sensorEntity.setValue(heaterCommand.heaterStatus() == HeaterStatus.ON ? 1.0 : 0.0);
        sensorDao1.save(sensorEntity);

        HeaterEntity entity = new HeaterEntity(heaterCommand.name(), roomDao.findById(heaterCommand.roomId()).orElse(null), sensorEntity);
        HeaterEntity saved = heaterDao.save(entity);

        return ResponseEntity.ok(HeaterMapper.of(saved));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Heater> update(@PathVariable Long id, @RequestBody HeaterCommand heaterCommand) {
        HeaterEntity entity = heaterDao.findById(id).orElse(null);
        if (entity == null) {
            return ResponseEntity.badRequest().build();
        }

        SensorEntity sensorEntity = sensorDao1.findById(entity.getStatus().getId()).orElseThrow();
        sensorEntity.setValue(heaterCommand.heaterStatus() == HeaterStatus.ON ? 1.0 : 0.0);
        sensorDao1.save(sensorEntity);

        entity.setName(heaterCommand.name());
        entity.setRoom(roomDao.findById(heaterCommand.roomId()).orElse(null));
        entity.setStatus(sensorEntity);

        return ResponseEntity.ok(HeaterMapper.of(heaterDao.save(entity)));
    }

    @PutMapping(path = "/{id}/switch")
    public ResponseEntity<Heater> switchHeaterStatusById(@PathVariable Long id) {
        HeaterEntity entity = heaterDao.findById(id).orElse(null);
        if(entity != null && entity.getStatus() != null){
            entity.getStatus().setValue(entity.getStatus().getValue() == 1.0 ? 0.0 : 1.0);
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Heater not found");
        }

        heaterDao.save(entity);
        return ResponseEntity.ok(HeaterMapper.of(entity));
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        HeaterEntity heater = heaterDao.findById(id).orElseThrow();
        heaterDao.deleteById(id);
        sensorDao1.deleteById(heater.getStatus().getId());
    }
}
