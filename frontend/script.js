document.getElementById('btn-play').addEventListener('click', () => {
    const username = document.getElementById('username').value.trim();

    if (username === "") {
        alert("Necesitas escribir un apodo para jugar. Favor de ingresar un apodo.");
    } else {
        console.log("¿Listo para jugar,", username, "?");
        
    }
});