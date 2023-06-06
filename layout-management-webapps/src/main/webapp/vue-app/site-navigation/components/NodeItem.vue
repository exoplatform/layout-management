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
    <v-hover>
      <v-row
        slot-scope="{ hover }"
        class="d-flex pt-2 px-0 text-truncate v-list-item v-list-item--dense d-flex flex-nowrap"
        :class="`${highlightNode ? ' light-grey-background ' : ' '}`">
        <v-col
          :cols="cols"
          class="my-0 py-0 px-0">
          <v-icon
            v-if="hasChildren && !hideChildren"
            size="23"
            class="px-0"
            :class="!$vuetify.rtl ? 'pull-right' : 'pull-left'"
            @click="displayChildren = !displayChildren">
            {{ icon }}
          </v-icon>
          <div v-else class="ms-3 me-2"></div>
        </v-col>
        <v-col
          class="my-0 py-0 text-truncate">
          <v-list-item class="px-0">
            <v-list-item-content>
              <v-list-item-title
                :title="navigationNode.label"
                class="font-weight-bold text-truncate"
                style="max-width: 250px">
                {{ navigationNode.label }}
              </v-list-item-title>
              <v-list-item-subtitle
                :title="navigationNodeUri"
                class="text-truncate"
                style="max-width: 250px">
                {{ navigationNodeUri }}
              </v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
        </v-col>
        <v-col
          cols="2"
          class="my-0 py-0">
          <site-navigation-node-item-menu
            :navigation-node="navigationNode"
            :hover="hover"
            :can-move-up="canMoveUp"
            :can-move-down="canMoveDown"
            :node-to-paste="nodeToPaste"
            :paste-mode="pasteMode" />
        </v-col>
      </v-row>
    </v-hover>
    <template v-if="displayChildren">
      <site-navigation-node-item
        v-for="child in navigationNode.children"
        :key="child.id"
        :navigation-node="child"
        :can-move-up="canMoveUpChildNode(child)"
        :can-move-down="canMoveDownChildNode(child)"
        :cols="cols + 1"
        :hide-children="hideChildren" />
    </template>
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
    cols: {
      type: Number,
      default: () => 1,
    },
    hideChildren: {
      type: Boolean,
      default: true
    },
  },
  data() {
    return {
      displayChildren: false,
      nodeToPaste: null,
      pasteMode: null
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
    this.$root.$on('cut-node', this.cutNode);
    this.$root.$on('copy-node', this.copyNode);
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
          this.$siteNavigationService.moveNode(navigationNodeId, null, previousNodeId).then(() => {
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
          this.$siteNavigationService.moveNode(navigationNodeId, null, previousNodeId).then(() => {
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
    cutNode(navigationNode) {
      this.pasteMode = 'Cut';
      this.nodeToPaste = navigationNode;
    },
    copyNode(navigationNode) {
      this.pasteMode = 'Copy';
      this.nodeToPaste = navigationNode;
    }
  }
};
</script>