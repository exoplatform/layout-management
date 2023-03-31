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
  <v-list>
    <v-list-item
      v-for="navigationsNode in navigationsNodes"
      :key="navigationsNode.id"
      class="px-1">
      <v-list-item-content class="text-truncate py-0 px-3">
        <site-navigation-node-item :navigation-node="navigationsNode" />
      </v-list-item-content>
    </v-list-item>
  </v-list>
</template>

<script>
export default {
  props: {
    navigationsNodes: {
      type: Object,
      default: null
    },
  },
};
</script>