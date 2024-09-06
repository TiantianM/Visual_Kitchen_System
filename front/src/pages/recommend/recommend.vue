<script setup>
  import { useRouter } from 'vue-router';
  import { ref, reactive, onMounted } from 'vue';
  import TopButton from '../../components/top_button.vue';
  import { ElLoading, ElMessageBox, ElMessage } from 'element-plus';
  import customFetch from '../../customFetch'

  const props = defineProps({});
  const data = reactive({});
  const router = useRouter();
  const recipes = ref([]);
  let row_data = ref('');
  onMounted(async () => {
    await fetchRecipes();
  });

  const fetchRecipes = async () => {
    // let fridge = router.currentRoute.value.query.fridge;
    let fridge = localStorage.getItem('fridge_result');
    console.log(fridge);
    try {
      const loadingInstance = ElLoading.service({
        fullscreen: true
      });
      const response = await customFetch('/recipe/recommend', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: fridge
      });
      const data = await response.json();
      
      loadingInstance.close();

      row_data.value =  data;
      console.log('Recipes fetched:', recipes.value);
    } catch (error) {
      console.error('Error fetching recipes:', error);
    }
  };

  const goToRecipeInfo = (row_data) => {
    // console.log(row_data);
    router.push({ name: 'recipe_info', query: { row_data: JSON.stringify(row_data) } });
  };
</script>

<template>
  <div class="flex-col page">
    <div>
      <TopButton/>
    </div>
    <span class="self-start text">Recommended for you</span>
    <div class="mt-90 flex-col self-stretch">
      <div class="grid">
        <div class="flex-col grid-item" v-for="(data, index) in row_data" :key="data.recipe.id" @click="goToRecipeInfo(data)">
          <div class="self-stretch section">
            <img class="image" :src="data.recipe.img">
          </div>
          <div class="flex-col justify-between" style="height: 5rem;">
            <span class="mt-10 self-start font">{{ data.recipe.title }}</span>
            <span class="mt-10 self-start font_2">ReadyIn in {{ data.recipe.readyIn }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="css">
  .page {
    padding: 4.13rem 1.13rem 9.81rem;
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
  .grid {
    /* height: 31.25rem; */
    display: grid;
    /* grid-template-rows: repeat(3, minmax(0, 1fr)); */
    grid-template-columns: repeat(auto-fit, minmax(150px, 200px));
    row-gap: 0.69rem;
    column-gap: 1.31rem;
  }
  .grid-item {
    padding: 0.75rem;
    background-color: rgb(255, 255, 255);
    border-radius: 0.38rem;
    /* height: 10rem; */
  }
  .section {
    background-color: rgb(216, 216, 216);
    height: 5.94rem;
    border-left: solid 0.063rem rgb(151, 151, 151);
    border-right: solid 0.063rem rgb(151, 151, 151);
    border-top: solid 0.063rem rgb(151, 151, 151);
    border-bottom: solid 0.063rem rgb(151, 151, 151);
  }
  .font {
    font-size: 0.69rem;
    font-family: 'Heiti SC';
    line-height: 0.69rem;
    font-weight: 700;
    color: rgb(0, 0, 0);
  }
  .font_2 {
    font-size: 0.69rem;
    font-family: 'Heiti SC';
    line-height: 0.69rem;
    font-weight: 700;
    color: rgb(151, 151, 151);
  }
  .group {
    margin-top: -0.25rem;
  }
  .font_3 {
    font-size: 0.88rem;
    font-family: 'Heiti SC';
    line-height: 0.88rem;
    font-weight: 700;
    color: rgb(255, 255, 255);
  }
  .text_2 {
    margin-left: 6.38rem;
  }
  .text_3 {
    margin-right: 6.19rem;
  }

  .image {
    width: 100%;
    height: 100%;
  }
</style>