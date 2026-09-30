import { request } from '@/utils/request'

export const toggleFavorite = (productId: number) =>
  request<{ favorited: boolean }>({ url: `/favorite/toggle/${productId}`, method: 'POST' })

export const myFavorites = () =>
  request<{ total: number; records: any[] }>({ url: '/favorite/list' })
