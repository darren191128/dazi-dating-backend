<template>
  <section class="hero">
    <div class="hero-content">
      <h1 class="hero-title">若山律师事务所</h1>
      <p class="hero-subtitle">专业法律服务，守护您的权益</p>
      <button class="hero-cta" @click="scrollToContact">立即咨询</button>
    </div>
    <div class="hero-bg"></div>
  </section>
</template>

<script setup lang="ts">
const scrollToContact = () => {
  const contactSection = document.getElementById('contact')
  if (contactSection) {
    contactSection.scrollIntoView({ behavior: 'smooth' })
  }
}
</script>

<style scoped>
.hero {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  background: linear-gradient(135deg, #1a3a5f 0%, #0f2944 100%);
  color: white;
  text-align: center;
}

.hero-content {
  z-index: 2;
  padding: 0 2rem;
}

.hero-title {
  font-size: clamp(2.5rem, 5vw, 4rem);
  font-weight: 700;
  margin-bottom: 1rem;
  animation: fadeInUp 1s ease-out;
}

.hero-subtitle {
  font-size: clamp(1.2rem, 2vw, 1.5rem);
  margin-bottom: 2rem;
  opacity: 0.9;
  animation: fadeInUp 1s ease-out 0.3s both;
}

.hero-cta {
  padding: 1rem 2rem;
  font-size: 1.1rem;
  background: #c9a227;
  color: #1a3a5f;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  animation: fadeInUp 1s ease-out 0.6s both;
}

.hero-cta:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(201, 162, 39, 0.4);
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
