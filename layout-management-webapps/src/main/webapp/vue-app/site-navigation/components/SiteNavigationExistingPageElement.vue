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
  <div>
    <span class="font-weight-bold text-start text-color body-2 mt-8">{{ $t('siteNavigation.label.selectExistingPage') }}</span>
    <v-flex class="mt-4">
      <v-chip
        :class="allSitesChipClass"
        @click="allSites = true">
        <span class="text-truncate">
          {{ $t('siteNavigation.label.allSites') }}
        </span>
      </v-chip>
      <v-chip
        :class="sitesOrSpaceChipClass"
        @click="allSites = false">
        <span class="text-truncate">
          {{ $t('siteNavigation.label.sitesOrSpace') }}
        </span>
      </v-chip>
    </v-flex>
    <site-navigation-navigations-suggester
      v-if="!allSites"
      v-model="selectedSite" />
    <site-navigation-page-suggester
      v-model="selectedPage"
      :search-in-all-sites="allSites"
      :site-type="selectedSite && selectedSite.key.type"
      :site-name="selectedSite && selectedSite.key.name" />
  </div>
</template>

<script>
export default {
  data() {
    return {
      allSites: true,
      selectedSite: null,
      selectedPage: null,
    };
  },
  computed: {
    allSitesChipClass(){
      return this.allSites && 'primary' || '';
    },
    sitesOrSpaceChipClass(){
      return !this.allSites &&'primary' || '';
    },
  }
};
</script>
