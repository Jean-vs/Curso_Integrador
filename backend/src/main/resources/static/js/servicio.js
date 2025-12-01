document.addEventListener("DOMContentLoaded", () => {
    console.log("servicio.js cargado correctamente ✅");

    document.querySelectorAll(".editar-btn").forEach(boton => {
        boton.addEventListener("click", () => {
            const id = boton.dataset.id;
            const nombre = boton.dataset.nombre;
            const descripcion = boton.dataset.descripcion;
            const precio = boton.dataset.precio;

            console.log("Servicio cargado:", { id, nombre, descripcion, precio });

            document.getElementById("editarIdServicio").value = id;
            document.getElementById("editarNombreServicio").value = nombre;
            document.getElementById("editarDescripcion").value = descripcion;
            document.getElementById("editarPrecioBase").value = precio;
        });
    });
});
