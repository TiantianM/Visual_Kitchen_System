<script setup>
import { useRouter } from 'vue-router';
import { reactive, ref } from 'vue';

const data = reactive({
  email: '',
  password: '',
  name: '',  // used for registration
  showRegistration: ref(false)  // toggle registration form
});

const router = useRouter();

const userRegister = async () => {
  try {
    const response = await fetch('http://localhost:5050/user/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        email: data.email,
        password: data.password,
        name: data.name
      })
    });

    if (response.ok) {
      const result = await response.json();
      alert('User Registration Successful: ' + result.message); // Display successful registration message
      data.showRegistration = false; // Hide registration form
      data.email = '';
      data.password = '';
      data.name = '';
    } else {
      const errorResponse = await response.text();
      alert('User Registration Failed: ' + errorResponse); // Display error message
    }
  } catch (error) {
    alert('Error during registration: ' + error);
  }
};

const login = async () => {
  try {
    const response = await fetch('http://localhost:5050/user/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        email: data.email,
        password: data.password
      })
    });

    if (response.ok) {
      const result = await response.json();
      // Store the token, user's name, and user's ID in localStorage
      localStorage.setItem('token', result.token);
      localStorage.setItem('username', result.name); // Adjust 'username' if the property name is different in your response
      localStorage.setItem('uid', result.id);       // Adjust 'userId' if the property name is different in your response
      
      // Redirect to the index page
      router.push('/index');
    } else {
      alert('Login failed, please check your credentials and try again.');
    }
  } catch (error) {
    alert('Network or server error');
  }
};


const toggleRegistration = () => {
  data.showRegistration = !data.showRegistration;
  data.email = '';
  data.password = '';
  data.name = '';
};
</script>

<template>
  <div class="flex-col page">
    <span class="self-start text">Welcome Back!</span>
    <div v-if="data.showRegistration" class="flex-col self-stretch group">
      <span class="self-start font">User Name</span>
      <el-input class="input mt-7" v-model="data.name"></el-input>
      <span class="self-start font">Email</span>
      <el-input class="input mt-7" v-model="data.email"></el-input>
      <span class="self-start font">Password</span>
      <el-input type="password" class="input mt-7" v-model="data.password"></el-input>
      <div class="flex-col justify-start items-center self-center text-wrapper_3" @click="userRegister">
        <span class="text_5">Submit Registration</span>
      </div>
    </div>
    <div v-else class="flex-col self-stretch group">
      <span class="self-start font">Email</span>
      <el-input class="input mt-7" v-model="data.email"></el-input>
      <span class="self-start font">Password</span>
      <el-input type="password" class="input mt-7" v-model="data.password"></el-input>
      <div class="flex-col justify-start items-center self-center text-wrapper_3" @click="login">
        <span class="text_5">Login</span>
      </div>
      <div class="flex-col justify-start items-center self-center text-wrapper_3" @click="toggleRegistration">
        <span class="text_5">{{ data.showRegistration ? 'Back to Login' : 'Register' }}</span>
      </div>
    </div>
  </div>
</template>

<style scoped lang="css">
  .mt-7 {
    margin-top: 0.44rem;
  }
  .page {
    padding: 4.13rem 1.13rem 26.75rem 1.13rem;
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
    margin-top: 2.69rem;
  }
  .font {
    font-size: 1rem;
    font-family: 'Heiti SC';
    line-height: 1rem;
    font-weight: 700;
    color: rgb(0, 0, 0);
  }
  .input {
    align-self: stretch;
  }
  .font_2 {
    font-size: 1rem;
    font-family: 'Heiti SC';
    line-height: 1rem;
    font-weight: 700;
    color: rgb(151, 151, 151);
  }
  .text_4 {
    margin-top: 2.06rem;
    font-size: 0.88rem;
    line-height: 0.88rem;
  }
  .text-wrapper_3 {
    margin-top: 1.81rem;
    padding: 1rem 0 0.94rem;
    background-color: rgb(0, 0, 0);
    width: 12.56rem;
  }
  .text_5 {
    color: rgb(255, 255, 255);
    font-size: 0.88rem;
    font-family: 'Heiti SC';
    font-weight: 700;
    line-height: 0.88rem;
  }
</style>