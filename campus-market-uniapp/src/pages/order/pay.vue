<template>
  <view class="page">
    <view class="status-icon">✓</view>
    <view class="status-text">订单已提交</view>
    <view class="order-no">订单号：{{ orderNo }}</view>
    <view class="amount">¥{{ price }}</view>

    <view class="actions">
      <button v-if="status === 0" class="primary" @click="pay">模拟支付</button>
      <button v-if="status === 1" class="primary" @click="confirm">确认收货</button>
      <button class="ghost" @click="goList">查看我的订单</button>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { payOrder, confirmOrder } from '@/api/order'

const orderId = ref(0)
const orderNo = ref('')
const price = ref(0)
const status = ref(0)

onLoad((opts) => {
  orderId.value = Number(opts?.id)
  orderNo.value = opts?.no || ''
  price.value = Number(opts?.price)
})

async function pay() {
  await payOrder(orderId.value)
  status.value = 1
  uni.showToast({ title: '支付成功', icon: 'success' })
}

async function confirm() {
  await confirmOrder(orderId.value)
  status.value = 2
  uni.showToast({ title: '交易完成', icon: 'success' })
}

function goList() {
  uni.redirectTo({ url: '/pages/order/list' })
}
</script>

<style>
.page { background: #fff; min-height: 100vh; padding: 100rpx 40rpx; text-align: center; }
.status-icon {
  width: 120rpx; height: 120rpx; border-radius: 50%;
  background: #07c160; color: #fff; font-size: 60rpx;
  line-height: 120rpx; margin: 0 auto;
}
.status-text { font-size: 36rpx; font-weight: 600; margin-top: 30rpx; }
.order-no { color: #999; font-size: 24rpx; margin-top: 16rpx; }
.amount { color: #ff4d4f; font-size: 56rpx; font-weight: 700; margin-top: 40rpx; }
.actions { margin-top: 80rpx; }
.primary {
  background: #ff4d4f; color: #fff; border-radius: 40rpx;
  font-size: 30rpx; margin-bottom: 20rpx;
}
.ghost { background: #f5f6fa; color: #333; border-radius: 40rpx; font-size: 28rpx; }
</style>
