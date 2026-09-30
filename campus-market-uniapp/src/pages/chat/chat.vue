<template>
  <view class="page">
    <scroll-view scroll-y class="msg-list" :scroll-top="scrollTop">
      <view v-for="m in messages" :key="m.id" :class="['msg-row', m.senderId === myId ? 'me' : 'other']">
        <view :class="['bubble', m.senderId === myId ? 'me-b' : 'other-b']">{{ m.content }}</view>
      </view>
    </scroll-view>

    <view class="input-bar">
      <input class="input" v-model="text" placeholder="输入消息..." confirm-type="send" @confirm="send" />
      <view class="send-btn" @click="send">发送</view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, nextTick } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { sendMessage, chatHistory, MessageVO } from '@/api/chat'

const otherUserId = ref(0)
const myId = Number(JSON.parse(uni.getStorageSync('campus_user') || '{}').id || 0)
const messages = ref<MessageVO[]>([])
const text = ref('')
const scrollTop = ref(0)

onLoad((opts) => {
  otherUserId.value = Number(opts?.userId)
  uni.setNavigationBarTitle({ title: opts?.nickname || '聊天' })
  loadHistory()
})

async function loadHistory() {
  messages.value = await chatHistory(otherUserId.value)
  await nextTick()
  scrollTop.value = 99999
}

async function send() {
  const content = text.value.trim()
  if (!content) return
  const m = await sendMessage(otherUserId.value, content)
  messages.value.push(m)
  text.value = ''
  await nextTick()
  scrollTop.value = 99999
}
</script>

<style>
.page { display: flex; flex-direction: column; height: 100vh; background: #f5f6fa; }
.msg-list { flex: 1; padding: 20rpx; }
.msg-row { display: flex; margin-bottom: 20rpx; }
.msg-row.me { justify-content: flex-end; }
.bubble {
  max-width: 70%; padding: 18rpx 24rpx; border-radius: 16rpx; font-size: 28rpx;
}
.me-b { background: #95ec69; color: #000; }
.other-b { background: #fff; color: #333; }
.input-bar {
  display: flex; padding: 16rpx; background: #fff;
  border-top: 1rpx solid #eee;
}
.input {
  flex: 1; background: #f5f6fa; border-radius: 30rpx;
  padding: 16rpx 24rpx; font-size: 28rpx;
}
.send-btn {
  margin-left: 16rpx; color: #07c160; font-size: 30rpx;
  padding: 16rpx 24rpx;
}
</style>
