package com.emse.spring.automacorp.model.dao;

import com.emse.spring.automacorp.model.entities.RoomEntity;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class RoomDaoTest {
    @Autowired
    private RoomDao roomDao;

    @Test
    public void shouldFindARoomById() {
        RoomEntity room = roomDao.getReferenceById(-10L);
        Assertions.assertThat(room.getName()).isEqualTo("Room 1 Mines");
        Assertions.assertThat(room.getFloor()).isEqualTo(1);
        Assertions.assertThat(room.getCurrentTemp().getId()).isEqualTo(-10);
    }

    @Test
    public void shouldFindRoomsByBuildingName() {
        List<RoomEntity> result = roomDao.findAllRoomsByBuildingName("Mines");
        Assertions.assertThat(result)
                .hasSize(3)
                .extracting("id", "name")
                .containsExactly(
                        Tuple.tuple(-10L, "Room 1 Mines"),
                        Tuple.tuple(-9L, "Room 2 Mines"),
                        Tuple.tuple(-8L, "Room 3 Mines")
                );
    }
}