<script setup>
  import { useRouter } from 'vue-router';
  import { ref, reactive, onMounted } from 'vue';
  import TopButton from '../../components/top_button.vue';
  import customFetch from '../../customFetch'

  const props = defineProps({});
  const router = useRouter();
  const imageUrls = [
          '/src/pages/creator_recipe/pictures/createpic2.png',
          '/src/pages/creator_recipe/pictures/createpic1.png'
        ]

  const group = ref([]);
  const num = ref([]);

  onMounted(async () => {
    try {
      let uid = localStorage.getItem('uid');
      const response = await customFetch(`/fridge/list/${uid}`, {});
      if (response.ok) {
        const data = await response.json();
        group.value = data;
        num.value = new Array(data.length).fill(1);
      } else {
        throw new Error('Failure to obtain data');
      }
    } catch (error) {
      console.error('Failure to obtain data:', error);
    }
  });


  const find = () => {
    const itemsWithNonZeroNum = [];
    num.value.forEach((value, index) => {
      if (value !== 0) {
        itemsWithNonZeroNum.push(group.value[index]);
      }
    });
    console.log('Items with non-zero num:', itemsWithNonZeroNum);
    router.push({
      path: '/recommend',
      query: {fridge: JSON.stringify(itemsWithNonZeroNum)}
    });
  };
</script>

<template>
  <div class="flex-col page">
    <div>
      <TopButton/>
    </div>
    <span class="self-start text">Recipe creator</span>
    <div class="self-stretch section">
      <el-carousel height="11.5rem" motion-blur>
        <el-carousel-item v-for="(imageUrl, index) in imageUrls" :key="index" :autoplay="true">
          <img :src="imageUrl" alt="carousel-image">
        </el-carousel-item>
      </el-carousel>
    </div>
    <div v-for="(item, index) in group" :key="index" class="self-stretch group">
      <div class="flex-row input view">
        <span class="font text_2">{{ item.ingredient.name }}</span>
        <div style="margin: auto;"></div>
        <el-input-number
          style="width: 15rem; align-items: center; margin-right: 0.3rem;"
          v-model="num[index]"
          :min="0"
          :max="item.quantity"
        />
      </div>
    </div>
    <div class="flex-col justify-start items-center self-center text-wrapper_4" @click="find"><span class="text_3">Find</span></div>
  </div>
</template>

<style scoped lang="css">
  .mt-7 {
    margin-top: 0.44rem;
  }
  .page {
    padding: 4.13rem 0.94rem 5.06rem 0.94rem;
    width: 100%;
    overflow-y: auto;
    overflow-x: hidden;
    height: 100%;
  }
  .text {
    margin-left: 0.19rem;
    color: rgb(0, 0, 0);
    font-size: 1.31rem;
    font-family: 'Heiti SC';
    font-weight: 700;
    line-height: 1.31rem;
  }
  .section {
    margin: 4.13rem 0.19rem 0 0.19rem;
    background-color: rgb(255, 255, 255);
    border-radius: 0.19rem;
    height: 11.5rem;
    border-left: 0.063rem;
    border-right: 0.063rem;
    border-top: 0.063rem;
    border-bottom: 0.063rem;
  }
  .group {
    margin-top: 1.56rem;
  }
  .input {
    padding: 1rem 0 1rem;
    background-color: rgb(255, 255, 255);
    border-radius: 0.19rem;
    height: 3.13rem;
    border-left: solid 0.063rem rgb(151, 151, 151);
    border-right: solid 0.063rem rgb(151, 151, 151);
    border-top: solid 0.063rem rgb(151, 151, 151);
    border-bottom: solid 0.063rem rgb(151, 151, 151);
  }
  .view {
    margin-left: 0.13rem;
    margin-right: 0.25rem;
  }
  .font {
    font-size: 1rem;
    font-family: 'Heiti SC';
    line-height: 1rem;
    font-weight: 700;
    color: rgb(0, 0, 0);
  }
  .text_2 {
    margin-left: 0.69rem;
  }
  .text-wrapper_2 {
    margin-right: 0.31rem;
  }
  .text-wrapper_1 {
    margin-right: 0.31rem;
  }
  .text-wrapper_3 {
    margin-right: 0.38rem;
  }
  .text-wrapper_5 {
    margin-right: 0.38rem;
  }
  .text-wrapper_4 {
    margin-top: 4.19rem;
    padding: 1rem 0 0.94rem;
    background-color: rgb(0, 0, 0);
    width: 12.56rem;
  }
  .text_3 {
    color: rgb(255, 255, 255);
    font-size: 0.88rem;
    font-family: 'Heiti SC';
    font-weight: 700;
    line-height: 0.88rem;
  }

  /* 走马灯 */
  .demonstration {
    color: var(--el-text-color-secondary);
  }

  .el-carousel__item h3 {
    color: #475669;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
    text-align: center;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n + 1) {
    background-color: #d3dce6;
  }

  .image-fit {
    width: 100%; 
    height: 100%; 
    object-fit: cover; 
  }

  .el-carousel__item {
    width: 100%;
    height: 100%;
  }

  /* Adjust the size and positioning of the image within the carousel item */
  .el-carousel__item img {
    width: 100%; /* Make the image take the full width of its parent carousel item */
    height: 100%; /* Make the image take the full height of its parent carousel item */
    object-fit: cover; /* Ensure the image covers the entire area of the carousel item */
  }


</style>