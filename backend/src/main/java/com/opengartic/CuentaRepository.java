package com.opengartic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, UUID> {
    Optional<Cuenta> findByCorreo(String correo);
    Optional<Cuenta> findByNombreUsuario(String nombreUsuario);
}