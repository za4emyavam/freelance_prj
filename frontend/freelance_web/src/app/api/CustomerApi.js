import {apiClient} from "./ApiClient";

export const retrieveAllCustomerTasks = (orderBy, ordering, offset) => apiClient.get("/customer/tasks", {
    params: {
        orderBy: orderBy,
        ordering: ordering,
        offset: offset
    }
})

export const retrieveCustomerTaskByTaskId = (taskId) => apiClient.get(`/customer/tasks/${taskId}`)

export const rejectContractorByTaskId = (taskId, contractorId, reason) =>
    apiClient.post(`/customer/tasks/${taskId}/contractors/${contractorId}/reject`, reason)

export const getContractorById = (id) => apiClient.get(`/customer/contractors/${id}`)

export const acceptContractor = (taskId, contractorId) =>
    apiClient.post(`/customer/tasks/${taskId}/contractors/${contractorId}`)

export const cancelTask = (taskId) => apiClient.delete(`/customer/tasks/${taskId}`)

export const acceptTask = (taskId, rate) => apiClient.post(`/customer/tasks/${taskId}/rate/${rate}`)

export const retrieveFields = () => apiClient.get(`/customer/fields`)

export const saveTask = (task) => apiClient.post(`/customer/tasks`, task)

export const updateTask = (taskId, task) => apiClient.put(`/customer/tasks/${taskId}`, task)

export const retrieveUserInfo = () => apiClient.get(`/customer/user_info`)

export const deactivateProfile = () => apiClient.delete(`/customer/profile`)

export const updateProfile = (user) => apiClient.put(`/customer/user_info`, user)