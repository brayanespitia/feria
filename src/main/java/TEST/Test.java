/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST;

import DAO.AlumnoJpaController;
import DAO.Conexion;
import DAO.exceptions.NonexistentEntityException;
import DTO.*;

/**
 *
 * @author PC MASTER RACE
 */
public class Test {

    public static void main(String[] args) throws NonexistentEntityException, Exception {

        Conexion con = Conexion.getConexion();
        
        
        System.out.println("creacion de alumno");
        AlumnoJpaController aDao = new AlumnoJpaController();
        Alumno a = new Alumno("Brayan", "brayan@gmail.com", "12345");
        aDao.create(a);
    }
}
