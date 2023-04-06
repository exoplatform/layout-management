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
  <exo-drawer
    ref="siteNavigationManageAccessDrawer"
    id="siteNavigationManageAccessDrawer"
    :right="!$vuetify.rtl"
    eager
    @closed="close">
    <template slot="title">
      <div class="d-flex">
        <i
          class="uiIcon uiArrowBAckIcon"
          @click="close"></i>
        <span class="ms-2"> {{ $t('siteNavigation.manageAccessDrawer.title') }}</span>
      </div>
    </template>
    <template slot="content">
      <v-card class="mx-4 my-4 px-2 py-2 elevation-0">
        <template>
          <span class="font-weight-bold text-start text-color body-2">{{ $t('siteNavigation.label.whoCanEdit') }}</span>
          <exo-identity-suggester
            ref="NavigationNodeEditPermissions"
            :labels="suggesterLabels"
            v-model="editPermissions"
            name="editPermissions"
            height="40"
            include-groups
            required />
        </template>
        <v-divider class="my-8" />
        <template>
          <span class="font-weight-bold text-start text-color body-2">{{ $t('siteNavigation.label.whoCanView') }}</span>
          <v-select
            v-model="visibilityChoice"
            :items="visibilityLabel"
            item-text="text"
            item-value="value"
            dense
            class="caption"
            outlined />
          <exo-identity-suggester
            v-if="showViewPermissionsGroupSuggester"
            ref="NavigationNodeViewPermissions"
            :labels="suggesterLabels"
            v-model="viewPermissions"
            name="viewPermissions"
            height="40"
            include-groups
            required />
        </template>
      </v-card>
    </template>
    <template slot="footer">
      <div class="d-flex justify-end">
        <v-btn
          class="btn ms-2"
          @click="close">
          {{ $t('siteNavigation.label.btn.cancel') }}
        </v-btn>
        <v-btn
          :loading="loading"
          class="btn btn-primary ms-2">
          {{ $t('siteNavigation.label.btn.save') }}
        </v-btn>
      </div>
    </template>
  </exo-drawer>
</template>
<script>
export default {

  data() {
    return {
      loading: false,
      navigationNode: null,
      editPermissions: null,
      viewPermissions: null,
      visibilityChoice: 'EVERYONE',
    };
  },
  computed: {
    suggesterLabels() {
      return {
        placeholder: this.$t('siteNavigation.label.groupSuggester.placeholder'),
        noDataLabel: this.$t('siteNavigation.label.groupSuggester.noData')
      };
    },
    visibilityLabel(){
      return [
        {
          text: this.$t('siteNavigation.label.view.everyone'),
          value: 'EVERYONE',
        },
        {
          text: this.$t('siteNavigation.label.view.designedGroup'),
          value: 'GROUP',
        },
      ];
    },
    showViewPermissionsGroupSuggester(){
      return this.visibilityChoice === 'GROUP';
    },
  },
  created() {
    this.$root.$on('open-site-navigation-manage-access-drawer', this.open);
  },
  methods: {
    open(navigationNode) {
      this.navigationNode = navigationNode;
      this.$refs.siteNavigationManageAccessDrawer.open();
    },
    close() {
      this.$refs.siteNavigationManageAccessDrawer.close();
    },
  }
};
</script>