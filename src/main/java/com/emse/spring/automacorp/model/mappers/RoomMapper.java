package com.emse.spring.automacorp.model.mappers;

import com.emse.spring.automacorp.model.entities.RoomEntity;
import com.emse.spring.automacorp.model.records.dao.Room;

import java.util.stream.Collectors;

public class RoomMapper {
    public static Room of(RoomEntity room){
        return new Room(
                room.getId(),
                room.getName(),
                room.getCurrentTemp().getValue(),
                room.getTargetTemp(),
                room.getFloor(),
                room.getBuilding().getId(),
                room.getWindows().stream().map(WindowMapper::of).collect(Collectors.toList()),
                room.getHeaters().stream().map(HeaterMapper::of).collect(Collectors.toList())
        );
    }
}
