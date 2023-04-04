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
  <div>
    <v-list-item
      dense
      class="px-0">
      <v-list-item-content>
        <v-list-item-title
          :title="navigationNode.label"
          class="font-weight-bold text-truncate">
          {{ navigationNode.label }}
        </v-list-item-title>
        <v-list-item-subtitle
          :title="navigationNodeUri"
          class="text-truncate">
          {{ navigationNodeUri }}
        </v-list-item-subtitle>
      </v-list-item-content>
    </v-list-item>
  </div>
</template>

<script>
export default {
  props: {
    navigationNode: {
      type: Object,
      default: null,
    },
  },
  computed: {
    navigationNodeUri() {
      return `/${this.navigationNode.uri}`;
    },
  }
};
</script>