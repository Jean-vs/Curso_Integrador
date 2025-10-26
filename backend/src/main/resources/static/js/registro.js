document.addEventListener("DOMContentLoaded", function() {
    const form = document.getElementById("formRegistro");
    const password = document.getElementById("password");
    const confirmar = document.getElementById("confirmar");
    const errorMsg = document.getElementById("errorConfirmacion");

    form.addEventListener("submit", function(event) {
        if (password.value !== confirmar.value) {
            //event.preventDefault();
            errorMsg.style.display = "block";
            confirmar.classList.add("is-invalid");
        } else {
            errorMsg.style.display = "none";
            confirmar.classList.remove("is-invalid");
        }
    });
});
