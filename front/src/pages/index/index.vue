<script setup>
import { useRoute, useRouter } from 'vue-router';
import { ref, onMounted, onUnmounted } from 'vue';
import { ElLoading, ElMessageBox, ElMessage } from 'element-plus';
import TopButton from '../../components/top_button.vue';
import GridIngredient from '../../components/grid_ingredient.vue'
import customFetch from '../../customFetch'

const route = useRoute();
const router = useRouter();


const logout = () => {
  localStorage.removeItem('token'); // Remove the stored JWT token
  localStorage.removeItem('uid');   // Remove the stored user ID
  router.push('/');            // Redirect to login page
};

const email = ref('');
const password = ref('');
const items = ref([]);

onMounted(async () => {
  email.value = route.query.email || '';
  password.value = route.query.password || '';
  await fetchData();
});

const fetchData = async () => {
  try {
    let uid = localStorage.getItem('uid');
    if (!uid) {
      throw new Error('No user ID found. User might not be logged in.');
    }

    const response = await fetch(`http://localhost:5050/fridge/list/${uid}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`, // Assuming you use Bearer tokens
      },
    });

    console.log("fetchData: ");
    console.log(response);

    if (!response.ok) {
      throw new Error(`Network response was not ok: ${response.statusText}`);
    }

    const data = await response.json();
    items.value = data;
  } catch (error) {
    console.error('Error fetching data:', error);
    // Handle the error accordingly, perhaps redirect to login page or show a message
  }
};

const sortKey = ref(0);
const sortItemsByExpiryDate = () => {
  console.log('Sorting items by expiry date');
  items.value.sort((a, b) => new Date(a.expirationDate) - new Date(b.expirationDate));
  sortKey.value++; 
};

const sortByName = () => {
  console.log('Sorting items by name');
  items.value.sort((a, b) => {
    const nameA = (a.ingredient ? a.ingredient.name : a.otherName).toLowerCase();
    const nameB = (b.ingredient ? b.ingredient.name : b.otherName).toLowerCase();
    return nameA.localeCompare(nameB);
  });
  sortKey.value++; 
};




const navigateToManualPage = () => {
  localStorage.setItem('edit_type', 'manual');
  router.push({
    path: '/edit_ingredient'
  });
};

const navigateToRecommendation = () => {
  // console.log(items);
  localStorage.setItem('fridge_result', JSON.stringify(items.value));
  router.push({
    path: '/recommend',
    // query: {fridge: JSON.stringify(items.value)}
  });
};

const navigateToCreator = () => {
  router.push({
    path: '/creator_recipe'
  });
};

const fileInput = ref(null);

const navigateToCameraPage = () => {
  fileInput.value.click();
};

const processImage = async (file) => {
  try {
    const loadingInstance = ElLoading.service({
      fullscreen: true
    });

    const formData = new FormData();
    formData.append('file', file);

    const response = await customFetch('/rekognition/detectLabels', {
      method: 'POST',
      body: formData
    });

    loadingInstance.close();

    if (!response.ok) {
      throw new Error(`Failed to detect labels: ${response.statusText}`);
    }

    return await response.json();
  } catch (error) {
    throw error;
  }
};

const handleFileChange = async (event) => {
  const file = event.target.files[0];
  if (file) {
    try {
      const labels = await processImage(file);
      router.push({ path: '/camera_result', query: { labels: labels } });
    } catch (error) {
      console.error('Error processing image:', error);
      ElMessageBox.alert('Error processing image. Please try again.', 'Message', {
        confirmButtonText: 'OK',
        type: 'error'
      });
    }
  }
};

const showLabelsMessageBox = (labels) => {
  console.log(labels)
  if (labels.length <= 20) {
    ElMessageBox.alert('Recognition result: ' + labels.join('</br>'), 'Message', {
      confirmButtonText: 'OK',
      dangerouslyUseHTMLString: true,
      type: 'success'
    });
  } else {
    ElMessageBox.alert('Too many labels. Please choose another way to view.', 'Message', {
      confirmButtonText: 'OK',
      type: 'info'
    });
  }
};

const handleDelete = async (index) => {
  console.log(items.value[index]);
  try {
    const id = items.value[index].id;
    const response = await customFetch(`/fridge/${id}`, {
      method: 'DELETE',
    });
    if (response.ok) {
      const data = await response.text();
      ElMessage.success(data);
      items.value.splice(index, 1);
    } else {
      throw new Error('Failed to delete');
    }
  } catch (error) {
    console.error('Failed to delete:', error);
    ElMessage.error('Failed to delete');
  }
};

const handleEdit = (index) => {
  const itemToEdit = items.value[index];
  // console.log(itemToEdit);
  // console.log(index);
  localStorage.setItem('edit_type', 'edit');
  sessionStorage.setItem('ingredient', JSON.stringify(itemToEdit));

  router.push({
    name: 'edit_ingredient',
      query: { index }
    })
};

// const video = ref(null);
// const canvas = ref(null);
// let stream = null; 

// const openCamera = async () => {
//   try {
//     stream = await navigator.mediaDevices.getUserMedia({ video: true });
//     if (stream) {
//       video.value.srcObject = stream;
//       video.value.style.display = 'block';
//     }
//   } catch (error) {
//     console.error('Error accessing camera:', error);
//   }
// };

// const takePhoto = () => {
//   const videoElement = video.value;
//   const canvasElement = canvas.value;
//   const context = canvasElement.getContext('2d');
//   const width = videoElement.videoWidth;
//   const height = videoElement.videoHeight;
  
//   canvasElement.width = width;
//   canvasElement.height = height;
  
//   context.drawImage(videoElement, 0, 0, width, height);
  
//   const photoDataUrl = canvasElement.toDataURL('image/png');
//   const blob = dataURItoBlob(photoDataUrl);
//   const photoFile = new File([blob], 'photo.png', { type: 'image/png' });

//   stopCamera();
// };

// const stopCamera = () => {
//   if (stream) {
//     stream.getTracks().forEach(track => track.stop());
//   }
// };

// const dataURItoBlob = (dataURI) => {
//   const byteString = atob(dataURI.split(',')[1]);
//   const mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];
//   const ab = new ArrayBuffer(byteString.length);
//   const ia = new Uint8Array(ab);
//   for (let i = 0; i < byteString.length; i++) {
//     ia[i] = byteString.charCodeAt(i);
//   }
//   return new Blob([ab], { type: mimeString });
// };

// onMounted(openCamera);

// onUnmounted(stopCamera);

const showCameraDialog = ref(false);
const video = ref(null);

const openCameraDialog = () => {
  showCameraDialog.value = true;
  startVideoStream();
};

const handleCloseCameraDialog = () => {
  showCameraDialog.value = false;
  stopVideoStream();
  const video = document.getElementById('video');
  if (video.srcObject) {
    const tracks = video.srcObject.getTracks();
    tracks.forEach(track => track.stop());
  }
};

const startVideoStream = () => {
  navigator.mediaDevices.getUserMedia({ video: true })
    .then(stream => {
      video.value.srcObject = stream;
      video.value.play();
    })
    .catch(error => console.error('Error starting video stream:', error));
};

const stopVideoStream = () => {
  if (video.value.srcObject) {
    const tracks = video.value.srcObject.getTracks();
    tracks.forEach(track => track.stop());
  }
};

const takePhoto = async () => {
  try {
    const canvas = document.createElement('canvas');
    const context = canvas.getContext('2d');
    canvas.width = video.value.videoWidth;
    canvas.height = video.value.videoHeight;
    context.drawImage(video.value, 0, 0, canvas.width, canvas.height);
    showCameraDialog.value = false;
    stopVideoStream();

    const file = await new Promise(resolve => canvas.toBlob(resolve, 'image/jpeg'));
    const labels = await processImage(file);
    router.push({ path: '/camera_result', query: { labels: labels } });
  } catch (error) {
    console.error('Error processing image:', error);
    ElMessageBox.alert('Error processing image. Please try again.', 'Message', {
      confirmButtonText: 'OK',
      type: 'error'
    });
  }
};

</script>

<template>
  <div class="flex-col page">
    <div>
      <TopButton/>
    </div>
    <span id="usernameSpan">{{ fullUsername }}</span>
    <div class="flex-row self-stretch group">
      <div class="flex-col justify-start items-center text-wrapper"><span class="font text_2">Fridge</span></div>
      <div class="flex-col justify-start text-wrapper_2 ml-11" @click="navigateToRecommendation"><span class="font text_3">Recommendation</span></div>
      <div class="flex-col justify-start text-wrapper_2 ml-11" @click="navigateToCreator"><span class="font text_3">Creator</span></div>
    </div>
    <GridIngredient :items="items" :onDelete="handleDelete" :onEdit="handleEdit" :key="sortKey" />
    <div class="flex-row justify-between items-center self-center section_2" style="width: 15rem;">
      <div class="flex-col justify-start items-center button" @click="navigateToManualPage"><span class="font_4">Manual</span></div>
      <div class="flex-col justify-end items-center button" @click="navigateToCameraPage">
        <span class="font_4">Upload</span>
        <input type="file" ref="fileInput" style="display: none" @change="handleFileChange">
      </div>
      <div class="flex-col justify-end items-center button" @click="openCameraDialog">
        <span class="font_4">Capture</span>
      </div>  
    </div>
    <button class="sort-button" @click="sortItemsByExpiryDate">Sort by Expiry Date</button>
    <button class="sort-button" @click="sortByName">Sort by Name</button>
    
    <button class="sort-button" @click="logout">Logout</button>
  </div>

  <el-dialog append-to-body v-model="showCameraDialog" title="Capture" width="65%" :before-close="handleCloseCameraDialog">
    <video ref="video" width="100%" height="auto"></video>
    <div class="flex-row justify-center mt-3">
      <el-button @click="takePhoto">Take Photo</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      username: 'User', // Default username
      fullUsername: '' // Full string to display
    };
  },
  methods: {
    fetchUsername() {
      const storedUsername = localStorage.getItem('username');
      this.username = storedUsername || this.username; // Use stored username or default
      this.fullUsername = `${this.username}'s refrigerator`; // Set the full string
    }
  },
  mounted() {
    this.fetchUsername(); // Fetch and set the username when component is mounted
  }
}
</script>

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
  grid-template-columns: repeat(auto-fit, minmax(150px, 200px));
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

.sort-button {
  padding: 10px 20px;
  background-color: #4CAF50; 
  color: white; 
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
  margin-top: 3px; 
  transition: background-color 0.3s; 
}

.sort-button:hover {
  background-color: #45a049; 
}

</style>


