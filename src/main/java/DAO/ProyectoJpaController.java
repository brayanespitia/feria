/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DTO.Asignatura;
import DTO.Categoria;
import DTO.Evento;
import DTO.Tipo;
import DTO.Alumno;
import DTO.Proyecto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author PC MASTER RACE
 */
public class ProyectoJpaController implements Serializable {

    public ProyectoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Proyecto proyecto) {
        if (proyecto.getAlumnoList() == null) {
            proyecto.setAlumnoList(new ArrayList<Alumno>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Asignatura asignatura = proyecto.getAsignatura();
            if (asignatura != null) {
                asignatura = em.getReference(asignatura.getClass(), asignatura.getCodigo());
                proyecto.setAsignatura(asignatura);
            }
            Categoria categoria = proyecto.getCategoria();
            if (categoria != null) {
                categoria = em.getReference(categoria.getClass(), categoria.getId());
                proyecto.setCategoria(categoria);
            }
            Evento evento = proyecto.getEvento();
            if (evento != null) {
                evento = em.getReference(evento.getClass(), evento.getId());
                proyecto.setEvento(evento);
            }
            Tipo tipo = proyecto.getTipo();
            if (tipo != null) {
                tipo = em.getReference(tipo.getClass(), tipo.getId());
                proyecto.setTipo(tipo);
            }
            List<Alumno> attachedAlumnoList = new ArrayList<Alumno>();
            for (Alumno alumnoListAlumnoToAttach : proyecto.getAlumnoList()) {
                alumnoListAlumnoToAttach = em.getReference(alumnoListAlumnoToAttach.getClass(), alumnoListAlumnoToAttach.getCodigo());
                attachedAlumnoList.add(alumnoListAlumnoToAttach);
            }
            proyecto.setAlumnoList(attachedAlumnoList);
            em.persist(proyecto);
            if (asignatura != null) {
                asignatura.getProyectoList().add(proyecto);
                asignatura = em.merge(asignatura);
            }
            if (categoria != null) {
                categoria.getProyectoList().add(proyecto);
                categoria = em.merge(categoria);
            }
            if (evento != null) {
                evento.getProyectoList().add(proyecto);
                evento = em.merge(evento);
            }
            if (tipo != null) {
                tipo.getProyectoList().add(proyecto);
                tipo = em.merge(tipo);
            }
            for (Alumno alumnoListAlumno : proyecto.getAlumnoList()) {
                alumnoListAlumno.getProyectoList().add(proyecto);
                alumnoListAlumno = em.merge(alumnoListAlumno);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Proyecto proyecto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proyecto persistentProyecto = em.find(Proyecto.class, proyecto.getId());
            Asignatura asignaturaOld = persistentProyecto.getAsignatura();
            Asignatura asignaturaNew = proyecto.getAsignatura();
            Categoria categoriaOld = persistentProyecto.getCategoria();
            Categoria categoriaNew = proyecto.getCategoria();
            Evento eventoOld = persistentProyecto.getEvento();
            Evento eventoNew = proyecto.getEvento();
            Tipo tipoOld = persistentProyecto.getTipo();
            Tipo tipoNew = proyecto.getTipo();
            List<Alumno> alumnoListOld = persistentProyecto.getAlumnoList();
            List<Alumno> alumnoListNew = proyecto.getAlumnoList();
            if (asignaturaNew != null) {
                asignaturaNew = em.getReference(asignaturaNew.getClass(), asignaturaNew.getCodigo());
                proyecto.setAsignatura(asignaturaNew);
            }
            if (categoriaNew != null) {
                categoriaNew = em.getReference(categoriaNew.getClass(), categoriaNew.getId());
                proyecto.setCategoria(categoriaNew);
            }
            if (eventoNew != null) {
                eventoNew = em.getReference(eventoNew.getClass(), eventoNew.getId());
                proyecto.setEvento(eventoNew);
            }
            if (tipoNew != null) {
                tipoNew = em.getReference(tipoNew.getClass(), tipoNew.getId());
                proyecto.setTipo(tipoNew);
            }
            List<Alumno> attachedAlumnoListNew = new ArrayList<Alumno>();
            for (Alumno alumnoListNewAlumnoToAttach : alumnoListNew) {
                alumnoListNewAlumnoToAttach = em.getReference(alumnoListNewAlumnoToAttach.getClass(), alumnoListNewAlumnoToAttach.getCodigo());
                attachedAlumnoListNew.add(alumnoListNewAlumnoToAttach);
            }
            alumnoListNew = attachedAlumnoListNew;
            proyecto.setAlumnoList(alumnoListNew);
            proyecto = em.merge(proyecto);
            if (asignaturaOld != null && !asignaturaOld.equals(asignaturaNew)) {
                asignaturaOld.getProyectoList().remove(proyecto);
                asignaturaOld = em.merge(asignaturaOld);
            }
            if (asignaturaNew != null && !asignaturaNew.equals(asignaturaOld)) {
                asignaturaNew.getProyectoList().add(proyecto);
                asignaturaNew = em.merge(asignaturaNew);
            }
            if (categoriaOld != null && !categoriaOld.equals(categoriaNew)) {
                categoriaOld.getProyectoList().remove(proyecto);
                categoriaOld = em.merge(categoriaOld);
            }
            if (categoriaNew != null && !categoriaNew.equals(categoriaOld)) {
                categoriaNew.getProyectoList().add(proyecto);
                categoriaNew = em.merge(categoriaNew);
            }
            if (eventoOld != null && !eventoOld.equals(eventoNew)) {
                eventoOld.getProyectoList().remove(proyecto);
                eventoOld = em.merge(eventoOld);
            }
            if (eventoNew != null && !eventoNew.equals(eventoOld)) {
                eventoNew.getProyectoList().add(proyecto);
                eventoNew = em.merge(eventoNew);
            }
            if (tipoOld != null && !tipoOld.equals(tipoNew)) {
                tipoOld.getProyectoList().remove(proyecto);
                tipoOld = em.merge(tipoOld);
            }
            if (tipoNew != null && !tipoNew.equals(tipoOld)) {
                tipoNew.getProyectoList().add(proyecto);
                tipoNew = em.merge(tipoNew);
            }
            for (Alumno alumnoListOldAlumno : alumnoListOld) {
                if (!alumnoListNew.contains(alumnoListOldAlumno)) {
                    alumnoListOldAlumno.getProyectoList().remove(proyecto);
                    alumnoListOldAlumno = em.merge(alumnoListOldAlumno);
                }
            }
            for (Alumno alumnoListNewAlumno : alumnoListNew) {
                if (!alumnoListOld.contains(alumnoListNewAlumno)) {
                    alumnoListNewAlumno.getProyectoList().add(proyecto);
                    alumnoListNewAlumno = em.merge(alumnoListNewAlumno);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = proyecto.getId();
                if (findProyecto(id) == null) {
                    throw new NonexistentEntityException("The proyecto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proyecto proyecto;
            try {
                proyecto = em.getReference(Proyecto.class, id);
                proyecto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proyecto with id " + id + " no longer exists.", enfe);
            }
            Asignatura asignatura = proyecto.getAsignatura();
            if (asignatura != null) {
                asignatura.getProyectoList().remove(proyecto);
                asignatura = em.merge(asignatura);
            }
            Categoria categoria = proyecto.getCategoria();
            if (categoria != null) {
                categoria.getProyectoList().remove(proyecto);
                categoria = em.merge(categoria);
            }
            Evento evento = proyecto.getEvento();
            if (evento != null) {
                evento.getProyectoList().remove(proyecto);
                evento = em.merge(evento);
            }
            Tipo tipo = proyecto.getTipo();
            if (tipo != null) {
                tipo.getProyectoList().remove(proyecto);
                tipo = em.merge(tipo);
            }
            List<Alumno> alumnoList = proyecto.getAlumnoList();
            for (Alumno alumnoListAlumno : alumnoList) {
                alumnoListAlumno.getProyectoList().remove(proyecto);
                alumnoListAlumno = em.merge(alumnoListAlumno);
            }
            em.remove(proyecto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Proyecto> findProyectoEntities() {
        return findProyectoEntities(true, -1, -1);
    }

    public List<Proyecto> findProyectoEntities(int maxResults, int firstResult) {
        return findProyectoEntities(false, maxResults, firstResult);
    }

    private List<Proyecto> findProyectoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Proyecto.class));
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

    public Proyecto findProyecto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Proyecto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProyectoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Proyecto> rt = cq.from(Proyecto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
