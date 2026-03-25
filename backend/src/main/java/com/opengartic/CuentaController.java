package com.opengartic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

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

        //ENCRIPTACIÓN
        String passEncriptada = passwordEncoder.encode(nuevaCuenta.getContrasena());
        nuevaCuenta.setContrasena(passEncriptada);

        cuentaRepository.save(nuevaCuenta);
        return ResponseEntity.ok("Cuenta creada exitosamente.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> iniciarSesion(@RequestBody Cuenta credenciales) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findByCorreo(credenciales.getCorreo());

        if (cuentaOpt.isPresent()) {
            // Comparamos el texto plano del login contra el hash de la BD
            if (passwordEncoder.matches(credenciales.getContrasena(), cuentaOpt.get().getContrasena())) {
                return ResponseEntity.ok("¡Bienvenido, " + cuentaOpt.get().getNombreUsuario() + "!");
            }
        }
        
        return ResponseEntity.status(401).body("Error: Correo o contraseña incorrectos.");
    }
}