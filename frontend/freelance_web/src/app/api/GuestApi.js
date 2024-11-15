import {apiClient} from "./ApiClient";

export const executeJwtAuthenticationService = (username, password) => apiClient.post("/login", {
    "username": username,
    "password": password
})

export const registration = (user) => apiClient.post("/create", user);