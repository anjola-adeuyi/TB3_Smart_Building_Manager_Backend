package com.emse.spring.automacorp.model.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "SP_BUILDING")
public class BuildingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @OneToOne(optional = false)
    private SensorEntity outsideTemperature;

    @OneToMany(mappedBy = "building")
    private List<RoomEntity> rooms;

    public BuildingEntity() {
        rooms = List.of();
    }

    public BuildingEntity(String name, SensorEntity outsideTemperature, List<RoomEntity> rooms) {
        this();
        this.name = name;
        this.outsideTemperature = outsideTemperature;
        this.rooms = rooms;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SensorEntity getOutsideTemperature() {
        return outsideTemperature;
    }

    public void setOutsideTemperature(SensorEntity outsideTemperature) {
        this.outsideTemperature = outsideTemperature;
    }

    public List<RoomEntity> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomEntity> rooms) {
        this.rooms = rooms;
    }
}
