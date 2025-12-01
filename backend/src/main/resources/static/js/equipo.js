// =====================================================
// VARIABLES GLOBALES
// =====================================================
let modoActual = "agregar";

const modalAgregarEquipo = new bootstrap.Modal(document.getElementById("modalAgregarEquipo"));
const modalEditarEquipo = new bootstrap.Modal(document.getElementById("modalEditarEquipo"));
const modalEmpleadosAgregar = new bootstrap.Modal(document.getElementById("modalEmpleadosAgregar"));
const modalEmpleadosEditar = new bootstrap.Modal(document.getElementById("modalEmpleadosEditar"));


// =====================================================
// ACTUALIZAR CONTADOR
// =====================================================
function actualizarContadores() {

    if (modoActual === "agregar") {
        let total = document.querySelectorAll(".check-empleado-agregar:checked").length;
        document.getElementById("countParticipantesAgregar").value = `${total} seleccionados`;
    }

    if (modoActual === "editar") {
        let total = document.querySelectorAll(".check-empleado-editar:checked").length;
        document.getElementById("countParticipantesEditar").value = `${total} seleccionados`;
    }
}


// =====================================================
// EVENTOS DE CHECKBOXES
// =====================================================
document.querySelectorAll(".check-empleado-agregar").forEach(cb => {
    cb.addEventListener("change", actualizarContadores);
});

document.querySelectorAll(".check-empleado-editar").forEach(cb => {
    cb.addEventListener("change", actualizarContadores);
});


// =====================================================
// ABRIR SELECTOR AGREGAR
// =====================================================
function abrirSelectorAgregar() {

    modoActual = "agregar";

    modalAgregarEquipo.hide();
    setTimeout(() => modalEmpleadosAgregar.show(), 250);
}


// =====================================================
// GUARDAR SELECCIÓN AGREGAR  ★ IMPORTANTE ★
// =====================================================
function guardarSeleccionAgregar() {

    let seleccionados = [];

    document.querySelectorAll(".check-empleado-agregar:checked").forEach(chk => {
        seleccionados.push(chk.value);
    });

    document.getElementById("participantesIdsAgregar").value = seleccionados.join(",");

    actualizarContadores();

    modalEmpleadosAgregar.hide();
    setTimeout(() => modalAgregarEquipo.show(), 250);
}


// =====================================================
// ABRIR SELECTOR EDITAR
// =====================================================
function abrirSelectorEditar() {

    modoActual = "editar";

    modalEditarEquipo.hide();
    setTimeout(() => modalEmpleadosEditar.show(), 250);
}


// =====================================================
// GUARDAR SELECCIÓN EDITAR  ★ IMPORTANTE ★
// =====================================================
function guardarSeleccionEditar() {

    let seleccionados = [];

    document.querySelectorAll(".check-empleado-editar:checked").forEach(chk => {
        seleccionados.push(chk.value);
    });

    document.getElementById("participantesIdsEditar").value = seleccionados.join(",");

    actualizarContadores();

    modalEmpleadosEditar.hide();
    setTimeout(() => modalEditarEquipo.show(), 250);
}


// =====================================================
// CARGAR DATOS AL EDITAR
// =====================================================
document.querySelectorAll(".editar-btn").forEach(btn => {

    btn.addEventListener("click", () => {

        modoActual = "editar";

        // Cargar datos
        document.getElementById("editarId").value = btn.dataset.id;
        document.getElementById("editarNombre").value = btn.dataset.nombre;
        document.getElementById("editarLider").value = btn.dataset.liderId;
        document.getElementById("editarEstado").value = btn.dataset.estado;

        // Resetear checks
        document.querySelectorAll(".check-empleado-editar").forEach(c => c.checked = false);

        actualizarContadores();
    });
});
