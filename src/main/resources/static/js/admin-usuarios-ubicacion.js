document.addEventListener('DOMContentLoaded', function() {
    const deptoSelect = document.getElementById('adminDeptoSelect');
    const provSelect = document.getElementById('adminProvSelect');
    const distSelect = document.getElementById('adminDistSelect');
    const deptoTexto = document.getElementById('adminDeptoTexto');

    // Si ya hay un departamento guardado (al editar), forzamos el texto
    if(deptoSelect && deptoSelect.value) {
        deptoTexto.value = deptoSelect.options[deptoSelect.selectedIndex].text;
    }

    if(deptoSelect) {
        deptoSelect.addEventListener('change', function() {
            deptoTexto.value = this.options[this.selectedIndex].text;
            
            provSelect.innerHTML = '<option value="">-- Seleccionar --</option>';
            distSelect.innerHTML = '<option value="">-- Seleccionar --</option>';
            
            if (this.value) {
                fetch(`/api/ubicacion/provincias/${this.value}`)
                    .then(res => res.json())
                    .then(data => {
                        data.forEach(p => provSelect.innerHTML += `<option value="${p.id}">${p.name}</option>`);
                    })
                    .catch(err => console.error("Error:", err));
            }
        });
    }

    if(provSelect) {
        provSelect.addEventListener('change', function() {
            distSelect.innerHTML = '<option value="">-- Seleccionar --</option>';
            
            if (this.value) {
                fetch(`/api/ubicacion/distritos/${this.value}`)
                    .then(res => res.json())
                    .then(data => {
                        data.forEach(d => distSelect.innerHTML += `<option value="${d.id}">${d.name}</option>`);
                    })
                    .catch(err => console.error("Error:", err));
            }
        });
    }
});