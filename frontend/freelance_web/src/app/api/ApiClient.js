import axios from "axios";

export const apiClient = axios.create(
    {
        baseURL: 'http://localhost:8081/v1'
    }
)

apiClient.interceptors.request.use(
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
);


//refresh token
/*
apiClient.interceptors.response.use(
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
