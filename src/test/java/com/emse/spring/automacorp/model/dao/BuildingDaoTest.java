package com.emse.spring.automacorp.model.dao;

import com.emse.spring.automacorp.model.entities.BuildingEntity;
import com.emse.spring.automacorp.model.entities.RoomEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

@DataJpaTest
class BuildingDaoTest {
    @Autowired
    private BuildingDao buildingDao;

    @Autowired
    private RoomDao roomDao;

    @Test
    public void shouldFindABuildingById() {
        BuildingEntity building = buildingDao.getReferenceById(-10L);
        Assertions.assertThat(building.getName()).isEqualTo("Mines");
        Assertions.assertThat(building.getOutsideTemperature().getId()).isEqualTo(-7);
        List<RoomEntity> rooms = Arrays.asList(roomDao.getReferenceById(-10L), roomDao.getReferenceById(-9L));
        Assertions.assertThat(building.getRooms().containsAll(rooms)).isTrue();
    }
}