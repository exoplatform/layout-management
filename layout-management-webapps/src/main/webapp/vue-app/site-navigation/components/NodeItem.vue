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
  <v-hover>
    <div slot-scope="{ hover }">
      <v-list-item
        dense
        class="px-0">
        <v-list-item-action class="me-2 my-0">
          <v-icon
            v-if="hasChildren"
            size="24"
            @click="displayChildren = !displayChildren">
            {{ icon }}
          </v-icon>
          <div v-else class="mfs-3 mfe-2"></div>
        </v-list-item-action>
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
        <v-list-item-action class="mx-0 my-0">
          <site-navigation-node-item-menu
            :navigation-node="navigationNode"
            :hover="hover" />
        </v-list-item-action>
      </v-list-item>
      <div v-if="displayChildren">
        <site-navigation-node-item
          v-for="child in navigationNode.children"
          :key="child.id"
          :navigation-node="child"
          class="ms-7" />
      </div>
    </div>
  </v-hover>
</template>

<script>
export default {
  props: {
    navigationNode: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      displayChildren: false,
    };
  },
  computed: {
    icon() {
      return this.displayChildren && 'mdi-menu-down' || 'mdi-menu-right';
    },
    hasChildren() {
      return this.navigationNode?.children?.length;
    },
    navigationNodeUri() {
      return `/${this.navigationNode.uri}`;
    },
  },
  created() {
    this.$root.$on('delete-node', this.deleteNodeChild);
  },
  methods: {
    deleteNodeChild(navigationNodeId) {
      if (this.navigationNode.children.length) {
        const index = this.navigationNode.children.findIndex(child => child.id === navigationNodeId);
        if (index >= 0) {
          this.navigationNode.children.splice(index, 1);
        }
      }
    }
  }
};
</script>