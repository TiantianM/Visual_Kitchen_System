<script setup>
  import { ElMessage, ElMessageBox } from 'element-plus';
  import { useRoute, useRouter } from 'vue-router';
  import { reactive, onMounted, ref, computed } from 'vue';
  import { UploadFilled } from '@element-plus/icons-vue'
  import TopButton from '../../components/top_button.vue';
  import customFetch from '../../customFetch'

  const props = defineProps({});

  const expiry_date = ref('')
  const keyword = ref('');
  const promptText = ref('');

  const route = useRoute();
  const router = useRouter();
  const num = ref(1)
  
  const index = ref('')
  const items = ref([]);
  let item = ref('')
  let type = ref('manual')
  
  onMounted(() => {
    type = localStorage.getItem('edit_type')
    console.log(type)
    index.value = route.query.index || '';
    if (type == 'camera' && index && index.value!='') {
      let cameraResult = sessionStorage.getItem('camera_result');
      items.value = JSON.parse(cameraResult);
      // console.log(index.value);
      // console.log(items.value);
      // console.log(items.value[index.value]);
      keyword.value = items.value[index.value].ingredient.name;
    } else if (type == 'edit') {
      let ingredient = sessionStorage.getItem('ingredient');
      item.value = JSON.parse(ingredient);
      console.log(item.value);
      keyword.value = item.value.ingredient.name;
      expiry_date.value = item.value.expirationDate;
      num.value = item.value.quantity;
    }
  });

  const handleChange = (value) => {
    num.value = value
    console.log(num.value)
  }

  const click_upload = async () => {
    try {
      if (type == 'camera') {
        items.value[index.value].quantity = num.value;
        items.value[index.value].expirationDate = formatDate(expiry_date.value);
        sessionStorage.setItem('camera_result', JSON.stringify(items.value));
        router.push('/camera_result');
        // console.log(items.value)
      } else if (type == 'edit') {
        try {
          item.value.ingredient.name = keyword.value;
          item.value.expirationDate = expiry_date.value;
          item.value.quantity = num.value;
          const response = await customFetch('/fridge/update', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(item.value)
          });
          if (response.ok) {
            ElMessageBox.alert('Success!', 'Tips', {
              type: 'success'
            }).then(() => {
              router.push({ path: '/index' });
            });
          } else {
            console.error('Request failed:', response.status);
            ElMessage.error('Request failed, please try again later');
          }
        } catch (error) {
          console.error('Error sending request:', error);
          ElMessage.error('Request failed, please try again later');
        }
      } else if (type == 'manual') {
        const response = await customFetch(`/ingredient/${keyword.value}`, {});
        if (response.status === 200) {
          const data = await response.json();
          const id = data.id;
          const requestData = {
            uid: localStorage.getItem('uid'),
            quantity: num.value,
            iid: id,
            expirationDate: formatDate(expiry_date.value)
          };
          await sendRequest(requestData);
        } else if (response.status === 404) {
          const requestData = {
            uid: localStorage.getItem('uid'),
            quantity: num.value,
            otherName: keyword.value,
            expirationDate: formatDate(expiry_date.value)
          };
          await sendRequest(requestData);
        } else {
          console.error('Unexpected response:', response.status);
        }
      }
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  const handleInput = async () => {
    if (keyword.value.length > 1) {
      try {
        const response = await customFetch(`/ingredient/search/${keyword.value}`, {});
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const data = await response.json();
        if (data.length > 0) {
          let promptValue = "";
          for (let i = 0; i < data.length; i++) {
            promptValue += data[i].name + ", \n"; // Access the 'name' property of each object
          }
          promptText.value = promptValue;
        } else {
          promptText.value = 'X';
        }
      } catch (error) {
        console.error('Error fetching data:', error);
        promptText.value = 'Error fetching data';
      }
    } else {
      promptText.value = 'X';
    }
  };

  const sendRequest = async (requestData) => {
    try {
      const response = await customFetch('/fridge/save', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestData)
      });
      if (response.ok) {
        ElMessageBox.alert('Success!', 'Tips', {
          type: 'success'
        }).then(() => {
          router.push({ path: '/index' });
        });
      } else {
        console.error('Request failed:', response.status);
        ElMessage.error('Request failed, please try again later');
      }
    } catch (error) {
      console.error('Error sending request:', error);
      ElMessage.error('Request failed, please try again later');
    }
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toISOString();
  };

  window.addEventListener('keydown', (event) => {
    if (event.key === 'Tab') {
      event.preventDefault();
      keyword.value = promptText.value;
    }
  });
</script>

<template>
  <div class="flex-col page">
    <div>
      <TopButton/>
    </div>
    <span class="font text">Please provide Ingredient details :)</span>
    <div class="flex-col group">
      <div class="flex-col">
        <div class="flex-row">
          <span class="self-start font_2">Name</span>
          <div style="margin: auto;"></div>
          <span class="mt-2 font_3">Prompt: {{ promptText }}</span>
        </div>
        <el-input class="input mt-7 elinput" v-model="keyword" @input="handleInput" :disabled="type === 'edit'"></el-input>
      </div>
      <div class="flex-col mt-15">
        <span class="self-start font_2">Quantity</span>
        <!-- <el-input class="input mt-7 elinput_2" v-model="data.v_model_2"></el-input> -->
        <el-input-number class="mt-7" v-model="num" :min="1" :max="9999" @change="handleChange" />
      </div>
      <div class="flex-col mt-15">
        <span class="self-start font_2">Expiry Date</span>
        <!-- <el-input class="input mt-7 elinput_3" v-model="data.v_model_3"></el-input> -->
        <div class="block mt-7">
          <el-date-picker
            v-model="expiry_date"
            type="date"
            placeholder="Pick a day"
            :size="size"
          />
        </div>
      </div>
    </div>
    <div class="flex-col group_2">
      <div class="flex-col justify-start items-center button" @click="click_upload">
        <span class="font" v-if="type == 'manual'">Upload</span>
        <span class="font" v-else>Edit</span>
      </div>
      <!-- <div class="flex-col justify-start items-center text-wrapper_2 mt-9"><span class="font">Delete</span></div> -->
    </div>
  </div>
</template>

<style scoped lang="css">
  .mt-15 {
    margin-top: 0.94rem;
  }
  .mt-7 {
    margin-top: 0.44rem;
  }
  .mt-9 {
    margin-top: 0.56rem;
  }
  .page {
    padding: 4.13rem 1.13rem 9rem 1.13rem;
    width: 100%;
    overflow-y: auto;
    overflow-x: hidden;
    height: 100%;
  }
  .font {
    font-size: 0.88rem;
    font-family: 'Heiti SC';
    line-height: 1.25rem;
    font-weight: 700;
    color: rgb(255, 255, 255);
  }
  .text {
    color: rgb(0, 0, 0);
    font-size: 1.31rem;
    line-height: 1.31rem;
  }
  .group {
    margin-top: 2.69rem;
  }
  .font_2 {
    font-size: 1rem;
    font-family: 'Heiti SC';
    line-height: 1rem;
    font-weight: 700;
    color: rgb(0, 0, 0);
  }
  .font_3 {
    font-size: 0.7rem;
    font-family: 'Heiti SC';
    line-height: 1rem;
    font-weight: 700;
    color: rgb(106, 102, 102);
  }
  .input {
    align-self: stretch;
  }
  .group_2 {
    margin-top: 7.38rem;
    padding: 0 4.69rem;
  }
  .button {
    margin-right: 0.13rem;
    padding: 0.7rem 0 0.7rem;
    background-color: rgb(0, 0, 0);
  }
  .text-wrapper_2 {
    margin-right: 0.19rem;
    padding: 0.7rem 0 0.7rem;
    background-color: rgb(213, 56, 56);
  }

  .demo-date-picker {
    display: flex;
    width: 100%;
    padding: 0;
    flex-wrap: wrap;
  }

  .demo-date-picker .block {
    padding: 30px 0;
    text-align: center;
    border-right: solid 1px var(--el-border-color);
    flex: 1;
  }

  .demo-date-picker .block:last-child {
    border-right: none;
  }

  .demo-date-picker .demonstration {
    display: block;
    color: var(--el-text-color-secondary);
    font-size: 14px;
    margin-bottom: 20px;
  }
</style>