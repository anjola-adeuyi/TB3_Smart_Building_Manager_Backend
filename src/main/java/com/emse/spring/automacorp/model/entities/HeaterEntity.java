package com.emse.spring.automacorp.model.entities;
import jakarta.persistence.*;

@Entity
@Table(name = "SP_HEATER")
public class HeaterEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @ManyToOne(optional = false)
    private RoomEntity room;

    @OneToOne(optional = false)
    private SensorEntity status;

    public HeaterEntity() {
    }

    public HeaterEntity(String name, RoomEntity room, SensorEntity status) {
        this.name = name;
        this.room = room;
        this.status = status;
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

    public RoomEntity getRoom() {
        return room;
    }

    public void setRoom(RoomEntity room) {
        this.room = room;
    }

    public SensorEntity getStatus() {
        return status;
    }

    public void setStatus(SensorEntity status) {
        this.status = status;
    }
}