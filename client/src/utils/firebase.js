// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: import.meta.env.VITE_FIREBASE_API_KEY,
  authDomain: "taskmanager-81161.firebaseapp.com",
  projectId: "taskmanager-81161",
  storageBucket: "taskmanager-81161.appspot.com",
  messagingSenderId: "999852287685",
  appId: "1:999852287685:web:4b86788b8467d9bceaf2ac",
  measurementId: "G-VMY852NLX5"
};

// Initialize Firebase
export const app = initializeApp(firebaseConfig);