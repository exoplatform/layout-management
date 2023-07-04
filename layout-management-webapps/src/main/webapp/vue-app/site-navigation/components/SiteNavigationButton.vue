<template>
  <v-tooltip bottom>
    <template #activator="{ on, attrs }">
      <v-btn
        size="16"
        outlined
        icon
        class="mx-2"
        :disabled="disabled">
        <v-icon
          v-on="on"
          v-bind="attrs"
          class="mb-2"
          :color="iconColor"
          :class="iconClass"
          size="16"
          @click="openSiteNavigationDrawer">
          fas fa-sitemap
        </v-icon>
      </v-btn>
    </template>
    <span>{{ $t('siteNavigation.button.tooltip.label') }}</span>
  </v-tooltip>
</template>

<script>
export default {
  props: {
    disabled: {
      type: Boolean,
      default: false,
    },
    iconClass: {
      type: String,
      default: null,
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
  methods: {
    openSiteNavigationDrawer() {
      let params;
      if (this.siteName && this.siteType) {
        params = {
          siteName: this.siteName,
          siteType: this.siteType,
        };
      }
      document.dispatchEvent(new CustomEvent('open-site-navigation-drawer',{detail: params}));
    }
  }
};
</script>
