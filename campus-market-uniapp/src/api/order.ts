import { request } from '@/utils/request'

export interface OrderVO {
  id: number
  orderNo: string
  productId: number
  productTitle: string
  productCover: string
  price: number
  buyerId: number
  sellerId: number
  sellerNickname?: string
  buyerNickname?: string
  status: number
  statusText: string
  tradeLocation?: string
  remark?: string
  createTime?: string
}

export const createOrder = (productId: number, tradeLocation: string, remark: string) =>
  request<OrderVO>({ url: '/order/create', method: 'POST', data: { productId, tradeLocation, remark } })

export const payOrder = (id: number) =>
  request<void>({ url: `/order/${id}/pay`, method: 'POST' })

export const confirmOrder = (id: number) =>
  request<void>({ url: `/order/${id}/confirm`, method: 'POST' })

export const cancelOrder = (id: number) =>
  request<void>({ url: `/order/${id}/cancel`, method: 'POST' })

export const myBuyOrders = () =>
  request<{ records: OrderVO[] }>({ url: '/order/buy' })

export const mySellOrders = () =>
  request<{ records: OrderVO[] }>({ url: '/order/sell' })
