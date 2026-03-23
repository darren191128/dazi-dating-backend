import apiClient from './client'

export interface Lawyer {
  id: number
  name: string
  title: string
  introduction: string
  photoUrl: string
  specialty: string
  email: string
  phone: string
}

export const lawyerApi = {
  getAll: () => apiClient.get<Lawyer[]>('/lawyers'),
  getById: (id: number) => apiClient.get<Lawyer>(`/lawyers/${id}`),
  getBySpecialty: (specialty: string) => apiClient.get<Lawyer[]>(`/lawyers/search?specialty=${specialty}`)
}
