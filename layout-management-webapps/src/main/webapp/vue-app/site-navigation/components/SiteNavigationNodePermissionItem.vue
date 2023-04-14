<template>
  <div class="d-flex" v-if="permissionGroup?.id">
    <v-icon
      size="19"
      class="fas fa-users me-3" />
    <span
      class="d-flex text-truncate font-weight-bold text-start text-color body-2 mt-2">
      {{ displayName }}
    </span>
    <v-spacer />
    <site-navigation-node-permission-type-selector
      :membership-type="membershipType"
      class="mt-1"
      @membership-type-changed="$emit('membership-type-changed', $event)" />
    <v-icon
      size="16"
      color="grey lighten-1"
      class="me-2 iconStyle"
      @click="$emit('remove-permission', permissionGroup)">
      fas fa-trash
    </v-icon>
  </div>
</template>

<script>
export default {
  props: {
    permissionGroup: {
      type: Object ,
      default: null
    },
    membershipType: {
      type: String ,
      default: '*'
    }
  },
  computed: {
    displayName(){
      return this.permissionGroup?.label || this.permissionGroup?.displayName;
    }
  },
};
</script>
