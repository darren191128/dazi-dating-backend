<template>
  <footer class="bg-slate-900 text-white">
    <div class="container mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div class="grid grid-cols-1 md:grid-cols-4 gap-8">
        <!-- Logo & Description -->
        <div class="col-span-1 md:col-span-1">
          <div class="flex items-center space-x-2 mb-4">
            <div class="w-8 h-8 bg-gradient-to-br from-blue-500 to-cyan-400 rounded-lg flex items-center justify-center">
              <i class="fas fa-cloud text-white text-sm"></i>
            </div>
            <span class="font-bold text-lg">启云科技</span>
          </div>
          <p class="text-white/60 text-sm mb-4">专业的软件定制开发服务商，让数字化更简单</p>
          <div class="flex space-x-4">
            <a href="#" class="text-white/60 hover:text-white transition-colors">
              <i class="fab fa-weixin text-xl"></i>
            </a>
            <a href="#" class="text-white/60 hover:text-white transition-colors">
              <i class="fab fa-weibo text-xl"></i>
            </a>
          </div>
        </div>

        <!-- Quick Links -->
        <div>
          <h3 class="font-semibold mb-4">快速链接</h3>
          <ul class="space-y-2 text-sm text-white/60">
            <li><RouterLink to="/about" class="hover:text-white transition-colors">关于我们</RouterLink></li>
            <li><RouterLink to="/services" class="hover:text-white transition-colors">服务介绍</RouterLink></li>
            <li><RouterLink to="/cases" class="hover:text-white transition-colors">案例展示</RouterLink></li>
            <li><RouterLink to="/team" class="hover:text-white transition-colors">团队介绍</RouterLink></li>
          </ul>
        </div>

        <!-- Services -->
        <div>
          <h3 class="font-semibold mb-4">服务项目</h3>
          <ul class="space-y-2 text-sm text-white/60">
            <li><RouterLink to="/services" class="hover:text-white transition-colors">小程序开发</RouterLink></li>
            <li><RouterLink to="/services" class="hover:text-white transition-colors">APP开发</RouterLink></li>
            <li><RouterLink to="/services" class="hover:text-white transition-colors">网站开发</RouterLink></li>
            <li><RouterLink to="/contact" class="hover:text-white transition-colors">定制开发</RouterLink></li>
          </ul>
        </div>

        <!-- Contact -->
        <div>
          <h3 class="font-semibold mb-4">联系我们</h3>
          <ul class="space-y-2 text-sm text-white/60">
            <li class="flex items-center gap-2">
              <i class="fas fa-map-marker-alt w-4"></i>
              重庆市xxx区xxx路xxx号
            </li>
            <li class="flex items-center gap-2">
              <i class="fas fa-phone w-4"></i>
              400-xxx-xxxx
            </li>
            <li class="flex items-center gap-2">
              <i class="fas fa-envelope w-4"></i>
              hello@qiyun.com
            </li>
          </ul>
        </div>
      </div>

      <div class="border-t border-white/10 mt-12 pt-8 text-center text-sm text-white/40">
        <p>&copy; 2024 重庆启云网络科技有限公司 版权所有</p>
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts">
</script>