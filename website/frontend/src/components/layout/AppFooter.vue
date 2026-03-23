<template>
  <footer class="app-footer">
    <div class="footer-container">
      <div class="footer-content">
        <div class="footer-section">
          <h4>{{ $t('nav.about') }}</h4>
          <p>公司简介和相关信息</p>
        </div>
        <div class="footer-section">
          <h4>{{ $t('nav.contact') }}</h4>
          <p>联系方式和地址</p>
        </div>
        <div class="footer-section">
          <h4>{{ $t('footer.terms') }}</h4>
          <p>服务条款和隐私政策</p>
        </div>
      </div>
      <div class="footer-bottom">
        <p>{{ $t('footer.copyright') }}</p>
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts"></script>

<style scoped>
.app-footer {
  background: #2c3e50;
  color: #fff;
  padding: 40px 0 20px;
  margin-top: auto;
}

.footer-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 40px;
  margin-bottom: 30px;
}

.footer-section h4 {
  font-size: 16px;
  margin-bottom: 15px;
  color: #fff;
}

.footer-section p {
  color: #bdc3c7;
  font-size: 14px;
  line-height: 1.6;
}

.footer-bottom {
  border-top: 1px solid #34495e;
  padding-top: 20px;
  text-align: center;
}

.footer-bottom p {
  color: #95a5a6;
  font-size: 13px;
}

@media (max-width: 768px) {
  .footer-content {
    grid-template-columns: 1fr;
    gap: 20px;
  }
}
</style>
