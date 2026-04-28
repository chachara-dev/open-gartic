// =============================================================
//  CONFIGURACIÓN DE AVATARES
//  Agrega o quita rutas aquí cuando añadas imágenes a /imagenes/
// =============================================================
const AVATARES = [
    "/Imagenes Chachara/Ideas de Logos/Logo Chachara.png",
];

const AVATAR_DEFAULT = AVATARES[0];

// =============================================================
//  Estado: avatar actualmente seleccionado en cada vista
// =============================================================
let guestAvatarUrl = AVATAR_DEFAULT;
let regAvatarUrl   = AVATAR_DEFAULT;

// =============================================================
//  Construcción del grid de avatares
// =============================================================
function buildAvatarPicker(pickerEl, previewEl, onSelect) {
    setAvatarPreview(previewEl, AVATAR_DEFAULT);

    AVATARES.forEach((path, index) => {
        const btn = document.createElement('div');
        btn.className = 'avatar-option' + (index === 0 ? ' selected' : '');
        btn.dataset.path = path;

        const img = document.createElement('img');
        img.src = path;
        img.alt = 'Avatar ' + (index + 1);
        btn.appendChild(img);

        btn.addEventListener('click', () => {
            pickerEl.querySelectorAll('.avatar-option').forEach(el => el.classList.remove('selected'));
            btn.classList.add('selected');
            setAvatarPreview(previewEl, path);
            onSelect(path);
        });

        pickerEl.appendChild(btn);
    });
}

function setAvatarPreview(previewEl, path) {
    previewEl.innerHTML = '';
    const img = document.createElement('img');
    img.src = path;
    img.alt = 'Avatar seleccionado';
    previewEl.appendChild(img);
}

// =============================================================
//  Inicializar pickers al cargar
// =============================================================
document.addEventListener('DOMContentLoaded', () => {
    buildAvatarPicker(
        document.getElementById('guest-avatar-picker'),
        document.getElementById('avatar-display'),
        (path) => { guestAvatarUrl = path; }
    );

    buildAvatarPicker(
        document.getElementById('reg-avatar-picker'),
        document.getElementById('reg-avatar-display'),
        (path) => { regAvatarUrl = path; }
    );
});

// =============================================================
//  Cambio de vista
// =============================================================
function switchView(viewId) {
    document.getElementById('view-guest').classList.add('hidden');
    document.getElementById('view-login').classList.add('hidden');
    document.getElementById('view-register').classList.add('hidden');
    document.getElementById(viewId).classList.remove('hidden');
}

// =============================================================
//  1. Jugar como invitado
// =============================================================
document.getElementById('btn-play').addEventListener('click', () => {
    const username = document.getElementById('username').value.trim();
    if (username === "") {
        alert("Necesitas escribir un apodo para jugar.");
        return;
    }
    localStorage.setItem('username',  username);
    localStorage.setItem('avatarUrl', guestAvatarUrl);
    window.location.href = 'game.html?username=' + encodeURIComponent(username);
});

// =============================================================
//  2. Iniciar sesión
// =============================================================
const API_URL = "http://localhost:8081/api/cuentas";

document.getElementById('btn-login').addEventListener('click', async () => {
    const email = document.getElementById('login-email').value.trim();
    const pass  = document.getElementById('login-pass').value.trim();

    if (!email || !pass) return alert("Llena todos los campos.");

    try {
        const response = await fetch(API_URL + '/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ correo: email, contrasena: pass })
        });

        if (response.ok) {
            const data = await response.json();
            alert(data.mensaje);
            localStorage.setItem('username',  data.nombreUsuario);
            localStorage.setItem('avatarUrl', data.avatarUrl);
            window.location.href = 'game.html';
        } else {
            const errorMsg = await response.text();
            alert("Error: " + errorMsg);
        }
    } catch (error) {
        alert("No se pudo conectar con el servidor. ¿Ya corriste el backend?");
    }
});

// =============================================================
//  3. Registro
// =============================================================
document.getElementById('btn-register').addEventListener('click', async () => {
    const username = document.getElementById('reg-username').value.trim();
    const email    = document.getElementById('reg-email').value.trim();
    const pass     = document.getElementById('reg-pass').value.trim();

    if (!username || !email || !pass) return alert("Todos los campos son obligatorios.");

    try {
        const response = await fetch(API_URL + '/registro', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                nombreUsuario: username,
                correo:        email,
                contrasena:    pass,
                avatarUrl:     regAvatarUrl
            })
        });

        const mensaje = await response.text();

        if (response.ok) {
            alert(mensaje);
            switchView('view-login');
        } else {
            alert(mensaje);
        }
    } catch (error) {
        alert("Error de conexión. Revisa que el servidor esté en el puerto 8081.");
    }
});