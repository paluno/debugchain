<template>
    <div v-if="value" class="modal-mask">
        <div class="modal-wrapper">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">{{title}}</h5>
                        <button type="button" class="close" @click="dismiss()" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <slot></slot>
                    </div>
                    <div class="modal-footer">
                        <slot name="footer">
                            <button type="button" class="btn btn-secondary" @click="dismiss()">Close</button>
                        </slot>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
  props: {
    title: String,
    value: Boolean
  },
  methods: {
    dismiss: function() {
      this.$emit("input", false);
    }
  }
};
</script>

<style lang="scss" scoped>

.modal-mask {
  position: fixed;
  z-index: 1000;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: table;
  transition: opacity 0.3s ease;
}

.modal-wrapper {
  display: table-cell;
  vertical-align: middle;
}

.modal-body {
    max-height: calc(100vh - 200px);
    overflow-y: auto;
}
</style>
