package com.opengartic;

import java.util.UUID;
import org.hibernate.Session;

public class Main {
    public static void main(String[] args) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        // =====================
        // CREATE - 3 jugadores
        // =====================
        session.beginTransaction();

        UUID sala = UUID.fromString("4bdd4c03-bf9b-4e3d-85dd-109e69469f92");

        Jugador j1 = new Jugador(sala, "Jorge", true);
        Jugador j2 = new Jugador(sala, "Ana", false);
        Jugador j3 = new Jugador(sala, "Carlos", false);

        session.persist(j1);
        session.persist(j2);
        session.persist(j3);

        session.getTransaction().commit();
        System.out.println("✅ CREATE: 3 jugadores agregados!");

        // =====================
        // READ - Leer jugador 3
        // =====================
        session.beginTransaction();

        Jugador jugadorLeido = session.get(Jugador.class, j3.getIdJugador());

        if (jugadorLeido != null) {
            System.out.println("✅ READ: Jugador 3 encontrado:");
            System.out.println("   - ID: " + jugadorLeido.getIdJugador());
            System.out.println("   - Nickname: " + jugadorLeido.getNickname());
            System.out.println("   - Es host: " + jugadorLeido.getEsHost());
        }

        session.getTransaction().commit();

        // ========================
        // UPDATE - Modificar j1
        // ========================
        session.beginTransaction();

        Jugador jugadorActualizar = session.get(Jugador.class, j1.getIdJugador());

        if (jugadorActualizar != null) {
            jugadorActualizar.setNickname("JorgeActualizado");
            jugadorActualizar.setEsHost(false);
            session.merge(jugadorActualizar);
            System.out.println("✅ UPDATE: Jugador 1 modificado!");
            System.out.println("   - Nuevo nickname: " + jugadorActualizar.getNickname());
        }

        session.getTransaction().commit();

        // ========================
        // DELETE - Borrar j2
        // ========================
        session.beginTransaction();

        Jugador jugadorEliminar = session.get(Jugador.class, j2.getIdJugador());

        if (jugadorEliminar != null) {
            session.remove(jugadorEliminar);
            System.out.println("✅ DELETE: Jugador 2 (Ana) eliminado!");
        }

        session.getTransaction().commit();

        session.close();
        System.out.println("🏁 CRUD completado!");
    }
}