<template>
  <view class="page">
    <view class="form">
      <input class="input" v-model="form.title" placeholder="标题（如：9成新 iPhone 13）" />
      <textarea class="textarea" v-model="form.description" placeholder="描述（成色细节、入手渠道、出售原因...）" />

      <view class="row">
        <view class="label">价格</view>
        <input class="input flex" v-model="form.price" type="digit" placeholder="0.00" />
      </view>
      <view class="row">
        <view class="label">原价(选填)</view>
        <input class="input flex" v-model="form.originalPrice" type="digit" placeholder="0.00" />
      </view>

      <picker :range="categoryNames" @change="onCategoryChange">
        <view class="picker">{{ categoryNames[form.categoryIndex] || '选择分类' }}</view>
      </picker>
      <picker :range="conditions" @change="onConditionChange">
        <view class="picker">{{ conditions[form.conditionIndex] || '选择成色' }}</view>
      </picker>

      <input class="input" v-model="form.location" placeholder="交易地点（如：图书馆门口）" />

      <!-- 第3.5课会换成 MinIO 上传，现在先填图片 URL -->
      <input class="input" v-model="form.imageUrl" placeholder="图片URL（可留空，第3.5课接上传）" />

      <button class="btn" :loading="loading" @click="handleSubmit">发 布</button>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getCategoryList, Category, createProduct } from '@/api/product'

const categories = ref<Category[]>([])
const categoryNames = ref<string[]>([])
const conditions = ['全新', '九成新', '八成新', '七成以下']

const form = ref({
  title: '',
  description: '',
  price: '',
  originalPrice: '',
  categoryIndex: 0,
  conditionIndex: 0,
  location: '',
  imageUrl: '',
})
const loading = ref(false)

// 页面打开时拉一次分类（onLoad 只跑一次，够了）
onLoad(async () => {
  const res = await getCategoryList()
  categories.value = res
  categoryNames.value = res.map((c) => c.name)
})

function onCategoryChange(e: any) {
  form.value.categoryIndex = Number(e.detail.value)
}

function onConditionChange(e: any) {
  form.value.conditionIndex = Number(e.detail.value)
}

async function handleSubmit() {
  if (!form.value.title.trim()) { uni.showToast({ title: '请填写标题', icon: 'none' }); return }
  if (!form.value.price || Number(form.value.price) <= 0) { uni.showToast({ title: '请填写正确价格', icon: 'none' }); return }
  if (!categories.value.length) { uni.showToast({ title: '分类加载失败', icon: 'none' }); return }

  loading.value = true
  try {
    await createProduct({
      title: form.value.title.trim(),
      description: form.value.description.trim(),
      price: Number(form.value.price),
      originalPrice: form.value.originalPrice ? Number(form.value.originalPrice) : undefined,
      categoryId: categories.value[form.value.categoryIndex].id,
      condition: form.value.conditionIndex,
      location: form.value.location.trim(),
      images: form.value.imageUrl.trim() ? [form.value.imageUrl.trim()] : [],
    })
    uni.showToast({ title: '发布成功', icon: 'success' })
    // 回首页（首页 onShow 会自动刷新看到新商品）
    setTimeout(() => uni.switchTab({ url: '/pages/index/index' }), 600)
  } catch (e) {
    // 401 已在 request.ts 处理跳登录；其他错误已弹 toast
  } finally {
    loading.value = false
  }
}
</script>

<style>
.page { background: #f5f6fa; min-height: 100vh; padding: 20rpx; }
.form { background: #fff; border-radius: 16rpx; padding: 30rpx; }
.input {
  border: 1rpx solid #e5e6eb;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
  margin-bottom: 20rpx;
}
.textarea {
  border: 1rpx solid #e5e6eb;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
  height: 160rpx;
  width: 100%;
  box-sizing: border-box;
  margin-bottom: 20rpx;
}
.row { display: flex; align-items: center; }
.label { width: 160rpx; font-size: 28rpx; color: #5c6470; }
.flex { flex: 1; }
.picker {
  border: 1rpx solid #e5e6eb;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
  margin-bottom: 20rpx;
  color: #1f2329;
}
.btn {
  background: #3b82f6;
  color: #fff;
  border-radius: 12rpx;
  font-size: 32rpx;
  margin-top: 20rpx;
}
</style>
