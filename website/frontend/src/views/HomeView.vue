<template>
  <div class="home-page">
    <!-- Hero Section -->
    <section class="hero">
      <div class="hero-content">
        <h1>{{ $t('home.hero.title') }}</h1>
        <p class="subtitle">{{ $t('home.hero.subtitle') }}</p>
        <el-button type="primary" size="large" @click="$router.push('/about')">
          {{ $t('home.hero.cta') }}
        </el-button>
      </div>
    </section>

    <!-- Features Section -->
    <section class="features">
      <h2>{{ $t('home.features.title') }}</h2>
      <div class="features-grid">
        <el-card class="feature-card">
          <el-icon :size="48" color="#409eff"><User /></el-icon>
          <h3>{{ $t('home.features.feature1') }}</h3>
          <p>拥有多年行业经验的专业团队，为您提供优质服务</p>
        </el-card>
        <el-card class="feature-card">
          <el-icon :size="48" color="#67c23a"><CircleCheck /></el-icon>
          <h3>{{ $t('home.features.feature2') }}</h3>
          <p>严格的质量控制体系，确保产品和服务的高品质</p>
        </el-card>
        <el-card class="feature-card">
          <el-icon :size="48" color="#e6a23c"><Service /></el-icon>
          <h3>{{ $t('home.features.feature3') }}</h3>
          <p>以客户为中心，提供全方位的售前售后服务支持</p>
        </el-card>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { User, CircleCheck, Service } from '@element-plus/icons-vue'
</script>

<style scoped>
.home-page {
  width: 100%;
}

.hero {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 120px 20px;
  text-align: center;
}

.hero-content h1 {
  font-size: 48px;
  margin-bottom: 20px;
  font-weight: 600;
}

.subtitle {
  font-size: 24px;
  margin-bottom: 40px;
  opacity: 0.9;
}

.features {
  padding: 80px 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.features h2 {
  text-align: center;
  font-size: 36px;
  margin-bottom: 50px;
  color: #333;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
}

.feature-card {
  text-align: center;
  padding: 40px 20px;
  transition:
    transform 0.3s,
    box-shadow 0.3s;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.feature-card h3 {
  margin: 20px 0 15px;
  font-size: 20px;
  color: #333;
}

.feature-card p {
  color: #666;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .hero-content h1 {
    font-size: 32px;
  }

  .subtitle {
    font-size: 18px;
  }

  .features-grid {
    grid-template-columns: 1fr;
  }
}
</style>
