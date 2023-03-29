<template>
  <v-app v-if="canManageSiteNavigation">
    <v-hover>
      <div slot-scope="{ hover }" class="mx-2">
        <v-tooltip bottom>
          <template #activator="{ on, attrs }">
            <v-btn
              v-bind="attrs"
              v-on="on"
              size="16"
              outlined
              icon>
              <v-icon
                v-show="hover"
                class="mb-2 text-color"
                size="16">
                fas fa-sitemap
              </v-icon>
            </v-btn>
          </template>
          {{ $t('siteNavigation.button.tooltip.label') }}
        </v-tooltip>
      </div>
    </v-hover>
  </v-app>
</template>
<script>
export default {
  props: {
    canManageSiteNavigation: {
      type: Boolean,
      default: false,
    },
  },
};
</script>
