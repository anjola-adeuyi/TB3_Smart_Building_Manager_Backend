package com.emse.spring.automacorp.model.dao;

import com.emse.spring.automacorp.model.entities.HeaterEntity;
import com.emse.spring.automacorp.model.entities.SensorEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

public class HeaterDaoCustomImpl implements HeaterDaoCustom{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<HeaterEntity> findAllHeatersByRoomName(String roomName) {
        String jpql = "select w from HeaterEntity w where w.room.name = :roomName order by w.name";
        return em.createQuery(jpql, HeaterEntity.class)
                .setParameter("roomName", roomName)
                .getResultList();
    }

    @Override
    @Transactional
    public void deleteByRoom(Long id) {
        String sensorJpql = "select h.status from HeaterEntity h where h.room.id = :id";
        List<SensorEntity> sensors = em.createQuery(sensorJpql, SensorEntity.class)
                .setParameter("id", id)
                .getResultList();

        // Delete the SensorEntity objects
        for (SensorEntity sensor : sensors) {
            em.remove(em.contains(sensor) ? sensor : em.merge(sensor));
        }

        // Then, delete the HeaterEntity objects
        String heaterJpql = "delete from HeaterEntity h where h.room.id = :id";
        em.createQuery(heaterJpql)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void offAllHeatersByRoom(Long roomId) {
        String jpql = "select w.status from HeaterEntity w where w.room.id = :roomId";
        List<SensorEntity> heaterSensors = em.createQuery(jpql, SensorEntity.class)
                .setParameter("roomId", roomId)
                .getResultList();

        for(SensorEntity sensor : heaterSensors) {
            sensor.setValue(0.0);
        }
    }

    @Override
    @Transactional
    public void onAllHeatersByRoom(Long roomId) {
        String jpql = "select w.status from HeaterEntity w where w.room.id = :roomId";
        List<SensorEntity> heaterSensors = em.createQuery(jpql, SensorEntity.class)
                .setParameter("roomId", roomId)
                .getResultList();

        for(SensorEntity sensor : heaterSensors) {
            sensor.setValue(1.0);
        }
    }
}
