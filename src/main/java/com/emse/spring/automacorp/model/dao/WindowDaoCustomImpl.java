package com.emse.spring.automacorp.model.dao;

import com.emse.spring.automacorp.model.entities.SensorEntity;
import com.emse.spring.automacorp.model.entities.WindowEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

public class WindowDaoCustomImpl implements WindowDaoCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<WindowEntity> findRoomsWithOpenWindows(Long id) {
        String jpql = "select w from WindowEntity w inner join w.windowStatus s " +
                "where w.room.id = :id and s.value > 0.0 order by w.name";
        return em.createQuery(jpql, WindowEntity.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<WindowEntity> findAllWindowsByRoomName(String roomName) {
        String jpql = "select w from WindowEntity w where w.room.name = :roomName order by w.name";
        return em.createQuery(jpql, WindowEntity.class)
                .setParameter("roomName", roomName)
                .getResultList();
    }

    @Override
    @Transactional
    public void deleteByRoom(Long id) {
        String sensorJpql = "select w.windowStatus from WindowEntity w where w.room.id = :id";
        List<SensorEntity> sensors = em.createQuery(sensorJpql, SensorEntity.class)
                .setParameter("id", id)
                .getResultList();

        // Delete the SensorEntity objects
        for (SensorEntity sensor : sensors) {
            em.remove(em.contains(sensor) ? sensor : em.merge(sensor));
        }

        // Then, delete the WindowEntity objects
        String windowJpql = "delete from WindowEntity w where w.room.id = :id";
        em.createQuery(windowJpql)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void closeAllWindowsByRoom(Long roomId) {
        String jpql = "select w.windowStatus from WindowEntity w where w.room.id = :roomId";
        List<SensorEntity> windowSensors = em.createQuery(jpql, SensorEntity.class)
                .setParameter("roomId", roomId)
                .getResultList();

        for(SensorEntity sensor : windowSensors) {
            sensor.setValue(0.0);
        }
    }

    @Override
    @Transactional
    public void openAllWindowsByRoom(Long roomId) {
        String jpql = "select w.windowStatus from WindowEntity w where w.room.id = :roomId";
        List<SensorEntity> windowSensors = em.createQuery(jpql, SensorEntity.class)
                .setParameter("roomId", roomId)
                .getResultList();

        for(SensorEntity sensor : windowSensors) {
            sensor.setValue(1.0);
        }
    }
}