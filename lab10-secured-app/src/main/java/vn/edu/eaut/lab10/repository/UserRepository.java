package vn.edu.eaut.lab10.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import vn.edu.eaut.lab10.config.JPAUtil;
import vn.edu.eaut.lab10.model.Role;
import vn.edu.eaut.lab10.model.User;

import java.util.List;
import java.util.Optional;

public class UserRepository {

    public Optional<User> findByUsername(String username) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            User user = em.createQuery(
                    "SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    public Optional<User> findByEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            User user = em.createQuery(
                    "SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    public Optional<User> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            User user = em.createQuery(
                    "SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.id = :id", User.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    public List<User> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles ORDER BY u.id ASC", User.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<User> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String kw = "%" + keyword.trim().toLowerCase() + "%";
            return em.createQuery(
                    "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles " +
                    "WHERE LOWER(u.username) LIKE :kw OR LOWER(u.email) LIKE :kw OR LOWER(u.fullName) LIKE :kw " +
                    "ORDER BY u.id ASC", User.class)
                    .setParameter("kw", kw)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public User save(User user) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (user.getId() == null) {
                em.persist(user);
            } else {
                user = em.merge(user);
            }
            tx.commit();
            return user;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<Role> findRoleByName(String name) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Role role = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(role);
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    public Role saveRole(Role role) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (role.getId() == null) {
                em.persist(role);
            } else {
                role = em.merge(role);
            }
            tx.commit();
            return role;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public long count() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }
}
