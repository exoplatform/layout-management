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
      <span>  {{ $t('siteNavigation.drawer.title') }}</span>
    </template>
    <template slot="content">
      <v-chip
        class="ms-4 mt-3 mb-4 primary">
        {{ siteName }}
      </v-chip>
      <site-navigation-nodes-list :navigations-nodes="navigationsNodes" />
    </template>
  </exo-drawer>
</template>

<script>
export default {
  data() {
    return {
      navigationsNodes: [],
    };
  },
  computed: {
    siteName(){
      return eXo.env.portal.siteKeyName;
    }
  },
  created() {
    this.getNavigations();
    this.$root.$on('open-site-navigation-drawer', this.open);
  },
  methods: {
    open() {
      this.$refs.siteNavigationDrawer.open();
    },
    close() {
      this.$refs.siteNavigationDrawer.close();
    },
    getNavigations() {
      return this.$navigationService.getNavigations(eXo.env.portal.siteKeyName, eXo.env.portal.siteKeyType)
        .then(navigationsNodes => {
          this.navigationsNodes = navigationsNodes?.filter(navigationsNode => navigationsNode.siteKey.name.toLowerCase() === eXo.env.portal.siteKeyName.toLowerCase()
              && navigationsNode.siteKey.type.toLowerCase() === eXo.env.portal.siteKeyType.toLowerCase()) || [];
        });
    },
  }
};
</script>
