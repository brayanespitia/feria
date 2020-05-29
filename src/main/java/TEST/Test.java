/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST;

import DAO.AlumnoJpaController;
import DAO.CategoriaJpaController;
import DAO.Conexion;
import DAO.EventoJpaController;
import DAO.TipoJpaController;
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
        
        
        System.out.println("creacion de evento");
        AlumnoJpaController eDao = new AlumnoJpaController();
        Evento e = new Evento("nombre", 2004-05-20);
        eDao.create(a);
        
        System.out.println("creando tipo evento");
        TipoJpaController tDao = new TipoJpaController();
        Tipo t = new Tipo(10,"feria de ciencia");
        tDao.create(t);
        
        
        System.out.println("creando categoria");
        CategoriaJpaController caDao = new CategoriaJpaController();
        Categoria ca = new Categoria (11,"ciencia");        
        Categoria cate = caDao.findCategoria(Integer.SIZE);
        
        
        
        
    }
}
