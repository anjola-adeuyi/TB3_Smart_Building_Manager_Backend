package com.emse.spring.automacorp.model.entities;
import com.emse.spring.automacorp.model.SensorType;
import jakarta.persistence.*;

@Entity
@Table(name = "SP_SENSOR")
public class SensorEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable=false, length=255)
    private String name;

    @Column(name = "sensor_value")
    private Double value;

    @Column(name = "sensor_type")
    @Enumerated(EnumType.STRING)
    private SensorType sensorType;

    @OneToOne(mappedBy = "currentTemp")
    private RoomEntity room;

    @OneToOne(mappedBy = "outsideTemperature")
    private BuildingEntity building;

    @OneToOne(mappedBy = "status")
    private HeaterEntity heater;

    @OneToOne(mappedBy = "windowStatus")
    private WindowEntity window;

    public SensorEntity() {
    }

    public SensorEntity(SensorType sensorType, String name) {
        this.name = name;
        this.sensorType = sensorType;
    }

    public Long getId() { // (10).
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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public RoomEntity getRoom() {
        return room;
    }

    public void setRoom(RoomEntity room) {
        this.room = room;
    }

    public BuildingEntity getBuilding() {
        return building;
    }

    public void setBuilding(BuildingEntity building) {
        this.building = building;
    }

    public HeaterEntity getHeater() {
        return heater;
    }

    public void setHeater(HeaterEntity heater) {
        this.heater = heater;
    }

    public WindowEntity getWindow() {
        return window;
    }

    public void setWindow(WindowEntity window) {
        this.window = window;
    }
}