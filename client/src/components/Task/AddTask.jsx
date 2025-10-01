import { Dialog } from "@headlessui/react";
import React, { useState, useEffect, useCallback } from "react";
import { useForm } from "react-hook-form";
import { BiImages } from "react-icons/bi";
import Button from "../Button";
import ModalWrapper from "../ModalWrapper";
import SelectList from "../SelectList";
import Textbox from "../Textbox";
import UserList from "./UserList";
import { useSelector } from "react-redux";
import {
  getStorage,
  ref,
  getDownloadURL,
  uploadBytesResumable,
} from "firebase/storage";
import { app } from "../../utils/firebase";
import {
  useCreateTaskMutation,
  useUpdateTaskMutation,
} from "../../redux/slices/api/taskApiSlice";
import { toast } from "sonner";
import { dateFormatter } from "../../utils";

const LISTS = ["PENDIENTE", "EN PROGRESO", "COMPLETADO"];
const PRIORITY = ["ALTO", "MEDIO", "NORMAL", "BAJO"];

const AddTask = ({ open, setOpen, task }) => {
  const [createTask, { isLoading }] = useCreateTaskMutation();
  const [updateTask, { isLoading: isUpdating }] = useUpdateTaskMutation();

  const currentUser = useSelector((state) => state.auth.user);
  const [team, setTeam] = useState([]);
  const [stage, setStage] = useState(LISTS[0]);
  const [priority, setPriority] = useState(PRIORITY[2]);
  const [assets, setAssets] = useState([]);
  const [uploading, setUploading] = useState(false);
  const [uploadedFileURLs, setUploadedFileURLs] = useState([]);

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm();

  const initializeForm = useCallback(() => {
    if (task) {
      reset({
        title: task.title || "",
        date: dateFormatter(task.date || new Date()),
      });
      setTeam(task.team || []);
      setStage(task.stage?.toUpperCase() || LISTS[0]);
      setPriority(task.priority?.toUpperCase() || PRIORITY[2]);
      setAssets(task.assets || []);
      setUploadedFileURLs(task.assets || []);
    } else {
      reset({
        title: "",
        date: dateFormatter(new Date()),
      });
      setTeam([currentUser]);
      setStage(LISTS[0]);
      setPriority(PRIORITY[2]);
      setAssets([]);
      setUploadedFileURLs([]);
    }
  }, [task, reset, currentUser]);

  useEffect(() => {
    initializeForm();
  }, [initializeForm]);


  const submitHandler = async (data) => {
    setUploading(true);
    try {
      const uploadedURLs = [...uploadedFileURLs];
  
      for (const file of assets) {
        if (file instanceof File) {
          const url = await uploadFile(file);
          uploadedURLs.push(url);
        }
      }
  
      const newData = {
        ...data,
        assets: uploadedURLs,
        team,
        stage,
        priority,
      };
  
      if (task?._id) {
        await updateTask({ ...newData, _id: task._id }).unwrap();
      } else {
        await createTask(newData).unwrap();
      }
  
      toast.success("Tarea guardada correctamente");
      setTimeout(() => {
        setOpen(false);
        window.location.reload();
      }, 500);
    } catch (err) {
      toast.error(err?.data?.message || err.error);
    } finally {
      setUploading(false);
    }
  };

  const handleSelect = (e) => {
    const files = Array.from(e.target.files);
    setAssets((prevAssets) => [...prevAssets, ...files]);
  };

  const uploadFile = async (file) => {
    const storage = getStorage(app);
    const name = new Date().getTime() + file.name;
    const storageRef = ref(storage, name);
    const uploadTask = uploadBytesResumable(storageRef, file);
  
    return new Promise((resolve, reject) => {
      uploadTask.on(
        "state_changed",
        (snapshot) => {
          console.log("Subiendo...");
        },
        (error) => {
          reject(error);
        },
        async () => {
          try {
            const downloadURL = await getDownloadURL(uploadTask.snapshot.ref);
            resolve(downloadURL);
          } catch (error) {
            reject(error);
          }
        }
      );
    });
  };

  return (
    <ModalWrapper open={open} setOpen={setOpen}>
      <form onSubmit={handleSubmit(submitHandler)} className="max-h-[80vh] overflow-y-auto">
        <Dialog.Title
          as="h2"
          className="text-base font-bold leading-6 text-gray-900 mb-4"
        >
          {task ? "ACTUALIZAR TAREA" : "AÑADIR TAREA"}
        </Dialog.Title>
        <div className="mt-2 flex flex-col gap-4">
          <Textbox
            placeholder="Título de la tarea"
            type="text"
            name="title"
            label="Título de la tarea"
            className="w-full rounded"
            register={register("title", { required: "El título es obligatorio" })}
            error={errors.title ? errors.title.message : ""}
          />

          <UserList setTeam={setTeam} team={team} />

          <div className="grid grid-cols-2 gap-4">
            <SelectList
              label="Etapa de la tarea"
              lists={LISTS}
              selected={stage}
              setSelected={setStage}
            />
            <div className="relative">
              <SelectList
                label="Nivel de prioridad"
                lists={PRIORITY}
                selected={priority}
                setSelected={setPriority}
              />
            </div>
          </div>

          <div className="flex gap-4">
            <div className="w-full">
              <Textbox
                placeholder="Fecha"
                type="date"
                name="date"
                label="Fecha de la tarea"
                className="w-full rounded"
                register={register("date", {
                  required: "¡La fecha es obligatoria!",
                })}
                error={errors.date ? errors.date.message : ""}
              />
            </div>

            <div className="w-full flex items-center justify-center mt-4">
              <label
                className="flex items-center gap-1 text-base text-ascent-2 hover:text-ascent-1 cursor-pointer my-4"
                htmlFor="imgUpload"
              >
                <input
                  type="file"
                  className="hidden"
                  id="imgUpload"
                  onChange={handleSelect}
                  accept=".jpg, .png, .jpeg"
                  multiple
                />
                <BiImages />
                <span>Añadir archivos</span>
              </label>
            </div>
          </div>

          <div className="bg-gray-50 py-6 sm:flex sm:flex-row-reverse gap-4">
            {uploading ? (
              <span className="text-sm py-2 text-red-500">
                Subiendo archivos
              </span>
            ) : (
              <Button
                label="Enviar"
                type="submit"
                className="bg-blue-600 px-8 text-sm font-semibold text-white hover:bg-blue-700 sm:w-auto"
              />
            )}

            <Button
              type="button"
              className="bg-white px-5 text-sm font-semibold text-gray-900 sm:w-auto"
              onClick={() => setOpen(false)}
              label="Cancelar"
            />
          </div>
        </div>
      </form>
    </ModalWrapper>
  );
};


export default AddTask;