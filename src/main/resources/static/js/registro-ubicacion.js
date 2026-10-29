document.addEventListener('DOMContentLoaded', function() {
    const deptoSelect = document.getElementById('departamentoSelect');
    const provSelect = document.getElementById('provinciaSelect');
    const distSelect = document.getElementById('distritoSelect');
    const deptoTexto = document.getElementById('departamentoTexto');

    // 1. Al cambiar Departamento, cargamos Provincias
    deptoSelect.addEventListener('change', function() {
        const deptoId = this.value;
        
        // Guardamos el nombre del departamento seleccionado (ej. "Cusco") en el campo oculto
        deptoTexto.value = this.options[this.selectedIndex].text;

        // Limpiamos los otros selects
        provSelect.innerHTML = '<option value="">-- Seleccionar --</option>';
        distSelect.innerHTML = '<option value="">-- Seleccionar --</option>';

        if (deptoId) {
            fetch(`/api/ubicacion/provincias/${deptoId}`)
                .then(res => res.json())
                .then(data => {
                    data.forEach(prov => {
                        const option = document.createElement('option');
                        option.value = prov.id;
                        option.text = prov.name;
                        provSelect.appendChild(option);
                    });
                })
                .catch(err => console.error("Error cargando provincias:", err));
        }
    });

    // 2. Al cambiar Provincia, cargamos Distritos
    provSelect.addEventListener('change', function() {
        const provId = this.value;
        
        distSelect.innerHTML = '<option value="">-- Seleccionar --</option>';

        if (provId) {
            fetch(`/api/ubicacion/distritos/${provId}`)
                .then(res => res.json())
                .then(data => {
                    data.forEach(dist => {
                        const option = document.createElement('option');
                        option.value = dist.id;
                        option.text = dist.name;
                        distSelect.appendChild(option);
                    });
                })
                .catch(err => console.error("Error cargando distritos:", err));
        }
    });
});