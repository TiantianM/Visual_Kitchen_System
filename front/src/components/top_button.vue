<script setup>
import { useRouter } from 'vue-router';
import { House, Back } from '@element-plus/icons-vue'

const router = useRouter();

const goToMainPage = () => {
  router.push('/index');
};
const goToBeforePage = () => {
  // router.go(-1);
  router.back();
};
</script>
<template>
  <el-button class="home" type="primary" color="#000000" @click="goToBeforePage">
    <el-icon>
      <Back />
    </el-icon>
  </el-button>
  <el-button class="home" type="primary" color="#000000" @click="goToMainPage">
    <el-icon>
      <House />
    </el-icon>
  </el-button>
</template>
<style scoped lang="css">
.home {
  border-radius: 0.38rem;
  margin-bottom: 1rem;
}
</style>