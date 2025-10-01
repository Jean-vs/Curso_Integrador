import React from "react";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import Textbox from "../components/Textbox";
import Button from "../components/Button";
import { useDispatch } from "react-redux";
import { useRegisterMutation } from "../redux/slices/api/authApiSlice";
import { toast } from "sonner";
import { setCredentials } from "../redux/slices/authSlice";
import Loading from "../components/Loader";

const Register = () => {
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm();

  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [registerUser, { isLoading }] = useRegisterMutation();

  const submitHandler = async (data) => {
    if (data.password !== data.confirmPassword) {
      toast.error("Las contraseñas no coinciden");
      return;
    }

    try {
      const result = await registerUser({
        nombre: data.nombre,
        email: data.email,
        password: data.password,
      }).unwrap();

      dispatch(setCredentials(result));
      toast.success("Registro exitoso");
      navigate("/dashboard");
    } catch (error) {
      console.log(error);
      toast.error(error?.data?.message || "Error al registrarse");
    }
  };

  return (
    <div className="w-full min-h-screen flex items-center justify-center flex-col lg:flex-row bg-[#f3f4f6]">
      <div className="w-full md:w-auto flex gap-0 md:gap-40 flex-col md:flex-row items-center justify-center">
        <div className="h-full w-full lg:w-2/3 flex flex-col items-center justify-center">
          <div className="w-full md:max-w-lg 2xl:max-w-3xl flex flex-col items-center justify-center gap-5 md:gap-y-10 2xl:-mt-20">
            <span className="flex gap-1 py-1 px-3 border rounded-full text-sm md:text-base border-gray-300 text-gray-600">
              ¡Crea tu cuenta para empezar a organizar tareas!
            </span>
            <p className="flex flex-col gap-0 md:gap-4 text-4xl md:text-6xl 2xl:text-7xl font-black text-center text-green-700">
              <span>Regístrate ahora</span>
              <span className="text-3xl md:text-5xl 2xl:text-6xl">y empieza gratis</span>
            </p>
            
          </div>
        </div>

        <div className="w-full md:w-1/3 p-4 md:p-1 flex flex-col justify-center items-center">
          <form
            onSubmit={handleSubmit(submitHandler)}
            className="form-container w-full md:w-[400px] flex flex-col gap-y-8 bg-white px-10 pt-14 pb-14"
          >
            <div>
              <p className="text-green-600 text-3xl font-bold text-center">¡Crea tu cuenta!</p>
              <p className="text-center text-base text-gray-700">
                Ingresa tus datos para continuar.
              </p>
            </div>

            <div className="flex flex-col gap-y-5">
              <Textbox
                placeholder="Tu nombre completo"
                type="text"
                name="nombre"
                label="Nombre"
                className="w-full rounded-full"
                register={register("nombre", {
                  required: "¡El nombre es requerido!",
                })}
                error={errors.nombre ? errors.nombre.message : ""}
              />

              <Textbox
                placeholder="email@ejemplo.com"
                type="email"
                name="email"
                label="Correo electrónico"
                className="w-full rounded-full"
                register={register("email", {
                  required: "¡El correo es requerido!",
                })}
                error={errors.email ? errors.email.message : ""}
              />

              <Textbox
                placeholder="Tu contraseña"
                type="password"
                name="password"
                label="Contraseña"
                className="w-full rounded-full"
                register={register("password", {
                  required: "¡La contraseña es requerida!",
                })}
                error={errors.password ? errors.password.message : ""}
              />

              <Textbox
                placeholder="Repite tu contraseña"
                type="password"
                name="confirmPassword"
                label="Confirmar Contraseña"
                className="w-full rounded-full"
                register={register("confirmPassword", {
                  required: "¡Confirma tu contraseña!",
                })}
                error={errors.confirmPassword ? errors.confirmPassword.message : ""}
              />

              {isLoading ? (
                <Loading />
              ) : (
                <Button
                  type="submit"
                  label="Registrarse"
                  className="w-full h-10 bg-green-700 text-white rounded-full"
                />
              )}
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Register;
