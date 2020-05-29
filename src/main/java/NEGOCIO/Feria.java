/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NEGOCIO;

import DAO.AlumnoJpaController;
import DAO.AsignaturaJpaController;
import DAO.CategoriaJpaController;
import DAO.Conexion;
import DAO.EventoJpaController;
import DAO.ProyectoJpaController;
import DAO.TipoJpaController;
import DTO.Alumno;
import DTO.Asignatura;
import DTO.Categoria;
import DTO.Evento;
import DTO.Proyecto;
import DTO.Tipo;
import java.util.Date;
import java.util.List;

/**
 *
 * @author PC MASTER RACE
 */
public class Feria {

    Conexion con = Conexion.getConexion();
    AlumnoJpaController alumnoDAO = new AlumnoJpaController(con.getBd());
    List<Alumno> alumnos = alumnoDAO.findAlumnoEntities();

    ProyectoJpaController proyectoDAO = new ProyectoJpaController(con.getBd());
    List<Proyecto> proyectos = proyectoDAO.findProyectoEntities();

    EventoJpaController eventoDAO = new EventoJpaController(con.getBd());
    List<Evento> eventos = eventoDAO.findEventoEntities();

    TipoJpaController tipoDAO = new TipoJpaController(con.getBd());
    List<Tipo> tipos = tipoDAO.findTipoEntities();

    CategoriaJpaController categoriaDAO = new CategoriaJpaController(con.getBd());
    List<Categoria> categorias = categoriaDAO.findCategoriaEntities();

    AsignaturaJpaController asignaturaDAO = new AsignaturaJpaController(con.getBd());
    List<Asignatura> asignaturas = asignaturaDAO.findAsignaturaEntities();

    public boolean registrarAlumno(String nombre, String email, String clave) {

        boolean exito = false;
        Alumno al = new Alumno();
        if (al == null) {
            try {
                Alumno a = new Alumno();
                a.setNombre(nombre);
                a.setEmail(email);
                a.setClave(clave);
                alumnoDAO.create(a);
                exito = true;

            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.err.println(e.getMessage());
            }

        }
        return exito;
    }

    public boolean registrarEvento(String nombre, Date fecha) {

        boolean exito = false;
        Evento ev = new Evento();
        if (ev == null) {
            try {
                Evento eve = new Evento();
                eve.setNombre(nombre);
                eve.setFecha(fecha);
                eventoDAO.create(eve);
                exito = true;

            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.err.println(e.getMessage());
            }

        }
        return exito;
    }

    public Alumno buscarAlumnoEmail(String email) {
        for (Alumno a : alumnos) {
            if (a.getEmail().equalsIgnoreCase(email)) {
                return a;
            }
        }
        return null;
    }

    public boolean login(String email, String clave) {

        boolean exito = false;
        Alumno al = new Alumno();
        al = buscarAlumnoEmail(email);
        return exito;
    }
}
