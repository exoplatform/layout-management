<!--
Copyright (C) 2023 eXo Platform SAS.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
-->
<template>
  <v-alert
    v-model="displayAlert"
    :type="alertType"
    border="left"
    class="white position-relative"
    elevation="2"
    dismissible
    colored-border
    outlined>
    <div class="d-flex">
      <div>
        <span class="text-color">
          {{ alertMessage }}
        </span>
        <div class="text-color">
          {{ administratorMessage }}
        </div>
      </div>
      <div :class="isMobile ? 'pt-0' : 'pt-3'">
        <v-btn
          v-if="alert.click"
          class="primary--text"
          text
          @click="alert.click">
          {{ alert.clickMessage }}
        </v-btn>
      </div>
      <v-btn
        slot="close"
        slot-scope="{toggle}"
        icon
        small
        light
        @click="toggle">
        <v-icon>close</v-icon>
      </v-btn>
    </div>
  </v-alert>
</template>
<script>
export default {
  props: {
    alert: {
      type: Object,
      default: null
    },
  },
  data: () => ({
    displayAlert: true,
  }),
  computed: {
    alertMessage() {
      return this.alert.message;
    },
    alertType() {
      return this.alert.type;
    },
    administratorMessage() {
      return this.alert?.administratorMessage;
    },
  },
  watch: {
    displayAlert() {
      if (!this.displayAlert) {
        this.$emit('dismissed');
      }
    },
  },
  created() {
    const time = 5000;
    window.setTimeout(() => this.displayAlert = false, time);
  },
};
</script>