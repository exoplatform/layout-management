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
    <site-navigation-button
      icon-class="text-color" 
      :icon-color="iconColor"
      :disabled="disabled"
      :site-name="siteName"
      :site-type="siteType" />
    <site-navigation-drawer v-if="siteNavigationDrawerOpened" />
    <manage-permissions-drawer v-if="siteNavigationDrawerOpened" />
    <site-navigation-node-drawer v-if="siteNavigationDrawerOpened" />
    <site-navigation-element-drawer v-if="siteNavigationDrawerOpened" />
  </v-app>
</template>
<script>
export default {
  props: {
    canManageSiteNavigation: {
      type: Boolean,
      default: false,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
    iconColor: {
      type: String,
      default: null,
    },
    siteName: {
      type: String,
      default: null,
    },
    siteType: {
      type: String,
      default: null,
    },
  },
  data () {
    return {
      siteNavigationDrawerOpened: false,
    };
  },
  computed: {
    isMobile() {
      return this.$vuetify.breakpoint.width < 768 ;
    },
  },
  created() {
    document.addEventListener('open-site-navigation-drawer', event => this.openSiteNavigationDrawer(event.detail));
  },
  methods: {
    openSiteNavigationDrawer(data) {
      this.siteNavigationDrawerOpened = true;
      this.$nextTick().then(() => this.$root.$emit('open-site-navigation-drawer', data));
    }
  }
};
</script>
