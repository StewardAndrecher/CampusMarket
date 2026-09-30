<template>
  <view class="page">
    <view v-if="list.length === 0" class="empty">还没有会话，去商品详情页联系卖家吧</view>
    <view v-for="c in list" :key="c.conversationId" class="item" @click="goChat(c)">
      <view class="avatar">{{ c.otherNickname?.charAt(0) || '?' }}</view>
      <view class="info">
        <view class="name">{{ c.otherNickname }}</view>
        <view class="last">{{ c.lastMessage }}</view>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { myConversations, ConversationVO } from '@/api/chat'

const list = ref<ConversationVO[]>([])

onShow(async () => {
  list.value = await myConversations()
})

function goChat(c: ConversationVO) {
  uni.navigateTo({ url: `/pages/chat/chat?userId=${c.otherUserId}&nickname=${c.otherNickname}` })
}
</script>

<style>
.page { background: #f5f6fa; min-height: 100vh; }
.empty { text-align: center; color: #999; margin-top: 200rpx; }
.item {
  background: #fff; margin: 10rpx 20rpx; padding: 24rpx;
  border-radius: 16rpx; display: flex; align-items: center;
}
.avatar {
  width: 80rpx; height: 80rpx; border-radius: 50%;
  background: #3b82f6; color: #fff;
  display: flex; align-items: center; justify-content: center;
  font-size: 32rpx; margin-right: 20rpx;
}
.info { flex: 1; }
.name { font-size: 30rpx; font-weight: 600; }
.last { font-size: 24rpx; color: #999; margin-top: 8rpx; }
</style>
