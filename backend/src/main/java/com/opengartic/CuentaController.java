package com.opengartic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/cuentas")
@CrossOrigin(origins = "*")
public class CuentaController {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/registro")
    public ResponseEntity<String> registrarCuenta(@RequestBody Cuenta nuevaCuenta) {
        if (cuentaRepository.findByCorreo(nuevaCuenta.getCorreo()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: El correo ya está registrado.");
        }
        if (cuentaRepository.findByNombreUsuario(nuevaCuenta.getNombreUsuario()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: El nombre de usuario ya está en uso.");
        }

        // Encriptación de contraseña
        String passEncriptada = passwordEncoder.encode(nuevaCuenta.getContrasena());
        nuevaCuenta.setContrasena(passEncriptada);

        // Avatar por defecto si el frontend no envía ninguno
        if (nuevaCuenta.getAvatar() == null || nuevaCuenta.getAvatar().isBlank()) {
            nuevaCuenta.setAvatar("/imagenes/avatar_01.png");
        }

        cuentaRepository.save(nuevaCuenta);
        return ResponseEntity.ok("Cuenta creada exitosamente.");
    }

    // Devuelve JSON con { mensaje, nombreUsuario, avatar }
    // para que el frontend pueda mostrar el recuadro del jugador en game.html
    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody Cuenta credenciales) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findByCorreo(credenciales.getCorreo());

        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            if (passwordEncoder.matches(credenciales.getContrasena(), cuenta.getContrasena())) {

                // Fallback por si alguna cuenta antigua no tiene avatar asignado
                String avatarPath = (cuenta.getAvatar() != null && !cuenta.getAvatar().isBlank())
                        ? cuenta.getAvatar()
                        : "/imagenes/avatar_01.png";

                Map<String, String> respuesta = new HashMap<>();
                respuesta.put("mensaje",       "¡Bienvenido, " + cuenta.getNombreUsuario() + "!");
                respuesta.put("nombreUsuario", cuenta.getNombreUsuario());
                respuesta.put("avatarUrl",     avatarPath);   // clave que espera script.js

                return ResponseEntity.ok(respuesta);
            }
        }

        return ResponseEntity.status(401).body("Error: Correo o contraseña incorrectos.");
    }
}