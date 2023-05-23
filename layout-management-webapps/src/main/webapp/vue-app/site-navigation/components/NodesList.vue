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
      v-for="navigationNode in navigationNodes"
      :key="navigationNode.id"
      class="px-1">
      <v-list-item-content class="text-truncate py-0 px-3">
        <site-navigation-node-item
          :navigation-node="navigationNode"
          :can-move-up="canMoveUpNode(navigationNode)"
          :can-move-down="canMoveDownNode(navigationNode)" />
      </v-list-item-content>
    </v-list-item>
  </v-list>
</template>

<script>
export default {
  props: {
    navigationNodes: {
      type: Object,
      default: null
    },
  },
  created() {
    this.$root.$on('delete-node', this.deleteNode);
    this.$root.$on('moveup-node', this.moveUpNode);
    this.$root.$on('movedown-node', this.moveDownNode);
  },
  methods: {
    canMoveUpNode(navigationNode) {
      return this.navigationNodes.indexOf(navigationNode) > 0;
    },
    canMoveDownNode(navigationNode) {
      return this.navigationNodes.indexOf(navigationNode) < this.navigationNodes.length - 1;
    }, 
    moveUpNode(navigationNodeId) {
      if (this.navigationNodes.length) {
        const index = this.navigationNodes.findIndex(navigationNode => navigationNode.id === navigationNodeId);
        if (index !== -1) {
          const previousNodeId = index >1 ? this.navigationNodes[index - 2].id : null;
          this.$siteNavigationService.moveNode(navigationNodeId, null, previousNodeId).then(() => {
            this.$root.$emit('refresh-navigation-nodes');
          });
        }
        
      }
    },
    moveDownNode(navigationNodeId) {
      if (this.navigationNodes.length) {
        const index = this.navigationNodes.findIndex(navigationNode => navigationNode.id === navigationNodeId);
        if (index !== -1) {
          const previousNodeId = this.navigationNodes[index + 1].id;
          this.$siteNavigationService.moveNode(navigationNodeId, null, previousNodeId).then(() => {
            this.$root.$emit('refresh-navigation-nodes');
          });
        }
      }
    },
    deleteNode(navigationNodeId) {
      if (this.navigationNodes.length) {
        const index = this.navigationNodes.findIndex(navigationNode => navigationNode.id === navigationNodeId);
        if (index >= 0) {
          this.navigationNodes.splice(index, 1);
        }
      }
    }
  }
};
</script>