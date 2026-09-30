<template>
  <view class="page">
    <swiper v-if="product.images?.length" class="swiper" indicator-dots circular>
      <swiper-item v-for="(img, i) in product.images" :key="i">
        <image class="swiper-img" :src="img" mode="aspectFill" />
      </swiper-item>
    </swiper>
    <view v-else class="no-img">暂无图片</view>

    <view class="body">
      <view class="price-row">
        <text class="price">¥{{ product.price }}</text>
        <text v-if="product.originalPrice" class="original">¥{{ product.originalPrice }}</text>
      </view>
      <view class="title">{{ product.title }}</view>
      <view class="tags">
        <text class="tag">{{ conditionText(product.condition) }}</text>
        <text class="tag">{{ product.location || '校内' }}</text>
      </view>
      <view class="divider" />
      <view class="desc-title">商品描述</view>
      <view class="desc">{{ product.description || '卖家很懒，什么都没写' }}</view>
    </view>

    <view class="seller" @click="contactSeller">
      <view class="avatar">{{ product.sellerNickname?.charAt(0) || '?' }}</view>
      <view class="seller-name">{{ product.sellerNickname || '未知卖家' }}</view>
      <view class="chat-btn">聊一聊</view>
    </view>

    <view class="bottom-bar">
      <view class="icon-btn" @click="onFavorite">
        <text class="icon">{{ favorited ? '★' : '☆' }}</text>
        <text class="icon-label">收藏</text>
      </view>
      <view class="buy-btn" @click="buy">立即购买</view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getProductDetail, ProductVO } from '@/api/product'
import { toggleFavorite } from '@/api/favorite'

const product = ref<ProductVO>({} as ProductVO)
const productId = ref(0)
const favorited = ref(false)

onLoad((options) => {
  productId.value = Number(options?.id)
  if (productId.value) loadDetail(productId.value)
})

async function loadDetail(id: number) {
  product.value = await getProductDetail(id)
}

function conditionText(c: number) {
  return ['全新', '九成新', '八成新', '七成以下'][c] || '其他'
}

async function onFavorite() {
  const r = await toggleFavorite(productId.value)
  favorited.value = r.favorited
  uni.showToast({ title: r.favorited ? '已收藏' : '已取消', icon: 'none' })
}

function buy() {
  uni.navigateTo({ url: `/pages/order/confirm?id=${productId.value}` })
}

function contactSeller() {
  if (!product.value.sellerId) return
  uni.navigateTo({
    url: `/pages/chat/chat?userId=${product.value.sellerId}&nickname=${product.value.sellerNickname}`,
  })
}
</script>

<style>
.page { background: #f5f6fa; min-height: 100vh; padding-bottom: 140rpx; }
.swiper { width: 100%; height: 750rpx; }
.swiper-img { width: 100%; height: 750rpx; }
.no-img { width: 100%; height: 750rpx; display: flex; align-items: center; justify-content: center; color: #aaa; background: #eee; }
.body { background: #fff; padding: 30rpx; border-radius: 16rpx; margin: -20rpx 20rpx 0; }
.price-row { display: flex; align-items: baseline; }
.price { color: #ff4d4f; font-size: 44rpx; font-weight: 700; }
.original { color: #aaa; font-size: 26rpx; text-decoration: line-through; margin-left: 16rpx; }
.title { font-size: 34rpx; font-weight: 600; margin-top: 16rpx; }
.tags { display: flex; gap: 16rpx; margin-top: 20rpx; }
.tag { font-size: 22rpx; color: #5c6470; background: #f2f3f5; padding: 6rpx 16rpx; border-radius: 8rpx; }
.divider { height: 1rpx; background: #f0f1f3; margin: 30rpx 0; }
.desc-title { font-size: 28rpx; font-weight: 600; }
.desc { font-size: 28rpx; color: #5c6470; margin-top: 12rpx; line-height: 1.6; }
.seller { background: #fff; margin: 20rpx; padding: 30rpx; border-radius: 16rpx; display: flex; align-items: center; }
.avatar { width: 80rpx; height: 80rpx; border-radius: 50%; background: #3b82f6; color: #fff; display: flex; align-items: center; justify-content: center; font-size: 32rpx; margin-right: 20rpx; }
.seller-name { font-size: 30rpx; font-weight: 600; flex: 1; }
.chat-btn { font-size: 26rpx; color: #3b82f6; }
.bottom-bar {
  position: fixed; bottom: 0; left: 0; right: 0;
  background: #fff; padding: 16rpx 30rpx;
  display: flex; align-items: center; border-top: 1rpx solid #eee;
}
.icon-btn { display: flex; flex-direction: column; align-items: center; margin-right: 40rpx; }
.icon { font-size: 40rpx; color: #ff4d4f; }
.icon-label { font-size: 22rpx; color: #666; }
.buy-btn {
  flex: 1; background: #ff4d4f; color: #fff; border-radius: 40rpx;
  text-align: center; line-height: 80rpx; font-size: 32rpx; height: 80rpx;
}
</style>
