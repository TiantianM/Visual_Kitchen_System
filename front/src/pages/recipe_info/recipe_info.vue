<script setup>
import { useRouter } from 'vue-router';
import { onMounted, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import TopButton from '../../components/top_button.vue';
import customFetch from '../../customFetch'

const router = useRouter();

// 定义响应式对象来保存界面数据
const recipeInfo = reactive({
  id: null,
  title: '',
  readyIn: '',
  img: '',
  directions: [],
  ingredients: [],
});

onMounted(() => {
  let row_data = router.currentRoute.value.query.row_data;
  if (typeof row_data === 'string') {
    try {
      row_data = JSON.parse(row_data);
      let recipe = row_data.recipe;
      recipeInfo.id = recipe.id;
      recipeInfo.title = recipe.title;
      recipeInfo.readyIn = recipe.readyIn;
      recipeInfo.img = recipe.img;
      recipeInfo.directions = recipe.directions.split('&&');
      recipeInfo.ingredients = recipe.ingredients.split('&&');
    } catch (error) {
      console.error('Error parsing recipe:', error);
    }
  }
});

const handleLike = async () => {
  try {
    const response = await customFetch('/preference_history/save', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        uid: localStorage.getItem('uid'),
        rid: recipeInfo.id,
        likeCount: 1,
        dislikeCount: 0
      })
    });
    const data = await response.json();
    console.log('Response from server:', data);
    ElMessage.success('Success!');
  } catch (error) {
    console.error('Error while sending like:', error);
    ElMessage.error('Please try it later!');
  }
};


const handleDisLike = async () => {
  try {
    const response = await customFetch('/preference_history/save', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        uid: localStorage.getItem('uid'),
        rid: recipeInfo.id,
        likeCount: 0,
        dislikeCount: 1
      })
    });
    const data = await response.json();
    console.log('Response from server:', data);
    ElMessage.success('Success!');
  } catch (error) {
    console.error('Error while sending like:', error);
    ElMessage.error('Please try it later!');
  }
};
</script>

<template>
  <div class="flex-col page">
    <div>
      <TopButton/>
    </div>
    <span class="self-start text">{{ recipeInfo.title }}</span>
    <div class="self-stretch section">
      <img class="image" :src="recipeInfo.img" />
    </div>
    <div class="flex-col self-stretch group">
      <span class="self-start font" style="font-size: 0.9rem;">The estimated cooking time：{{ recipeInfo.readyIn }}</span>
      <div class="flex-col justify-start self-stretch relative group_2 view">
        <div class="flex-col justify-start items-start section_2">
          <div class="flex-row group_3">
            <div v-for="(ingredient, index) in recipeInfo.ingredients" :key="index" class="group_4">
              <div class="flex-row" style="width: 100%">
                <div class="flex-col justify-start items-center text-wrapper_2"><span class="font">{{ ingredient }}</span></div>
              </div>
            </div>
          </div>
        </div>
        <div class="flex-col justify-start items-center text-wrapper pos"><span class="font_2">Ingredients</span></div>
      </div>
      <span class="self-start font text_2" style="font-size: 0.9rem;">Directions:</span>
      <div class="flex-col self-stretch group_5">
        <div v-for="(direction, index) in recipeInfo.directions" :key="index" class="flex-col justify-start relative group_2">
          <div class="flex-col justify-start items-start text-wrapper_5">
            <span class="font text_4">{{ direction }}</span>
          </div>
          <div class="flex-col justify-start items-start text-wrapper pos_2">
            <span class="font_2 text_5">Step {{ index+1 }}</span>
          </div>
        </div>
      </div>
      <div class="flex-row justify-center self-stretch group_6">
        <div class="flex-col justify-start items-center button" @click="handleLike()"><span class="font_2">Like</span></div>
        <div class="ml-12 flex-col justify-start items-center button_2" @click="handleDisLike()"><span class="font_2">DisLike</span></div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="css">
  .ml-9 {
    margin-left: 0.56rem;
  }
  .mt-9 {
    margin-top: 0.56rem;
  }
  .page {
    padding: 4.25rem 1.13rem 0 1.13rem;
    width: 100%;
    overflow-y: auto;
    overflow-x: hidden;
    height: 100%;
  }
  .text {
    color: rgb(0, 0, 0);
    font-size: 1.31rem;
    font-family: 'Heiti SC';
    font-weight: 700;
    line-height: 1.31rem;
  }
  .section {
    margin-top: 4.25rem;
    background-color: rgb(255, 255, 255);
    border-radius: 0.19rem;
    /* width: 100%;
    height: 95%; */
  }
  .group {
    padding: 1.38rem 0 1.88rem;
  }
  .font {
    font-size: 0.69rem;
    font-family: 'Heiti SC';
    line-height: 0.69rem;
    font-weight: 700;
    color: rgb(0, 0, 0);
  }
  .group_2 {
    padding-top: 0.75rem;
  }
  .view {
    margin-top: 0.69rem;
  }
  .section_2 {
    padding: 1.31rem 0 0.63rem;
    background-color: rgb(255, 255, 255);
    border-radius: 0.19rem;
  }
  .group_3 {
    width: 100%;
    margin-left: 0.63rem;
    flex-wrap: wrap;
  }
  .grid {
    width: 100%;
    margin-top: 0.1rem;
    /* height: 31.25rem; */
    display: grid;
    /* grid-template-rows: repeat(auto-fit, minmax(70px, 1fr)); */
    grid-template-columns: repeat(auto-fit, minmax(auto-fit, auto-fit));
    row-gap: 0.3rem;
    column-gap: 0.1rem;
  }
  .group_4 {
    margin-right: 0.63rem;
    padding-bottom: 0.63rem;
  }
  .text-wrapper_2 {
    padding: 0.37rem;
    background-color: rgb(217, 217, 217);
    border-radius: 0.19rem;
    /* width: 5rem; */
    height: 1.5rem;
  }
  .text-wrapper_3 {
    padding: 0.44rem 0 0.38rem;
    background-color: rgb(217, 217, 217);
    border-radius: 0.19rem;
    width: 5rem;
  }
  .text-wrapper {
    padding: 0.38rem 0 0.44rem;
    background-color: rgb(111, 182, 131);
    border-radius: 0.38rem 0.38rem 0rem 0.38rem;
    width: 5rem;
  }
  .pos {
    position: absolute;
    left: 0;
    top: 0;
  }
  .font_2 {
    font-size: 0.69rem;
    font-family: 'Heiti SC';
    line-height: 0.69rem;
    font-weight: 700;
    color: rgb(255, 255, 255);
  }
  .text_2 {
    margin-top: 1.06rem;
  }
  .group_5 {
    margin-top: 0.75rem;
  }
  .text-wrapper_5 {
    padding: 1.13rem 0 0.69rem;
    background-color: rgb(255, 255, 255);
    border-radius: 0.19rem;
  }
  .text_4 {
    margin-left: 0.69rem;
  }
  .text-wrapper_4 {
    position: absolute;
    left: 0;
    top: 0;
  }
  .text_3 {
    margin-left: 0.63rem;
  }
  .pos_2 {
    position: absolute;
    left: 0;
    top: 0;
  }
  .text_5 {
    margin-left: 0.63rem;
  }
  .pos_3 {
    position: absolute;
    left: 0;
    top: 0;
  }
  .text_6 {
    margin-left: 0.63rem;
  }
  .group_6 {
    margin-top: 1.31rem;
  }
  .button {
    padding: 0.5rem 0 0.5rem;
    background-color: rgb(111, 182, 131);
    border-radius: 0.38rem;
    width: 3.69rem;
    height: 1.69rem;
  }
  .button_2 {
    padding: 0.5rem 0 0.5rem;
    background-color: rgb(0, 0, 0);
    border-radius: 0.38rem;
    width: 3.69rem;
    height: 1.69rem;
  }

  .image {
    object-fit: contain;
    /* max-height: 80%; */
    width: 100%;
    height: 100%;
  }
</style>