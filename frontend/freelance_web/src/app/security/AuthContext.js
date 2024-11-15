import {apiClient} from "../api/ApiClient";
import {createContext, useContext, useState} from "react";
import {executeJwtAuthenticationService} from "../api/GuestApi";
import {jwtDecode} from "jwt-decode";

export const AuthContext = createContext(undefined)
export const useAuth = () => useContext(AuthContext)

export default function AuthProvider({children}) {
    const [isAuthenticated, setIsAuthenticated] = useState(() => {
        return localStorage.getItem('isAuthenticated') === 'true';
    });

    const requestInterceptor = apiClient.interceptors.request.use(
        (config) => {
            // retrieve accessToken from localStorage
            const token = localStorage.getItem('accessToken');
            if (token) {
                config.headers.Authorization = `Bearer ${token}`;
            }
            return config;
        },
        (error) => Promise.reject(error)
    );

    async function login(username, password) {

        try {
            const response = await executeJwtAuthenticationService(username, password)

            if (response.status === 200) {
                setIsAuthenticated(true)
                localStorage.setItem('isAuthenticated', 'true')
                const decoded = jwtDecode(response.data.accessToken)
                localStorage.setItem("uid", decoded.id)
                localStorage.setItem("email", decoded.sub)
                localStorage.setItem("role", decoded.role)
                localStorage.setItem("exp_jwt", decoded.exp)
                localStorage.setItem("accessToken", response.data.accessToken)
                localStorage.setItem("refreshToken", response.data.refreshToken)


                apiClient.interceptors.request.use(requestInterceptor)
                apiClient.interceptors.response.use(
                    response => response,
                    async error => {
                        const originalRequest = error.config;

                        if (error.response.status === 403 && !originalRequest._retry && error.response.data === "Token expired.") {
                            originalRequest._retry = true;

                            try {
                                const refreshToken = localStorage.getItem('refreshToken');

                                // Запрашиваем новый accessToken
                                const response = await apiClient.post('/refresh', { refreshToken });
                                const newAccessToken = response.data.accessToken;

                                // Обновляем accessToken и refreshToken в localStorage
                                localStorage.setItem('accessToken', newAccessToken);
                                localStorage.setItem('refreshToken', response.data.refreshToken);

                                // Удаляем request interceptor перед повторным запросом
                                apiClient.interceptors.request.eject(requestInterceptor);

                                // Обновляем заголовок Authorization для originalRequest
                                originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;

                                // Повторяем запрос с новым токеном
                                const newResponse = await apiClient(originalRequest);

                                // Восстанавливаем request interceptor с обновленным токеном
                                apiClient.interceptors.request.use(
                                    (config) => {
                                        const token = localStorage.getItem('accessToken');
                                        if (token) {
                                            config.headers.Authorization = `Bearer ${token}`;
                                        }
                                        return config;
                                    },
                                    (error) => Promise.reject(error)
                                );

                                return newResponse;

                            } catch (e) {
                                console.error("Ошибка при обновлении токена:", e);
                            }
                        }

                        return Promise.reject(error);
                    }
                );

                return true
            } else {
                logout()
                return false
            }
        } catch (error) {
            console.error(error)
            logout()
            return false
        }
    }

    function logout() {
        setIsAuthenticated(false)
        /*logoutApi(localStorage.getItem("email"))
            .catch(() => console.log("error"))*/
        localStorage.removeItem('isAuthenticated')
        localStorage.removeItem("exp_jwt")
        localStorage.removeItem("uid")
        localStorage.removeItem("email")
        localStorage.removeItem("role")
        localStorage.removeItem("accessToken")
        localStorage.removeItem("refreshToken")
        apiClient.interceptors.request.clear()
        apiClient.interceptors.response.clear()

    }

    return (
        <AuthContext.Provider value={{isAuthenticated, login, logout}}>
            {children}
        </AuthContext.Provider>
    )
}