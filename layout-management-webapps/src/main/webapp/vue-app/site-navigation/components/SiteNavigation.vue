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
  <v-app v-if="canManageSiteNavigation && !isMobile">
    <v-tooltip bottom>
      <template #activator="{ on, attrs }">
        <v-btn
          size="16"
          outlined
          icon
          class="mx-2">
          <v-icon
            v-on="on"
            v-bind="attrs"
            class="mb-2 text-color"
            size="16"
            @click="openSiteNavigationDrawer">
            fas fa-sitemap
          </v-icon>
        </v-btn>
      </template>
      <span>{{ $t('siteNavigation.button.tooltip.label') }}</span>
    </v-tooltip>
    <site-navigation-drawer v-if="siteNavigationDrawerOpened" />
    <site-navigation-manage-access-drawer v-if="siteNavigationDrawerOpened" />
    <site-navigation-node-drawer v-if="siteNavigationDrawerOpened" />
    <site-navigation-element-drawer v-if="siteNavigationDrawerOpened" />
    <site-navigation-notification-alerts v-if="siteNavigationDrawerOpened" />
  </v-app>
</template>
<script>
export default {
  data () {
    return {
      siteNavigationDrawerOpened: false,
    };
  },
  props: {
    canManageSiteNavigation: {
      type: Boolean,
      default: false,
    },
  },
  computed: {
    isMobile() {
      return this.$vuetify.breakpoint.width < 768 ;
    },
  },
  methods: {
    openSiteNavigationDrawer() {
	  this.siteNavigationDrawerOpened = true;
	  this.$nextTick().then(() => this.$root.$emit('open-site-navigation-drawer'));
    }
  }
};
</script>
