/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.IllegalOrphanException;
import DAO.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DTO.Proyecto;
import DTO.Tipo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author PC MASTER RACE
 */
public class TipoJpaController implements Serializable {

    public TipoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipo tipo) {
        if (tipo.getProyectoList() == null) {
            tipo.setProyectoList(new ArrayList<Proyecto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Proyecto> attachedProyectoList = new ArrayList<Proyecto>();
            for (Proyecto proyectoListProyectoToAttach : tipo.getProyectoList()) {
                proyectoListProyectoToAttach = em.getReference(proyectoListProyectoToAttach.getClass(), proyectoListProyectoToAttach.getId());
                attachedProyectoList.add(proyectoListProyectoToAttach);
            }
            tipo.setProyectoList(attachedProyectoList);
            em.persist(tipo);
            for (Proyecto proyectoListProyecto : tipo.getProyectoList()) {
                Tipo oldTipoOfProyectoListProyecto = proyectoListProyecto.getTipo();
                proyectoListProyecto.setTipo(tipo);
                proyectoListProyecto = em.merge(proyectoListProyecto);
                if (oldTipoOfProyectoListProyecto != null) {
                    oldTipoOfProyectoListProyecto.getProyectoList().remove(proyectoListProyecto);
                    oldTipoOfProyectoListProyecto = em.merge(oldTipoOfProyectoListProyecto);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipo tipo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipo persistentTipo = em.find(Tipo.class, tipo.getId());
            List<Proyecto> proyectoListOld = persistentTipo.getProyectoList();
            List<Proyecto> proyectoListNew = tipo.getProyectoList();
            List<String> illegalOrphanMessages = null;
            for (Proyecto proyectoListOldProyecto : proyectoListOld) {
                if (!proyectoListNew.contains(proyectoListOldProyecto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Proyecto " + proyectoListOldProyecto + " since its tipo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Proyecto> attachedProyectoListNew = new ArrayList<Proyecto>();
            for (Proyecto proyectoListNewProyectoToAttach : proyectoListNew) {
                proyectoListNewProyectoToAttach = em.getReference(proyectoListNewProyectoToAttach.getClass(), proyectoListNewProyectoToAttach.getId());
                attachedProyectoListNew.add(proyectoListNewProyectoToAttach);
            }
            proyectoListNew = attachedProyectoListNew;
            tipo.setProyectoList(proyectoListNew);
            tipo = em.merge(tipo);
            for (Proyecto proyectoListNewProyecto : proyectoListNew) {
                if (!proyectoListOld.contains(proyectoListNewProyecto)) {
                    Tipo oldTipoOfProyectoListNewProyecto = proyectoListNewProyecto.getTipo();
                    proyectoListNewProyecto.setTipo(tipo);
                    proyectoListNewProyecto = em.merge(proyectoListNewProyecto);
                    if (oldTipoOfProyectoListNewProyecto != null && !oldTipoOfProyectoListNewProyecto.equals(tipo)) {
                        oldTipoOfProyectoListNewProyecto.getProyectoList().remove(proyectoListNewProyecto);
                        oldTipoOfProyectoListNewProyecto = em.merge(oldTipoOfProyectoListNewProyecto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipo.getId();
                if (findTipo(id) == null) {
                    throw new NonexistentEntityException("The tipo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipo tipo;
            try {
                tipo = em.getReference(Tipo.class, id);
                tipo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Proyecto> proyectoListOrphanCheck = tipo.getProyectoList();
            for (Proyecto proyectoListOrphanCheckProyecto : proyectoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tipo (" + tipo + ") cannot be destroyed since the Proyecto " + proyectoListOrphanCheckProyecto + " in its proyectoList field has a non-nullable tipo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tipo> findTipoEntities() {
        return findTipoEntities(true, -1, -1);
    }

    public List<Tipo> findTipoEntities(int maxResults, int firstResult) {
        return findTipoEntities(false, maxResults, firstResult);
    }

    private List<Tipo> findTipoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipo.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Tipo findTipo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipo.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipo> rt = cq.from(Tipo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
