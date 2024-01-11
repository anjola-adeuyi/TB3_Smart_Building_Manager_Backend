package com.emse.spring.automacorp.model.controllers;

import com.emse.spring.automacorp.model.SensorType;
import com.emse.spring.automacorp.model.dao.*;
import com.emse.spring.automacorp.model.entities.BuildingEntity;
import com.emse.spring.automacorp.model.entities.SensorEntity;
import com.emse.spring.automacorp.model.mappers.BuildingMapper;
import com.emse.spring.automacorp.model.records.dao.Building;
import com.emse.spring.automacorp.model.records.dto.BuildingCommand;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@CrossOrigin
@RestController
@RequestMapping("/api/buildings")
@Transactional
public class BuildingController {
    private final BuildingDao buildingDao;
    private final RoomDao roomDao;
    private final SensorDao1 sensorDao1;
    private final WindowDao windowDao;
    private final HeaterDao heaterDao;

    public BuildingController(BuildingDao buildingDao, RoomDao roomDao, SensorDao1 sensorDao1, WindowDao windowDao, HeaterDao heaterDao) {
        this.buildingDao = buildingDao;
        this.roomDao = roomDao;
        this.sensorDao1 = sensorDao1;
        this.windowDao = windowDao;
        this.heaterDao = heaterDao;
    }

    @GetMapping
    public List<Building> findAll() {
        return buildingDao.findAll()
                .stream()
                .map(BuildingMapper::of)
                .sorted(Comparator.comparing(Building::name))
                .collect(toList());
    }

    @GetMapping(path = "/{id}")
    public Building findById(@PathVariable Long id) {
        return buildingDao.findById(id).map(BuildingMapper::of).orElse(null);
    }

    @PostMapping
    public ResponseEntity<Building> create(@RequestBody BuildingCommand command) {
        SensorEntity sensorEntity = new SensorEntity(SensorType.TEMPERATURE, "Outside temperature " + command.name());
        sensorEntity.setValue(command.outsideTemperature());
        sensorDao1.save(sensorEntity);

        BuildingEntity entity = new BuildingEntity(command.name(), sensorEntity, List.of());

        BuildingEntity saved = buildingDao.save(entity);
        return ResponseEntity.ok(BuildingMapper.of(saved));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Building> update(@PathVariable Long id, @RequestBody BuildingCommand command) {
        BuildingEntity entity = buildingDao.findById(id).orElse(null);
        if (entity == null) {
            return ResponseEntity.badRequest().build();
        }

        SensorEntity sensorEntity = sensorDao1.findById(entity.getOutsideTemperature().getId()).orElseThrow();
        sensorEntity.setValue(command.outsideTemperature());
        sensorDao1.save(sensorEntity);

        entity.setName(command.name());
        entity.setOutsideTemperature(sensorEntity);

        return ResponseEntity.ok(BuildingMapper.of(buildingDao.save(entity)));
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        BuildingEntity building = buildingDao.findById(id).orElseThrow();

        List<Long> currentTempIds = roomDao.findAllRoomsByBuildingName(building.getName())
                .stream()
                .map(roomEntity -> roomEntity.getCurrentTemp().getId())
                .toList();

        building.getRooms().forEach(room -> {
            windowDao.deleteByRoom(room.getId());
            heaterDao.deleteByRoom(room.getId());
            roomDao.deleteById(room.getId());
        });

        currentTempIds.forEach(sensorDao1::deleteById);

        buildingDao.deleteById(id);
        sensorDao1.deleteById(building.getOutsideTemperature().getId());
    }
}

