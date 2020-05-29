/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.IllegalOrphanException;
import DAO.exceptions.NonexistentEntityException;
import DAO.exceptions.PreexistingEntityException;
import DTO.Asignatura;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DTO.Proyecto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author PC MASTER RACE
 */
public class AsignaturaJpaController implements Serializable {

    public AsignaturaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Asignatura asignatura) throws PreexistingEntityException, Exception {
        if (asignatura.getProyectoList() == null) {
            asignatura.setProyectoList(new ArrayList<Proyecto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Proyecto> attachedProyectoList = new ArrayList<Proyecto>();
            for (Proyecto proyectoListProyectoToAttach : asignatura.getProyectoList()) {
                proyectoListProyectoToAttach = em.getReference(proyectoListProyectoToAttach.getClass(), proyectoListProyectoToAttach.getId());
                attachedProyectoList.add(proyectoListProyectoToAttach);
            }
            asignatura.setProyectoList(attachedProyectoList);
            em.persist(asignatura);
            for (Proyecto proyectoListProyecto : asignatura.getProyectoList()) {
                Asignatura oldAsignaturaOfProyectoListProyecto = proyectoListProyecto.getAsignatura();
                proyectoListProyecto.setAsignatura(asignatura);
                proyectoListProyecto = em.merge(proyectoListProyecto);
                if (oldAsignaturaOfProyectoListProyecto != null) {
                    oldAsignaturaOfProyectoListProyecto.getProyectoList().remove(proyectoListProyecto);
                    oldAsignaturaOfProyectoListProyecto = em.merge(oldAsignaturaOfProyectoListProyecto);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAsignatura(asignatura.getCodigo()) != null) {
                throw new PreexistingEntityException("Asignatura " + asignatura + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Asignatura asignatura) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Asignatura persistentAsignatura = em.find(Asignatura.class, asignatura.getCodigo());
            List<Proyecto> proyectoListOld = persistentAsignatura.getProyectoList();
            List<Proyecto> proyectoListNew = asignatura.getProyectoList();
            List<String> illegalOrphanMessages = null;
            for (Proyecto proyectoListOldProyecto : proyectoListOld) {
                if (!proyectoListNew.contains(proyectoListOldProyecto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Proyecto " + proyectoListOldProyecto + " since its asignatura field is not nullable.");
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
            asignatura.setProyectoList(proyectoListNew);
            asignatura = em.merge(asignatura);
            for (Proyecto proyectoListNewProyecto : proyectoListNew) {
                if (!proyectoListOld.contains(proyectoListNewProyecto)) {
                    Asignatura oldAsignaturaOfProyectoListNewProyecto = proyectoListNewProyecto.getAsignatura();
                    proyectoListNewProyecto.setAsignatura(asignatura);
                    proyectoListNewProyecto = em.merge(proyectoListNewProyecto);
                    if (oldAsignaturaOfProyectoListNewProyecto != null && !oldAsignaturaOfProyectoListNewProyecto.equals(asignatura)) {
                        oldAsignaturaOfProyectoListNewProyecto.getProyectoList().remove(proyectoListNewProyecto);
                        oldAsignaturaOfProyectoListNewProyecto = em.merge(oldAsignaturaOfProyectoListNewProyecto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = asignatura.getCodigo();
                if (findAsignatura(id) == null) {
                    throw new NonexistentEntityException("The asignatura with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Asignatura asignatura;
            try {
                asignatura = em.getReference(Asignatura.class, id);
                asignatura.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The asignatura with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Proyecto> proyectoListOrphanCheck = asignatura.getProyectoList();
            for (Proyecto proyectoListOrphanCheckProyecto : proyectoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Asignatura (" + asignatura + ") cannot be destroyed since the Proyecto " + proyectoListOrphanCheckProyecto + " in its proyectoList field has a non-nullable asignatura field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(asignatura);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Asignatura> findAsignaturaEntities() {
        return findAsignaturaEntities(true, -1, -1);
    }

    public List<Asignatura> findAsignaturaEntities(int maxResults, int firstResult) {
        return findAsignaturaEntities(false, maxResults, firstResult);
    }

    private List<Asignatura> findAsignaturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Asignatura.class));
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

    public Asignatura findAsignatura(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Asignatura.class, id);
        } finally {
            em.close();
        }
    }

    public int getAsignaturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Asignatura> rt = cq.from(Asignatura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
