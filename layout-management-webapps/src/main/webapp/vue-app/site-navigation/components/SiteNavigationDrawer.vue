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
    id="siteNavigationDrawer"
    ref="siteNavigationDrawer"
    :right="!$vuetify.rtl"
    eager
    allow-expand
    @closed="close">
    <template slot="title">
      <span>{{ $t('siteNavigation.drawer.title') }}</span>
    </template>
    <template slot="content">
      <v-chip
        class="ms-4 mt-3 mb-4 primary">
        {{ siteName }}
      </v-chip>
      <site-navigation-nodes-list :navigation-nodes="navigationNodes" />
    </template>
  </exo-drawer>
</template>

<script>
export default {
  data() {
    return {
      navigationNodes: [],
      siteName: eXo.env.portal.siteKeyName,
      siteType: eXo.env.portal.siteKeyType,
    };
  },
  created() {
    this.$root.$on('navigation-node-deleted', this.getNavigationNodes);
    this.$root.$on('refresh-navigation-nodes', this.getNavigationNodes);
    this.getNavigationNodes();
    this.$root.$on('open-site-navigation-drawer', this.open);
  },
  methods: {
    open() {
      this.$refs.siteNavigationDrawer.open();
    },
    close() {
      this.$refs.siteNavigationDrawer.close();
    },
    getNavigationNodes() {
      return this.$siteNavigationService.getNavigationNodes(this.siteType, this.siteName, false, true)
        .then(navigationNodes => {
          this.navigationNodes = navigationNodes || [];
        });
    },
  }
};
</script>
