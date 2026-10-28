

// ===== CAMBIO DE ROL (TABS) =====
document.querySelectorAll('.role-tab').forEach(tab => {
    tab.addEventListener('click', function() {
        // Quitar active de todos
        document.querySelectorAll('.role-tab').forEach(t => t.classList.remove('active'));
        
        // Poner active al seleccionado
        this.classList.add('active');
        
        // ✅ Actualizar el input oculto con el rol seleccionado
        const rol = this.dataset.role;
        document.getElementById('rolSeleccionado').value = rol;
    });
});

// ... resto del código




// ===== CAMBIO DE ROL (TABS) =====
document.querySelectorAll('.role-tab').forEach(tab => {
    tab.addEventListener('click', function() {
        document.querySelectorAll('.role-tab').forEach(t => t.classList.remove('active'));
        this.classList.add('active');
        // Ocultar error al cambiar de rol
        const errorDiv = document.getElementById('loginError');
        if (errorDiv) errorDiv.classList.add('d-none');
    });
});

// ===== VALIDACIÓN DEL FORMULARIO =====
document.getElementById('loginForm')?.addEventListener('submit', function(e) {
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();
    const btn = document.getElementById('btnLogin');
    const btnText = document.getElementById('btnLoginText');

    // Validar que no estén vacíos
    if (!email || !password) {
        e.preventDefault();
        alert('Por favor, completa todos los campos.');
        return false;
    }

    // Deshabilitar botón mientras se envía
    btn.disabled = true;
    btnText.textContent = 'Cargando...';
});

// ===== LIMPIAR ERROR AL ESCRIBIR =====
document.getElementById('email')?.addEventListener('input', function() {
    const errorDiv = document.getElementById('loginError');
    if (errorDiv) errorDiv.classList.add('d-none');
});
document.getElementById('password')?.addEventListener('input', function() {
    const errorDiv = document.getElementById('loginError');
    if (errorDiv) errorDiv.classList.add('d-none');
});