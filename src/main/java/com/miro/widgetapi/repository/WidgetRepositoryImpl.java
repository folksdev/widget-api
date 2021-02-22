package com.miro.widgetapi.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class WidgetRepositoryImpl implements CustomWidgetRepository{

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Integer> findAllZIndex() {
        Query query = entityManager.createNativeQuery("SELECT w.z FROM Widget as w");
        return query.getResultList();
    }
}
