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
  <v-menu
    v-model="displayActionMenu"
    transition="slide-x-reverse-transition"
    :right="!$vuetify.rtl"
    offset-x
    offset-y
    class="px-0 mx-2 overflow-visible">
    <template #activator="{ on, attrs }">
      <v-btn
        v-bind="attrs"
        class="pull-right"
        icon
        v-on="on">
        <v-icon>mdi-dots-vertical</v-icon>
      </v-btn>
    </template>
    <v-list class="pa-0" dense>
      <v-list-item
        v-if="navigationNode.pageKey"
        class="subtitle-2" 
        @click="editLayout">
        {{ $t('siteNavigation.label.editLayout') }}
      </v-list-item>
      <v-divider />
      <v-list-item
        @click="deleteNode()">
        <v-icon
          size="13"
          class="pe-1">
          fas fa-trash
        </v-icon>
        <v-list-item-title
          class="subtitle-2">
          <span class="ps-1">{{ $t('siteNavigation.label.delete') }}</span>
        </v-list-item-title>
      </v-list-item>
    </v-list>
  </v-menu>
</template>

<script>
export default {
  props: {
    navigationNode: {
      type: Object,
      default: null,
    },
  },
  data: () => ({
    displayActionMenu: false,
  }),
  computed: {
    pageName() {
      return this.navigationNode?.pageKey?.name;
    },
    pageSiteType() {
      return this.navigationNode?.pageKey?.site?.typeName;
    },
    pageSiteName() {
      return this.navigationNode?.pageKey?.site?.name;
    },
  },
  created() {
    $(document).on('mousedown', () => {
      if (this.displayActionMenu) {
        window.setTimeout(() => {
          this.displayActionMenu = false;
        },200);
      }
    });
  },
  methods: {
    deleteNode() {
      const deleteDelay = 6;
      this.$siteNavigationService.deleteNode(this.navigationNode.id, deleteDelay)
        .then(() => {
          this.$root.$emit('confirm-node-deletion', this.navigationNode);
        });
      const redirectionTime = 6100;
      setTimeout(() => {
        const deletedNode = localStorage.getItem('deletedNode');
        if (deletedNode != null) {
          this.$root.$emit('navigation-node-deleted');
        }
      }, redirectionTime);
    },
    editLayout() {
      const uiPageId = $('.UIPage').attr('id').split('UIPage-')[1];
      return this.$siteNavigationService.editLayout(uiPageId, this.pageName, this.pageSiteType, this.pageSiteName);    
    }
  }
};
</script>