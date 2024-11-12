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


                apiClient.interceptors.request.use(
                    (config) => {
                        config.headers.Authorization = `Bearer ${response.data.accessToken}`
                        return config
                    }
                )
                apiClient.interceptors.response.use(
                    response => response,
                    async error => {
                        const originalRequest = error.config;

                        if (error.response.status === 403 && !originalRequest._retry) {
                            originalRequest._retry = true;

                            try {
                                const refreshToken = localStorage.getItem('refreshToken');
                                const response = await apiClient.post('/refresh', {refreshToken});
                                const newAccessToken = response.data.accessToken;

                                localStorage.setItem('accessToken', newAccessToken);
                                apiClient.defaults.headers.common['Authorization'] = `Bearer ${newAccessToken}`;
                                originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;

                                return apiClient(originalRequest);
                            } catch (e) {
                                console.error(e);
                            }
                        }

                        return Promise.reject(error)
                    }
                )

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