package com.fif.services.impl;

import com.fif.entity.Log;
import com.fif.services.LogRepository;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Database implementation of LogRepository using JPA/Hibernate
 */
@Repository
public class DatabaseLogRepository implements LogRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<Log> queryAll() {
        Query query = em.createQuery("SELECT l FROM Log l");
        @SuppressWarnings("unchecked")
        List<Log> result = query.getResultList();
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Log get(String id) {
        return em.find(Log.class, id);
    }

    @Override
    @Transactional(noRollbackFor = {})
    public Log save(Log log) {
        em.persist(log);
        em.flush();
        return log;
    }

    @Override
    @Transactional
    public void delete(Log log) {
        Log r = get(log.getId());
        if (r != null) {
            em.remove(r);
        }
    }
}