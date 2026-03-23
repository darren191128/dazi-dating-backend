<template>
  <div class="min-h-screen">
    <Header />
    <main class="pt-16">
      <HeroSection />
      <ServicesSection />
      <TeamSection />
      <CasesSection />
      <NewsSection />
    </main>
    <Footer />
  </div>
</template>

<script setup lang="ts">
import Header from '../components/Header.vue'
import Footer from '../components/Footer.vue'
import HeroSection from '../components/HeroSection.vue'
import ServicesSection from '../components/ServicesSection.vue'
import TeamSection from '../components/TeamSection.vue'
import CasesSection from '../components/CasesSection.vue'
import NewsSection from '../components/NewsSection.vue'
</script>
