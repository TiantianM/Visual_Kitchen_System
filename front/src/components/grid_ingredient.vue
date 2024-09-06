<script setup>
import { ref, defineProps } from 'vue';


const props = defineProps({
  items: Array,
  onDelete: Function,
  onEdit: Function
});

const handleDelete = (index) => {
  props.onDelete(index);
};

const handleEdit = (index) => {
  props.onEdit(index);
};

const getExpirationDays = (expirationDate) => {
    const now = new Date();
    const expiration = new Date(expirationDate);
    const diffTime = expiration.getTime() - now.getTime();
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return diffDays;
};

const getImageSource = (item) => {
    if (item.ingredient) {
    return item.ingredient.img;
    } else {
    return item.otherImg;
    }
};

const getImageAlt = (item) => {
    if (item.ingredient) {
    return item.ingredient.name;
    } else {
    return item.otherName;
    }
};

const getItemName = (item) => {
    if (item.ingredient) {
    return item.ingredient.name;
    } else {
    return item.otherName;
    }
};

const getItemSubCategory = (item) => {
    if (item.ingredient && item.ingredient.subCategory) {
    return item.ingredient.subCategory;
    } else {
    return 'Other';
    }
};
</script>
<template>
<div class="grid">
    <div v-for="(item, index) in items" :key="index" class="flex-col grid-item">
        <div class="self-stretch section">
            <div class="overlay" @click="handleDelete(index)"></div>
            <img class="image" :src="getImageSource(item)" :alt="getImageAlt(item)" @click="handleEdit(index)">
        </div>
        <div class="mt-10 flex-row justify-between items-baseline self-stretch" @click="handleEdit(index)">
            <span class="font">{{ getItemName(item) }}</span>
            <span class="font_2">{{ getItemSubCategory(item) }}</span>
        </div>
        <div class="mt-10 flex-row justify-between items-baseline self-stretch" @click="handleEdit(index)">
            <span class="font_3">Expires in {{ getExpirationDays(item.expirationDate) }} days</span>
            <span class="font_3">{{item.quantity}} {{item.ingredient.unit}}</span>
        </div>
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