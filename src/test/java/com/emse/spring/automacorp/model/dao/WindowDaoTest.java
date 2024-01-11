package com.emse.spring.automacorp.model.dao;

import com.emse.spring.automacorp.model.entities.RoomEntity;
import com.emse.spring.automacorp.model.entities.WindowEntity;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.stream.Collectors;

@DataJpaTest
class WindowDaoTest {
    @Autowired
    private WindowDao windowDao;

    @Autowired
    private RoomDao roomDao;

    @Test
    public void shouldFindAWindowById() {
        WindowEntity window = windowDao.getReferenceById(-10L);
        Assertions.assertThat(window.getName()).isEqualTo("Window 1 Room 1 Mines");
        Assertions.assertThat(window.getWindowStatus().getValue()).isEqualTo(1.0);
    }

    @Test
    public void shouldFindRoomsWithOpenWindows() {
        List<WindowEntity> result = windowDao.findRoomsWithOpenWindows(-10L);
        Assertions.assertThat(result)
                .hasSize(2)
                .extracting("id", "name")
                .containsExactly(
                        Tuple.tuple(-10L, "Window 1 Room 1 Mines"),
                        Tuple.tuple(-12L, "Window 3 Room 1 Mines")
                );
    }

    @Test
    public void shouldNotFindRoomsWithOpenWindows() {
        List<WindowEntity> result = windowDao.findRoomsWithOpenWindows(-9L);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void shouldFindWindowsByRoomName() {
        List<WindowEntity> result = windowDao.findAllWindowsByRoomName("Room 1 Mines");
        Assertions.assertThat(result)
                .hasSize(3)
                .extracting("id", "name")
                .containsExactly(
                        Tuple.tuple(-10L, "Window 1 Room 1 Mines"),
                        Tuple.tuple(-11L, "Window 2 Room 1 Mines"),
                        Tuple.tuple(-12L, "Window 3 Room 1 Mines")
                );
    }

    @Test
    public void shouldDeleteWindowsRoom() {
        RoomEntity room = roomDao.getReferenceById(-10L);
        List<Long> roomIds = room.getWindows().stream().map(WindowEntity::getId).collect(Collectors.toList());
        Assertions.assertThat(roomIds).hasSize(3);

        windowDao.deleteByRoom(-10L);
        List<WindowEntity> result = windowDao.findAllById(roomIds);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void shouldCloseAllWindowsByRoom() {
        RoomEntity room = roomDao.getReferenceById(-10L);
        windowDao.closeAllWindowsByRoom(room.getId());
        List<WindowEntity> windows = windowDao.findAllWindowsByRoomName(room.getName());
        Assertions.assertThat(windows).allMatch(window -> window.getWindowStatus().getValue() == 0.0);
    }

    @Test
    public void shouldOpenAllWindowsByRoom() {
        RoomEntity room = roomDao.getReferenceById(-10L);
        windowDao.openAllWindowsByRoom(room.getId());
        List<WindowEntity> windows = windowDao.findAllWindowsByRoomName(room.getName());
        Assertions.assertThat(windows).allMatch(window -> window.getWindowStatus().getValue() == 1.0);
    }
}