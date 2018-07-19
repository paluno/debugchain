<template>
  <transition name="fade">
    <div class="alert alert-danger" role="alert">
      {{errorMessage}}
    </div>
  </transition>
</template>

<script>
export default {
  name: "ErrorToast",
  props: {
    error: {
      type: Error,
      required: true
    }
  },
  computed: {
    errorMessage() {
      if (this.error.userMessage) {
        return this.error.userMessage;
      } else {
        return "An unknown error occured! We're sorry, please try again later.";
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}
</style>
