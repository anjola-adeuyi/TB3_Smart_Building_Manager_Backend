package com.emse.spring.automacorp.model.dao;

import com.emse.spring.automacorp.model.entities.RoomEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class RoomDaoCustomImpl implements RoomDaoCustom{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<RoomEntity> findAllRoomsByBuildingName(String buildingName) {
        String jpql = "select w from RoomEntity w where w.building.name = :buildingName order by w.name";
        return em.createQuery(jpql, RoomEntity.class)
                .setParameter("buildingName", buildingName)
                .getResultList();
    }
}
