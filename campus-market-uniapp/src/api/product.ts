import { request } from '@/utils/request'

export interface ProductVO {
  id: number
  sellerId: number
  sellerNickname?: string
  title: string
  description?: string
  price: number
  originalPrice?: number
  categoryId: number
  condition: number
  location?: string
  viewCount?: number
  createTime?: string
  coverUrl?: string
  images?: string[]
}

export interface Category {
  id: number
  name: string
}

export interface PageResult<T> {
  records: T[]
  total: number
  current: number
  size: number
}

// 发布商品（需要登录，token 自动带上）
export function createProduct(data: Record<string, any>) {
  return request<ProductVO>({ url: '/product/create', method: 'POST', data })
}

// 商品列表（分类/关键词/分页）
export function getProductList(params: { categoryId?: number; keyword?: string; page: number; size: number }) {
  return request<PageResult<ProductVO>>({ url: '/product/list', method: 'GET', data: params })
}

// 商品详情
export function getProductDetail(id: number) {
  return request<ProductVO>({ url: `/product/detail/${id}`, method: 'GET' })
}

// 分类列表
export function getCategoryList() {
  return request<Category[]>({ url: '/category/list', method: 'GET' })
}
