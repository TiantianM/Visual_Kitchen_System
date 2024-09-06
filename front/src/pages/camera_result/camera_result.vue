<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import TopButton from '../../components/top_button.vue';
import GridIngredient from '../../components/grid_ingredient.vue'
import { useRoute, useRouter } from 'vue-router';
import { carouselItemProps } from 'element-plus';
import customFetch from '../../customFetch'

let labels = [];
const route = useRoute();
const router = useRouter();
const items = ref([]);

onMounted(async () => {
  const cameraResult = sessionStorage.getItem('camera_result');
  if (cameraResult && cameraResult!='[object Object]') {
    items.value = JSON.parse(cameraResult);
    console.log("cameraResult:", items.value);
    sessionStorage.removeItem('camera_result');
  } else {
    labels = route.query.labels || [];
    fetchData(labels);
  }
});

const fetchData = async (labels) => {
  try {
    const response = await customFetch('/ingredient/search_list', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(labels)
    });
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    const data = await response.json();
    items.value = wrapData(data);
    console.log('items.value:', items.value);
  } catch (error) {
    console.error('Error fetching data:', error);
  }
};

const upload = (labels) => {
  let uid = localStorage.getItem('uid');
  items.value.forEach(item => {
    item.uid = uid;
    item.iid = item.ingredient.id;
    item.quantity = item.quantity || 1;
    item.expirationDate = item.expirationDate || "2024-04-29T16:00:00.000Z";
  });
  // console.log(items.value);
  customFetch('/fridge/saveAll', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(items.value)
  })
  .then(response => response.json())
  .then(data => {
    console.log('Items saved:', data);
    ElMessage.success('Ingredients saved successfully');
    router.push({ name: 'index' });
  })
  .catch(error => {
    console.error('Error saving items:', error);
    ElMessage.error('Failed to save Ingredients');
  });
};

const wrapData = (data) => {
    return data.map(item => {
        return { ingredient: item };
    });
};

const handleDelete = (index) => {
  items.value.splice(index, 1)
};

const handleEdit = (index) => {
  const itemToEdit = items.value[index];
  // console.log(itemToEdit);
  // console.log(index);
  localStorage.setItem('edit_type', 'camera');
  sessionStorage.setItem('camera_result', JSON.stringify(items.value));
  router.push({
    name: 'edit_ingredient',
      query: { index }
    })
};

</script>

<template>
  <div class="flex-col page">
    <div>
      <TopButton/>
    </div>
    <GridIngredient :items="items" :onDelete="handleDelete" :onEdit="handleEdit" />
    <div class="flex-row justify-center items-center self-center section_2" style="width: 11rem;">
      <div class="flex-col justify-start items-center button" @click="upload"><span class="font_4">Upload</span></div>
    </div>
  </div>
</template>

<style scoped lang="css">
.ml-11 {
  margin-left: 0.69rem;
}

.page {
  padding: 4.13rem 1.13rem 5.5rem 1.13rem;
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

.group {
  margin-top: 2.25rem;
}

.text-wrapper {
  padding: 0.44rem 0 0.44rem;
  background-color: rgb(0, 0, 0);
  border-radius: 0.38rem;
  width: 2.81rem;
  height: 1.56rem;
}

.font {
  font-size: 0.69rem;
  font-family: 'Heiti SC';
  line-height: 0.69rem;
  font-weight: 700;
  color: rgb(0, 0, 0);
}

.text_2 {
  color: rgb(255, 255, 255);
}

.text-wrapper_2 {
  padding: 0.44rem 0 0.44rem;
  background-color: rgb(255, 255, 255);
  border-radius: 0.38rem;
  height: 1.56rem;
}

.text_3 {
  margin-left: 0.25rem;
  margin-right: 0.31rem;
}

.grid {
  margin-top: 1.81rem;
  /* height: 31.25rem; */
  display: grid;
  /* grid-template-rows: repeat(auto-fit, minmax(70px, 1fr)); */
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  row-gap: 0.69rem;
  column-gap: 1.31rem;
}

.grid-item {
  padding: 0.69rem 0.69rem 0.69rem 0.75rem;
  background-color: rgb(255, 255, 255);
  border-radius: 0.38rem;
  height: 10rem;
}

.section {
  background-color: rgb(216, 216, 216);
  height: 5.94rem;
  border-left: solid 0.063rem rgb(151, 151, 151);
  border-right: solid 0.063rem rgb(151, 151, 151);
  border-top: solid 0.063rem rgb(151, 151, 151);
  border-bottom: solid 0.063rem rgb(151, 151, 151);
}

.font_2 {
  font-size: 0.56rem;
  font-family: 'Heiti SC';
  line-height: 0.56rem;
  font-weight: 700;
  color: rgb(85, 185, 126);
}

.font_3 {
  font-size: 0.69rem;
  font-family: 'Heiti SC';
  line-height: 0.69rem;
  font-weight: 700;
  color: rgb(151, 151, 151);
}

.section_2 {
  margin-top: 1.88rem;
  border-radius: 1.19rem;
}

.text-wrapper_3 {
  overflow: hidden;
  width: 1.44rem;
  height: 2rem;
}

.text_4 {
  color: rgb(255, 255, 255);
  font-size: 2.44rem;
  font-family: 'Heiti SC';
  font-weight: 700;
  line-height: 2.44rem;
}

.image {
  width: 100%;
  height: 100%;
}

.font_4 {
    font-size: 0.88rem;
    font-family: 'Heiti SC';
    line-height: 0.88rem;
    font-weight: 700;
    color: rgb(255, 255, 255);
  }

  .button {
    padding: 0.56rem 0 0.63rem;
    background-color: rgb(0, 0, 0);
    border-radius: 0.56rem;
    width: 4.63rem;
  }

  .overlay {
    position: absolute;
    top: -5px;
    left: -5px;
    width: 15px;
    height: 15px;
    background-color: #D54B45;
    border-radius: 50%;
  }

.overlay::before,
.overlay::after {
  content: '';
  position: absolute;
  width: 1.5px;
  height: 11px;
  background-color: white;
}

.overlay::before {
  transform: rotate(45deg);
}

.overlay::after {
  transform: rotate(-45deg);
}

.overlay::before {
  top: 30%;
  left: 30%;
  transform-origin: center;
}

.overlay::after {
  top: 10%;
  left: 30%;
  transform-origin: center;
}

.overlay::before {
  transform-origin: 5px 7px;
}

.overlay::after {
  transform-origin: 2.5px 3px;
}

</style>