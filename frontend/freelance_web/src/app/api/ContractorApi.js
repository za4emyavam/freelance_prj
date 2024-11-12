import {apiClient} from "./ApiClient";

export const getAllTasksWithSort = (orderBy, ordering, offset, sort, value) => apiClient.get(`/contractor/tasks`, {
    params: {
        sort: sort,
        orderBy: orderBy,
        ordering: ordering,
        offset: offset
    },
    headers: {
        Value: value
    }
})

export const getAllFields = () => apiClient.get(`/contractor/fields`)

export const getContractorProfile = () => apiClient.get(`/contractor/profile`)

export const deactivateContractorProfile = () => apiClient.delete(`/contractor/profile`)

export const updateContractorProfile = (user) => apiClient.put(`/contractor/user_info`, user)

export const retrieveContractorTaskById = (taskId) => apiClient.get(`/contractor/tasks/${taskId}`)

export const agreedToTask = (taskId) => apiClient.post(`/contractor/tasks/${taskId}`)

export const retrieveAllContractorsTasks = (orderBy, ordering, offset, sort, value) => apiClient.get(`/contractor/tasks/contractor`, {
    params: {
        sort: sort,
        orderBy: orderBy,
        ordering: ordering,
        offset: offset
    },
    headers: {
        Value: value
    }
})