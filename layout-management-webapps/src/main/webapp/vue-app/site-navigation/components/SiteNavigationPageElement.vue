<template>
  <div>
    <site-navigation-new-page-element
      v-if="isNewPageElement" />
  </div>
</template>

<script>
export default {
  props: {
    elementType: {
      type: String,
      default: 'newPage'
    }
  },
  computed: {
    isNewPageElement() {
      return this.elementType === 'newPage';
    }
  }
};
</script>
