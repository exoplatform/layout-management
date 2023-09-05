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
              {{ $t('siteManagement.label.siteDescription.title') }}           
            </span>
          </v-label>
        </v-card-text>
        <v-card-text class="d-flex py-0">
          <exo-activity-rich-editor
            v-model="siteDescription"
            max-length="1300"
            :tag-enabled="false"
            class="flex" />
        </v-card-text>
        <v-card-text class="mt-4">
          <v-label>
            <span class="text-color font-weight-bold mb-2"> {{ $t('siteManagement.label.displayOrder') }}</span>
          </v-label>
          <v-row class="mx-0 mt-2">
            <v-col class="d-flex flex-row px-0 py-0 col-10">
              <v-switch
                v-model="displayed"
                :disabled="site.metaSite"
                class="mt-2" />
              <label v-if="displayed" class="subtitle-1 mx-1"> {{ $t('siteManagement.label.displayed') }} </label>
              <label v-else class="subtitle-1 mx-1"> {{ $t('siteManagement.label.notDisplayed') }} </label>
            </v-col>
            <v-col class="col-2 px-0 py-0 mt-n1 orderDisplay">
              <v-text-field
                v-if="displayed"
                v-model="displayOrder"
                :rules="[rules.value]"
                type="number"
                class="pt-2"
                outlined
                required />
            </v-col>
          </v-row>
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
      displayOrder: 0,
      displayed: true,
      rules: {
        value: (v) => (v > 0 && v<= 9999) || this.$t('siteManagement.displayOrder.error')
      },
    };
  },
  created() {
    this.$root.$on('open-site-card-properties-drawer', this.open);
  },
  computed: {
    disabled(){
      return !this.siteLabel || (this.displayed && this.displayOrder < 1);
    }
  },
  methods: {
    open(site) {
      this.site =  site;
      this.siteName = this.site.name;
      this.siteLabel = this.site.displayName || this.site.name;
      this.siteDescription = this.site.description !== null ?  this.site.description : '';
      this.displayed = this.site.displayed;
      this.displayOrder = this.site.displayOrder;
      this.$nextTick().then(() => {
        this.$refs.siteCardPropertiesDrawer.open();
      });
    },
    close() {
      this.site = null;
      this.siteName = '';
      this.siteLabel = '';
      this.siteDescription = '';
      this.$refs.siteCardPropertiesDrawer.close();
    },
    updateSite() {
      return this.$siteManagementService.updateSite(this.site.name, this.site.siteType, this.siteLabel, this.siteDescription, this.site.metaSite || this.displayed, this.displayed && this.displayOrder || 0)
        .then(() => {
          const message = this.$t('siteManagement.label.updateSite.success');
          this.$root.$emit('layout-notification-alert', {
            message,
            type: 'success',
          });
          this.$root.$emit('refresh-sites');
          this.close();
        }).catch((e) => {
          const message = e.message ==='401' &&  this.$t('siteNavigation.label.updateSite.unauthorized') || this.$t('siteNavigation.label.updateSite.error');
          this.$root.$emit('layout-notification-alert', {
            message,
            type: 'error',
          });
        })
        .finally(() => {
          this.loading = false;
          this.$refs.siteCardPropertiesDrawer.endLoading();
        });
    }

  }
};
</script>
