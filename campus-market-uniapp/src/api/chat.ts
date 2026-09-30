import { request } from '@/utils/request'

export interface MessageVO {
  id: number
  conversationId: number
  senderId: number
  content: string
  createTime?: string
}

export interface ConversationVO {
  conversationId: number
  otherUserId: number
  otherNickname: string
  otherAvatar?: string
  lastMessage: string
  updateTime?: string
}

export const sendMessage = (toUserId: number, content: string, productId?: number) =>
  request<MessageVO>({ url: '/chat/send', method: 'POST', data: { toUserId, content, productId } })

export const myConversations = () =>
  request<ConversationVO[]>({ url: '/chat/conversations' })

export const chatHistory = (otherUserId: number) =>
  request<MessageVO[]>({ url: `/chat/history/${otherUserId}` })
