const API_URL = "http://localhost:8081/api/cuentas";

// Función para cambiar entre las vistas (Invitado, Login, Registro)
function switchView(viewId) {
    document.getElementById('view-guest').classList.add('hidden');
    document.getElementById('view-login').classList.add('hidden');
    document.getElementById('view-register').classList.add('hidden');
    document.getElementById(viewId).classList.remove('hidden');
}

// 1. Lógica: Jugar como invitado (Solo local por ahora)
document.getElementById('btn-play').addEventListener('click', () => {
    const username = document.getElementById('username').value.trim();
    if (username === "") {
        alert("Necesitas escribir un apodo para jugar.");
    } else {
        console.log("Jugando como invitado:", username);
        // Redirigir a la sala de juego
        window.location.href = `game.html?username=${encodeURIComponent(username)}`;
    }
});

// 2. Lógica: Iniciar Sesión (Conexión al Backend)
document.getElementById('btn-login').addEventListener('click', async () => {
    const email = document.getElementById('login-email').value.trim();
    const pass = document.getElementById('login-pass').value.trim();

    if (!email || !pass) return alert("Llena todos los campos.");

    try {
        const response = await fetch(`${API_URL}/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ correo: email, contrasena: pass })
        });

        const mensaje = await response.text();

        if (response.ok) {
            alert(mensaje); // "¡Bienvenido, Usuario!"
            // Guardar sesión y redirigir
            localStorage.setItem('username', email); // O extraer del mensaje
            window.location.href = 'game.html';
        } else {
            alert("Error: " + mensaje);
        }
    } catch (error) {
        alert("No se pudo conectar con el servidor. ¿Ya corriste el backend?");
    }
});

// 3. Lógica: Registrarse (Conexión al Backend)
document.getElementById('btn-register').addEventListener('click', async () => {
    const username = document.getElementById('reg-username').value.trim();
    const email = document.getElementById('reg-email').value.trim();
    const pass = document.getElementById('reg-pass').value.trim();

    if (!username || !email || !pass) return alert("Todos los campos son obligatorios.");

    try {
        const response = await fetch(`${API_URL}/registro`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ 
                nombreUsuario: username, 
                correo: email, 
                contrasena: pass 
            })
        });

        const mensaje = await response.text();

        if (response.ok) {
            alert(mensaje);
            switchView('view-login'); // Lo mandamos a loguearse tras el éxito
        } else {
            alert(mensaje);
        }
    } catch (error) {
        alert("Error de conexión. Revisa que el servidor esté en el puerto 8080.");
    }
});