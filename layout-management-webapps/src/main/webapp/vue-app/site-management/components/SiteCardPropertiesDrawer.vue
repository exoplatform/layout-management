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
    id="siteCardPropertiesDrawer"
    ref="siteCardPropertiesDrawer"
    :right="!$vuetify.rtl"
    eager
    allow-expand
    @closed="close">
    <template slot="title">
      <span>{{ $t('siteManagement.drawer.properties.title') }}</span>
    </template>
    <template slot="content">
      <v-form>
        <v-card-text class="d-flex pb-2">
          <v-label>
            <span class="text-color font-weight-bold">
              {{ $t('siteManagement.label.siteLabel.title') }} *              
            </span>
          </v-label>
        </v-card-text>
        <v-card-text class="d-flex py-0">
          <v-text-field
            v-model="siteLabel"
            class="pt-0"
            type="text"
            required="required"
            outlined
            dense />
        </v-card-text>
        <v-card-text class="d-flex pb-2">
          <v-label>
            <span class="text-color font-weight-bold">
              {{ $t('siteManagement.label.siteName.title') }} *              
            </span>
          </v-label>
        </v-card-text>
        <v-card-text class="d-flex py-0">
          <v-text-field
            v-model="siteName"
            class="pt-0"
            type="text"
            required="required"
            :disabled="disableSiteName"
            outlined
            dense />
        </v-card-text>
        <v-card-text class="d-flex pb-2">
          <v-label>
            <span class="text-color font-weight-bold">
              {{ $t('siteManagement.label.siteDescription.title') }} *             
            </span>
          </v-label>
        </v-card-text>
        <v-card-text class="d-flex py-0">
          <exo-activity-rich-editor
            v-model="siteDescription"
            max-length="1300"
            class="flex" />
        </v-card-text>
      </v-form>
    </template>
    <template slot="footer">
      <div class="d-flex justify-end">
        <v-btn
          class="btn ms-2"
          @click="close">
          {{ $t('siteNavigation.label.btn.cancel') }}
        </v-btn>
        <v-btn
          :disabled="disabled"
          :loading="loading"
          @click="updateSite"
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
      site: null,
      siteName: '',
      siteLabel: '',
      siteDescription: '',
      disableSiteName: true,
    };
  },
  created() {
    this.$root.$on('open-site-card-properties-drawer', this.open);
  },
  methods: {
    open(site) {
      this.site =  site;
      this.siteName = this.site.name;
      this.siteLabel = this.site.displayName || this.site.name;
      this.siteDescription = this.site.description;
      this.$refs.siteCardPropertiesDrawer.open();
    },
    close() {
      this.$refs.siteCardPropertiesDrawer.close();
    },
    updateSite() {
      return this.$siteManagementService.updateSite(this.site.name, this.site.siteType, this.siteLabel, this.siteDescription)
        .then(() => {
          this.$root.$emit('refresh-sites');
          this.close();
        });
    }

  }
};
</script>
