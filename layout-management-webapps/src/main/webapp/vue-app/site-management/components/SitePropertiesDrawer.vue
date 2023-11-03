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
    id="sitePropertiesDrawer"
    ref="sitePropertiesDrawer"
    v-model="sitePropertiesDrawer"
    :right="!$vuetify.rtl"
    :allow-expand="!$root.isMobile"
    eager
    @expand-updated="expanded = $event"
    @closed="close">
    <template slot="title">
      <span>{{ $t('siteManagement.drawer.properties.title') }}</span>
    </template>
    <template v-if="showDrawerContent" slot="content">
      <v-form>
        <v-card-text class="d-flex pb-2">
          <translation-text-field
            ref="siteTitleTranslation"
            v-model="siteTitleTranslations"
            :field-value.sync="siteLabel"
            :maxlength="maxTitleLength"
            :no-expand-icon="!expanded"
            drawer-title="siteManagement.label.translateTitle"
            :object-id="siteId"
            object-type="site"
            field-name="label"
            name="siteTitle"
            class="width-auto flex-grow-1 pt-4"
            back-icon
            autofocus
            required>
            <template #title>
              <v-label>
                <span class="text-color font-weight-bold">
                  {{ $t('siteManagement.label.siteLabel.title') }} *              
                </span>
              </v-label>
            </template>
          </translation-text-field>
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
        <v-card-text class="d-flex py-0">
          <translation-text-field
            ref="siteDescriptionTranslation"
            v-model="siteDescriptionTranslations"
            :field-value.sync="siteDescription"
            :object-id="siteId"
            :max-length="maxDescriptionLength"
            :no-expand-icon="!expanded"
            drawer-title="siteManagement.label.translateDescription"
            object-type="site"
            field-name="description"
            class="width-auto flex-grow-1 pt-4"
            back-icon
            rich-editor>
            <template #title>
              <v-label>
                <span class="text-color font-weight-bold">
                  {{ $t('siteManagement.label.siteDescription.title') }}           
                </span>
              </v-label>
            </template>
            <rich-editor
              id="siteDescription"
              ref="siteDescriptionEditor"
              v-model="siteDescription"
              :max-length="maxDescriptionLength"
              :tag-enabled="false"
              ck-editor-type="site" />
          </translation-text-field>
        </v-card-text>
        <v-card-text class="mt-3 py-4 px-3">
          <v-label>
            <span class="text-color font-weight-bold mb-2 px-1 text-capitalize"> {{ $t('siteManagement.label.banner') }}</span>
          </v-label>
          <v-label>
            <span class="text-color caption px-1 text-capitalize"> {{ $t('siteManagement.label.caption') }}</span>
          </v-label>
          <site-management-banner-selector
            ref="siteBannerSelector"
            :src="siteBannerUrl"
            :default-src="defaultSiteBannerUrl"
            :is-default="isDefaultBanner"
            :no-expand="!expanded"
            @updated="updateBannerUploadId"
            @reset="resetBannerUploadId" />
        </v-card-text>
        <v-card-text class="mt-4">
          <v-label>
            <span class="text-color font-weight-bold mb-2"> {{ $t('siteManagement.label.displayOrder') }}</span>
          </v-label>
          <v-row class="mx-0 mt-2">
            <v-col class="d-flex flex-row px-0 py-0 col-10">
              <v-switch
                v-model="displayed"
                :disabled="displayedDisabled"
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
          {{ $t('siteManagement.label.btn.cancel') }}
        </v-btn>
        <v-btn
          :disabled="saveDisabled"
          :loading="loading"
          @click="updateSite"
          class="btn btn-primary ms-2">
          {{ $t('siteManagement.label.btn.save') }}
        </v-btn>
      </div>
    </template>
  </exo-drawer>
</template>

<script>
export default {
  data() {
    return {
      sitePropertiesDrawer: false,
      site: null,
      siteName: '',
      siteLabel: '',
      siteDescription: '',
      maxDescriptionLength: 1300,
      disableSiteName: true,
      displayOrder: 0,
      displayed: true,
      siteTitleTranslations: {},
      siteDescriptionTranslations: {},
      siteId: null,
      expanded: false,
      siteBannerUrl: '',
      bannerUploadId: null,
      hasDefaultBanner: true,
      isDefaultBanner: true,
      defaultSiteBannerUrl: '',
      rules: {
        value: (v) => (v > 0 && v<= 9999) || this.$t('siteManagement.displayOrder.error')
      },
    };
  },
  created() {
    this.$root.$on('open-site-properties-drawer', this.open);
  },
  watch: {
    siteDescription() {
      if (this.$refs.siteDescriptionTranslation) {
        this.$refs.siteDescriptionTranslation.setValue(this.siteDescription);
      }
    },
  },
  computed: {
    saveDisabled(){
      return !this.siteLabel || (this.displayed && this.displayOrder < 1);
    },
    displayedDisabled(){
      return this.site?.metaSite;
    },
    showDrawerContent() {
      return this.sitePropertiesDrawer && !!this.site;
    },
  },
  methods: {
    open(site, freshInstance) {
      this.siteTitleTranslations = {};
      if (site && !freshInstance) {
        this.$refs.sitePropertiesDrawer.open();
        return this.$siteService.getSiteById(parseInt(site.siteId), false, false, 'en')
          .then(freshSite => this.open(freshSite, true));
      }
      this.site = site;
      this.siteName = site.name;
      this.siteId = site.siteId;
      this.siteLabel = site.displayName || site.name ;
      this.siteDescription = site.description;
      this.displayed = site.displayed;
      this.displayOrder = site.displayOrder;
      this.siteBannerUrl = site.bannerUrl;
      this.defaultSiteBannerUrl = `/portal/rest/v1/social/sites/${site.name}/banner?isDefault=true`;
      this.isDefaultBanner = site.bannerFileId === 0 ;
      this.hasDefaultBanner = this.isDefaultBanner;
      this.bannerUploadId = null;
      this.$nextTick().then(() => {
        this.$refs.sitePropertiesDrawer.open();
      });
    },
    close() {
      this.site = null;
      this.reset();
      this.$refs.sitePropertiesDrawer.close();
    },
    updateBannerUploadId(bannerUploadId) {
      this.bannerUploadId = bannerUploadId;
      this.isDefaultBanner = false;
    },
    resetBannerUploadId() {
      this.bannerUploadId = '0';
      this.isDefaultBanner = true;
    },
    reset() {
      this.sitePropertiesDrawer = false;
      this.isDefaultBanner = true;
      this.siteBannerUrl = null;
      this.defaultSiteBannerUrl = null;
      this.siteBannerSelectorKey = null;
      this.$refs.siteBannerSelector.reset();
    },
    updateSite() {
      return this.$siteManagementService.updateSite(this.site.name, this.site.siteType, this.siteLabel, this.siteDescription, this.site.metaSite || this.displayed, this.displayed && this.displayOrder || 0, this.bannerUploadId !== '0' && this.bannerUploadId || null, !this.hasDefaultBanner && this.bannerUploadId === '0')
        .then(() => this.$translationService.saveTranslations('site', this.siteId, 'label', this.siteTitleTranslations))
        .then(() => this.$translationService.saveTranslations('site', this.siteId, 'description', this.siteDescriptionTranslations))
        .then(() => {
          const message = this.$t('siteManagement.label.updateSite.success');
          this.$root.$emit('layout-notification-alert', {
            message,
            type: 'success',
          });
          this.$root.$emit('refresh-sites');
          this.close();
        }).catch((e) => {
          const message = e.message ==='401' &&  this.$t('siteManagement.label.updateSite.unauthorized') || this.$t('siteManagement.label.updateSite.error');
          this.$root.$emit('layout-notification-alert', {
            message,
            type: 'error',
          });
        })
        .finally(() => {
          this.loading = false;
          this.$refs.sitePropertiesDrawer.endLoading();
        });
    }
  }
};
</script>
