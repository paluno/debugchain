<template>
    <transition name="fade">
        <div class="alert alert-danger" role="alert">
            An unknown error occured!
        </div>
    </transition>
</template>

<script>
export default {
  name: "ErrorToast"
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
