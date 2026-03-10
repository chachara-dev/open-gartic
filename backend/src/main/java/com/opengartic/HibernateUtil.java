package com.opengartic;

import io.github.cdimascio.dotenv.Dotenv;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final Dotenv dotenv = Dotenv.configure().load();
    private static final SessionFactory sessionFactory;

    static {
        try {

            String url = "jdbc:postgresql://"
                    + dotenv.get("DB_HOST") + ":"
                    + dotenv.get("DB_PORT") + "/"
                    + dotenv.get("DB_NAME");

            Configuration configuration = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Jugador.class);

            configuration.setProperty("hibernate.connection.url", url);
            configuration.setProperty("hibernate.connection.username", dotenv.get("DB_USER"));
            configuration.setProperty("hibernate.connection.password", dotenv.get("DB_PASSWORD"));
            configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");

            sessionFactory = configuration.buildSessionFactory(
                    new StandardServiceRegistryBuilder()
                            .applySettings(configuration.getProperties())
                            .build()
            );

        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}