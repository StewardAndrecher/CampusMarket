<template>
  <view class="page">
    <!-- 搜索栏 -->
    <view class="search-bar">
      <input class="search-input" v-model="keyword" placeholder="搜索商品" confirm-type="search" @confirm="onSearch" />
    </view>

    <!-- 分类横向滚动 -->
    <scroll-view class="category-bar" scroll-x>
      <view
        v-for="cat in categories"
        :key="cat.id"
        class="cat-item"
        :class="{ active: currentCategory === cat.id }"
        @click="onCategoryChange(cat.id)"
      >
        {{ cat.name }}
      </view>
    </scroll-view>

    <!-- 商品列表 -->
    <view v-if="products.length" class="grid">
      <view v-for="p in products" :key="p.id" class="card" @click="goDetail(p.id)">
        <image v-if="p.coverUrl" class="cover" :src="p.coverUrl" mode="aspectFill" />
        <view v-else class="cover placeholder">暂无图片</view>
        <view class="info">
          <view class="title">{{ p.title }}</view>
          <view class="price-row">
            <text class="price">¥{{ p.price }}</text>
            <text class="condition">{{ conditionText(p.condition) }}</text>
          </view>
          <view class="meta">{{ p.location || '校内' }} · {{ p.viewCount || 0 }} 浏览</view>
        </view>
      </view>
    </view>

    <view v-else-if="!loading" class="empty">还没有商品，去发布第一件吧</view>
    <view v-if="loading" class="loading">加载中...</view>
    <view v-if="noMore && products.length" class="no-more">没有更多了</view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { onShow, onReachBottom } from '@dcloudio/uni-app'
import { getProductList, getCategoryList, Category, ProductVO } from '@/api/product'

const categories = ref<Category[]>([])
const currentCategory = ref<number | null>(null)   // null = 全部
const keyword = ref('')
const products = ref<ProductVO[]>([])
const page = ref(1)
const size = 10
const loading = ref(false)
const noMore = ref(false)

// 首页每次显示都刷新（第2课学的 onShow 套路）
onShow(() => {
  loadCategories()
  refreshList()
})

// 触底加载更多（uni-app 页面生命周期）
onReachBottom(() => {
  if (!loading.value && !noMore.value) {
    loadList()
  }
})

async function loadCategories() {
  const res = await getCategoryList()
  // 最前面插入"全部"
  categories.value = [{ id: 0, name: '全部' }, ...res]
}

async function refreshList() {
  page.value = 1
  noMore.value = false
  products.value = []
  await loadList()
}

async function loadList() {
  loading.value = true
  try {
    const res = await getProductList({
      categoryId: currentCategory.value || undefined,
      keyword: keyword.value || undefined,
      page: page.value,
      size,
    })
    products.value.push(...res.records)
    noMore.value = products.value.length >= res.total
    page.value++
  } finally {
    loading.value = false
  }
}

function onCategoryChange(id: number) {
  currentCategory.value = id === 0 ? null : id
  refreshList()
}

function onSearch() {
  refreshList()
}

function conditionText(c: number) {
  return ['全新', '九成新', '八成新', '七成以下'][c] || '其他'
}

function goDetail(id: number) {
  uni.navigateTo({ url: `/pages/product/detail?id=${id}` })
}
</script>

<style>
.page { background: #f5f6fa; min-height: 100vh; padding-bottom: 40rpx; }
.search-bar { padding: 20rpx; background: #fff; }
.search-input {
  background: #f5f6fa;
  border-radius: 32rpx;
  padding: 16rpx 30rpx;
  font-size: 28rpx;
}
.category-bar { white-space: nowrap; background: #fff; padding: 10rpx 0 20rpx; }
.cat-item {
  display: inline-block;
  padding: 10rpx 30rpx;
  margin-left: 20rpx;
  border-radius: 30rpx;
  background: #f5f6fa;
  font-size: 26rpx;
  color: #5c6470;
}
.cat-item.active { background: #3b82f6; color: #fff; }
.grid { display: flex; flex-wrap: wrap; justify-content: space-between; padding: 20rpx; }
.card {
  width: 48.5%;
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
  margin-bottom: 20rpx;
}
.cover { width: 100%; height: 320rpx; }
.cover.placeholder { display: flex; align-items: center; justify-content: center; color: #aaa; font-size: 26rpx; }
.info { padding: 16rpx; }
.title { font-size: 28rpx; font-weight: 600; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.price-row { display: flex; justify-content: space-between; align-items: center; margin-top: 8rpx; }
.price { color: #ff4d4f; font-size: 32rpx; font-weight: 700; }
.condition { font-size: 22rpx; color: #8a919f; background: #f2f3f5; padding: 4rpx 12rpx; border-radius: 8rpx; }
.meta { font-size: 22rpx; color: #aaa; margin-top: 8rpx; }
.empty, .loading, .no-more { text-align: center; color: #aaa; font-size: 26rpx; padding: 40rpx 0; }
</style>
