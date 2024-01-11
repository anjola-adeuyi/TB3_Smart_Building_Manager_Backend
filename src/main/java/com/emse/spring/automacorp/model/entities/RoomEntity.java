package com.emse.spring.automacorp.model.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "SP_ROOM")
public class RoomEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToOne(optional = false)
    private SensorEntity currentTemp;

    @Column(nullable = false)
    private int floor;

    @Column
    private Double targetTemp;

    @OneToMany(mappedBy = "room")
    private List<HeaterEntity> heaters;

    @OneToMany(mappedBy = "room")
    private List<WindowEntity> windows;

    @ManyToOne(optional = false)
    private BuildingEntity building;

    public RoomEntity() {
        heaters = List.of();
        windows = List.of();
    }

    public RoomEntity(String name, SensorEntity currentTemp, int floor) {
        this();
        this.name = name;
        this.currentTemp = currentTemp;
        this.floor = floor;
    }

    public Long getId() {
        return id;
    }

    public int getFloor() {
        return floor;
    }

    public String getName() {
        return name;
    }

    public SensorEntity getCurrentTemp() {
        return currentTemp;
    }

    public Double getTargetTemp() {
        return targetTemp;
    }

    public List<WindowEntity> getWindows() {
        return windows;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrentTemp(SensorEntity currentTemp) {
        this.currentTemp = currentTemp;
    }

    public void setTargetTemp(double targetTemp) {
        this.targetTemp = targetTemp;
    }

    public void setWindows(List<WindowEntity> windows) {
        this.windows = windows;
    }

    public List<HeaterEntity> getHeaters() {
        return heaters;
    }

    public void setHeaters(List<HeaterEntity> heaters) {
        this.heaters = heaters;
    }

    public BuildingEntity getBuilding() {
        return building;
    }

    public void setBuilding(BuildingEntity building) {
        this.building = building;
    }
}
