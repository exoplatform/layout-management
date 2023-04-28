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
  <div :class="hasChildren ? 'ms-4' : 'ms-7'">
    <v-hover>
      <div slot-scope="{ hover }">
        <v-list-item
          dense
          :class="highlightNode ? 'px-0 light-grey-background' : 'px-0'">
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
              :hover="hover"
              :can-move-up="canMoveUp"
              :can-move-down="canMoveDown" />
          </v-list-item-action>
        </v-list-item>
      </div>
    </v-hover>
    <div v-if="displayChildren">
      <site-navigation-node-item
        v-for="child in navigationNode.children"
        :key="child.id"
        :navigation-node="child"
        :can-move-up="canMoveUpChildNode(child)"
        :can-move-down="canMoveDownChildNode(child)" />
    </div>
  </div>
</template>

<script>
export default {
  props: {
    navigationNode: {
      type: Object,
      default: null,
    },
    canMoveUp: {
      type: Boolean,
      default: () => false,
    },
    canMoveDown: {
      type: Boolean,
      default: () => false,
    },
  },
  data() {
    return {
      displayChildren: false,
    };
  },
  computed: {
    highlightNode() {
      return this.navigationNode.uri === eXo.env.portal.selectedNodeUri;
    },
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
    this.displayCurrentNodeParentTree();
    this.$root.$on('delete-node', this.deleteChildNode);
    this.$root.$on('moveup-node', this.moveUpChildNode);
    this.$root.$on('movedown-node', this.moveDownChildNode);
  },
  methods: {
    canMoveUpChildNode(navigationNode) {
      return this.navigationNode.children.indexOf(navigationNode) > 0;
    },
    canMoveDownChildNode(navigationNode) {
      return this.navigationNode.children.indexOf(navigationNode) < this.navigationNode.children.length - 1;
    }, 
    moveUpChildNode(navigationNodeId) {
      if (this.navigationNode.children.length) {
        const index = this.navigationNode.children.findIndex(navigationNode => navigationNode.id === navigationNodeId);
        if (index !== -1) {
          const previousNodeId = index > 1 ? this.navigationNode.children[index - 2].id : null;
          this.$siteNavigationService.moveNode(navigationNodeId, previousNodeId).then(() => {
            this.$root.$emit('refresh-navigation-nodes');
          });
        }
  
      }
    },
    moveDownChildNode(navigationNodeId) {
      if (this.navigationNode.children.length) {
        const index = this.navigationNode.children.findIndex(navigationNode => navigationNode.id === navigationNodeId);
        if (index !== -1) {
          const previousNodeId = this.navigationNode.children[index + 1].id;
          this.$siteNavigationService.moveNode(navigationNodeId, previousNodeId).then(() => {
            this.$root.$emit('refresh-navigation-nodes');
          });
        } 
      }
    },
    deleteChildNode(navigationNodeId) {
      if (this.navigationNode.children.length) {
        const index = this.navigationNode.children.findIndex(child => child.id === navigationNodeId);
        if (index >= 0) {
          this.navigationNode.children.splice(index, 1);
        }
      }
    },
    displayCurrentNodeParentTree() {
      const currentUri = eXo.env.portal.selectedNodeUri;
      const splittedCurrentUri = currentUri.split('/');
      let nodeUri = '';
      const currentNodeParentTree = splittedCurrentUri.map(subPath => {
        nodeUri += `${subPath}/`;
        return nodeUri.slice(0, -1); // Remove trailing slash
      }).slice(0, -1); // Remove last element
      this.displayChildren = currentNodeParentTree.includes(this.navigationNode.uri);
    },
  }
};
</script>