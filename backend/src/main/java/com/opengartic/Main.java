package com.opengartic;

import java.util.UUID;
import org.hibernate.Session;

public class Main {
    public static void main(String[] args) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        UUID sala = UUID.fromString("4bdd4c03-bf9b-4e3d-85dd-109e69469f92");

        Jugador j1 = new Jugador(sala, "Jorge", true);
        Jugador j2 = new Jugador(sala, "Ana", false);

        session.persist(j1);
        session.persist(j2);

        session.getTransaction().commit();

        System.out.println("Dos jugadores agregados!");

        session.beginTransaction();

        session.remove(j1);

        session.getTransaction().commit();

        System.out.println("Jugador eliminado!");

        session.close();

        System.out.println("Base de datos lista!");
    }
}