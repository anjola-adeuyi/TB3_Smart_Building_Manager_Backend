package com.emse.spring.automacorp.model.entities;

import jakarta.persistence.*;

import java.util.List;

/**
 * Represents a room within a building in the smart building management system.
 */
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

    /**
     * Default constructor for creating a room entity.
     */
    public RoomEntity() {
        heaters = List.of();
        windows = List.of();
    }

    /**
     * Creates a room entity with specified details.
     *
     * @param name The name of the room.
     * @param currentTemp The current temperature sensor associated with the room.
     * @param floor The floor number the room is on.
     */
    public RoomEntity(String name, SensorEntity currentTemp, int floor) {
        this();
        this.name = name;
        this.currentTemp = currentTemp;
        this.floor = floor;
    }

    // Getter and setter methods with Javadoc comments

    /**
     * Gets the unique identifier of the room.
     *
     * @return The ID of the room.
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the floor number of the room.
     *
     * @return The floor number.
     */
    public int getFloor() {
        return floor;
    }

    /**
     * Gets the name of the room.
     *
     * @return The name of the room.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the current temperature sensor of the room.
     *
     * @return The current temperature sensor entity.
     */
    public SensorEntity getCurrentTemp() {
        return currentTemp;
    }

    /**
     * Gets the target temperature of the room.
     *
     * @return The target temperature.
     */
    public Double getTargetTemp() {
        return targetTemp;
    }

    /**
     * Gets the list of windows associated with the room.
     *
     * @return A list of window entities.
     */
    public List<WindowEntity> getWindows() {
        return windows;
    }

    /**
     * Sets the unique identifier of the room.
     *
     * @param id The ID to set for the room.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets the floor number of the room.
     *
     * @param floor The floor number to set for the room.
     */
    public void setFloor(int floor) {
        this.floor = floor;
    }

    /**
     * Sets the name of the room.
     *
     * @param name The new name for the room.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the current temperature sensor for the room.
     *
     * @param currentTemp The temperature sensor to set.
     */
    public void setCurrentTemp(SensorEntity currentTemp) {
        this.currentTemp = currentTemp;
    }

    /**
     * Sets the target temperature for the room.
     *
     * @param targetTemp The target temperature to set.
     */
    public void setTargetTemp(double targetTemp) {
        this.targetTemp = targetTemp;
    }

    /**
     * Sets the list of windows for the room.
     *
     * @param windows The list of windows to set.
     */
    public void setWindows(List<WindowEntity> windows) {
        this.windows = windows;
    }

    /**
     * Gets the list of heaters associated with the room.
     *
     * @return A list of heater entities.
     */
    public List<HeaterEntity> getHeaters() {
        return heaters;
    }

    /**
     * Sets the list of heaters for the room.
     *
     * @param heaters The list of heaters to set.
     */
    public void setHeaters(List<HeaterEntity> heaters) {
        this.heaters = heaters;
    }

    /**
     * Gets the building entity to which the room belongs.
     *
     * @return The building entity.
     */
    public BuildingEntity getBuilding() {
        return building;
    }

    /**
     * Sets the building entity for the room.
     *
     * @param building The building entity to set.
     */
    public void setBuilding(BuildingEntity building) {
        this.building = building;
    }
}
