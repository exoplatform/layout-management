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
