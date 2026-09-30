<template>
  <view class="page">
    <view class="card">
      <view class="row" v-if="product.id">
        <image class="cover" :src="product.images?.[0] || '/static/logo.png'" mode="aspectFill" />
        <view class="info">
          <view class="title">{{ product.title }}</view>
          <view class="price">¥{{ product.price }}</view>
        </view>
      </view>
    </view>

    <view class="card">
      <view class="label">交易地点</view>
      <input class="input" v-model="tradeLocation" placeholder="如：图书馆门口" />
      <view class="label">备注（可选）</view>
      <input class="input" v-model="remark" placeholder="想和卖家说的话" />
    </view>

    <view class="bottom-bar">
      <view class="total">合计：<text class="price">¥{{ product.price }}</text></view>
      <button class="btn" @click="submit">提交订单</button>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getProductDetail } from '@/api/product'
import { createOrder } from '@/api/order'

const productId = ref(0)
const product = ref<any>({})
const tradeLocation = ref('图书馆门口')
const remark = ref('')

onLoad((opts) => {
  productId.value = Number(opts?.id)
  loadDetail()
})

async function loadDetail() {
  product.value = await getProductDetail(productId.value)
}

async function submit() {
  if (!tradeLocation.value.trim()) {
    uni.showToast({ title: '请填写交易地点', icon: 'none' })
    return
  }
  const order = await createOrder(productId.value, tradeLocation.value.trim(), remark.value.trim())
  uni.showToast({ title: '下单成功', icon: 'success' })
  setTimeout(() => {
    uni.redirectTo({ url: `/pages/order/pay?id=${order.id}&no=${order.orderNo}&price=${order.price}` })
  }, 600)
}
</script>

<style>
.page { background: #f5f6fa; min-height: 100vh; padding-bottom: 180rpx; }
.card { background: #fff; margin: 20rpx; padding: 24rpx; border-radius: 16rpx; }
.row { display: flex; }
.cover { width: 140rpx; height: 140rpx; border-radius: 12rpx; }
.info { margin-left: 20rpx; flex: 1; }
.title { font-size: 28rpx; }
.price { color: #ff4d4f; font-size: 36rpx; font-weight: 700; margin-top: 10rpx; }
.label { font-size: 26rpx; color: #5c6470; margin: 20rpx 0 10rpx; }
.input { background: #f5f6fa; border-radius: 10rpx; padding: 20rpx; font-size: 28rpx; }
.bottom-bar {
  position: fixed; bottom: 0; left: 0; right: 0;
  background: #fff; padding: 20rpx 30rpx;
  display: flex; align-items: center;
  border-top: 1rpx solid #eee;
}
.total { flex: 1; font-size: 28rpx; }
.btn {
  background: #ff4d4f; color: #fff; border-radius: 40rpx;
  font-size: 30rpx; padding: 0 60rpx; height: 80rpx; line-height: 80rpx;
}
</style>
