import axios from "axios";

export const apiClient = axios.create(
    {
        baseURL: 'http://localhost:8081/v1'
    }
)

/*apiClient.interceptors.request.use(
    (config) => {
        // retrieve accessToken from localStorage
        const token = localStorage.getItem('accessToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);*/

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

//refresh token
/*apiClient.interceptors.response.use(
    response => response,
    async error => {
        const originalRequest = error.config;

        if (error.response.status === 403 && localStorage.getItem('refreshToken') != null) {
            // Инициализация счетчика попыток, если он еще не существует
            if (!originalRequest._retryCount) {
                originalRequest._retryCount = 0;
            }

            // Увеличиваем счетчик попыток
            originalRequest._retryCount += 1;

            // Проверяем, если количество попыток меньше или равно 2
            if (originalRequest._retryCount <= 2) {
                try {
                    const refreshToken = localStorage.getItem('refreshToken');
                    const response = await apiClient.post('/refresh', { refreshToken });

                    const newAccessToken = response.data.accessToken;
                    const newRefreshToken = response.data.refreshToken;

                    // Обновляем токены в localStorage
                    localStorage.setItem('accessToken', newAccessToken);
                    localStorage.setItem('refreshToken', newRefreshToken);

                    // Обновляем заголовки для повторного запроса
                    apiClient.defaults.headers.common['Authorization'] = `Bearer ${newAccessToken}`;
                    originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;

                    // Повторяем оригинальный запрос
                    return apiClient(originalRequest);
                } catch (e) {
                    // Обработка ошибок обновления токена
                    console.error(e);
                }
            }
        }

        return Promise.reject(error);
    }
);*/
