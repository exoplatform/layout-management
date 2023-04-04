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
    <v-list
      v-for="navigationNode in navigationNodes"
      :key="navigationNode.id"
      class="py-0 mr-0">
      <v-row class="d-flex py-0">
        <v-col
          :cols="cols"
          class="px-1 py-0 d-flex text-truncate">
          <v-list-item
            :class="extraClass">
            <v-list-item-action
              class="me-2 my-0">
              <v-icon
                v-if="navigationNode.children.length"
                size="24"
                @click="displayChildren = !displayChildren">
                {{ icon }}
              </v-icon>
              <div v-else class="mfs-3 mfe-2"></div>
            </v-list-item-action>
            <v-list-item-content class="text-truncate py-0 px-3">
              <site-navigation-node-item :navigation-node="navigationNode" />
            </v-list-item-content>
          </v-list-item>
        </v-col>
        <v-col cols="2">
          <v-list-item-action class="mx-0 my-0 px-0 py-0">
            <site-navigation-node-item-menu :navigation-node="navigationNode" />
          </v-list-item-action>
        </v-col>
      </v-row>
      <div v-if="displayChildren">
        <site-navigation-nodes-list
          :navigation-nodes="navigationNode.children"
          :margin="margin+3"
          :expanded="expanded" />
      </div>
    </v-list>
  </div>
</template>

<script>
export default {
  props: {
    navigationNodes: {
      type: Object,
      default: null
    },
    margin: {
      type: Number,
      default: () => 0,
    },
    expanded: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      displayChildren: false,
      originMargin: 0,
    };
  },
  computed: {
    icon() {
      return this.displayChildren && 'mdi-menu-down' || 'mdi-menu-right';
    },
    extraClass() {
      return `ml-${this.originMargin + this.margin}`;
    },
    cols(){
      return this.expanded && 4 || 9;
    }
  }
};
</script>