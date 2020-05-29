/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NEGOCIO;

import DAO.AlumnoJpaController;
import DAO.Conexion;
import DTO.Alumno;
import java.util.List;

/**
 *
 * @author PC MASTER RACE
 */
public class Feria {
     Conexion con = Conexion.getConexion();
     AlumnoJpaController alumnoDAO = new AlumnoJpaController(con.getBd());
     List<Alumno> alumnos = alumnoDAO.findAlumnoEntities();
}
