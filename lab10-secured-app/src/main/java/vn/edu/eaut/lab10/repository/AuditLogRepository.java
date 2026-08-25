package vn.edu.eaut.lab10.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.eaut.lab10.config.JPAUtil;
import vn.edu.eaut.lab10.model.AuditLog;

import java.util.List;

public class AuditLogRepository {

    public AuditLog save(AuditLog log) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(log);
            tx.commit();
            return log;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return log;
        } finally {
            em.close();
        }
    }

    public List<AuditLog> findAllRecent() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM AuditLog a ORDER BY a.createdAt DESC", AuditLog.class)
                    .setMaxResults(50)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<AuditLog> findByUsername(String username) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM AuditLog a WHERE a.username = :username ORDER BY a.createdAt DESC", AuditLog.class)
                    .setParameter("username", username)
                    .setMaxResults(30)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
