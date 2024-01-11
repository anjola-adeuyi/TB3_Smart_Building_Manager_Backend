package com.emse.spring.automacorp.model.dao;

import com.emse.spring.automacorp.model.entities.WindowEntity;

import java.util.List;

public interface WindowDaoCustom {
    List<WindowEntity> findRoomsWithOpenWindows(Long id);
    List<WindowEntity> findAllWindowsByRoomName(String roomName);
    void deleteByRoom(Long id);
    void closeAllWindowsByRoom(Long id);
    void openAllWindowsByRoom(Long id);
}
