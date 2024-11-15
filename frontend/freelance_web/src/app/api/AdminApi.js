import {apiClient} from "./ApiClient";

export const retrieveUsersCount = (period) => apiClient.post(`/admin/users`, period)

export const retrieveDoneTasks = (period) => apiClient.post(`/admin/tasks/done`, period)

export const retrieveCancelledTasks = (period) => apiClient.post(`/admin/tasks/cancelled`, period)

export const retrieveNotDoneTasks = (period) => apiClient.post(`/admin/tasks/not_done`, period)

export const retrieveAvgRating = () => apiClient.get(`/admin/avg_rating`)

export const retrieveAllFields = () => apiClient.get(`/admin/fields`)

export const retrieveFieldById = (fieldId) => apiClient.get(`/admin/field/${fieldId}`)

export const updateField = (field) => apiClient.put(`/admin/fields`, field)

export const createField = (field) => apiClient.post(`/admin/fields`, field)