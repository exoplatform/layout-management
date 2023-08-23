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
  <v-app>
    <v-card
      color="transparent"
      class="mb-5"
      flat>
      <v-hover>
        <v-img
          slot-scope="{ hover }"
          :lazy-src="imageUrl"
          :src="imageUrl"
          :min-height="36"
          :max-height="height"
          id="imageImg"
          height="auto"
          min-width="100%"
          class="d-flex"
          eager>
          <div
            v-show="hover"
            class="d-flex flex-grow-1 position-absolute full-height full-width">
            <div class="me-2 ms-auto my-auto mt-sm-2 mb-sm-0">
              <v-btn
                v-if="!isDefaultImage"
                v-show="hover"
                :title="$t('image.title.deleteImage')"
                id="imageDeleteButton"
                outlined
                icon
                dark
                @click="removeImage">
                <v-icon size="18">mdi-delete</v-icon>
              </v-btn>
              <v-btn
                v-show="hover"
                ref="imageInput"
                id="imageEditButton"
                :title="$t('image.title.ChangeImage')"
                icon
                outlined
                dark
                @click="$refs.imageCropDrawer.open()">
                <v-icon size="18">fas fa-file-image</v-icon>
              </v-btn>
            </div>
          </div>
        </v-img>
      </v-hover>
      <image-crop-drawer
        ref="imageCropDrawer"
        :src="imageCropperSrc"
        :crop-options="imageCropperOptions"
        :drawer-title="imageCropperDrawerTitle"
        :max-file-size="maxUploadSizeInBytes"
        :max-image-width="1280"
        :no-expand-icon="false"
        back-icon
        @input="updateUploadId"
        @data="imageData = $event" />
      <image-crop-drawer
        ref="imageCropDrawer"
        :crop-options="cropOptions"
        :max-file-size="maxUploadSizeInBytes"
        :src="imageUrl"
        max-image-width="1280"
        drawer-title="image.title.ChangeImage"
        @input="uploadImage" />
    </v-card>
    <layout-notification-alerts />
  </v-app>
</template>

<script>
const DEFAULT_MAX_UPLOAD_SIZE_IN_MB = 2;
const ONE_KB = 1024;

export default {
  props: {
    imageUrl: {
      type: String,
      default: '',
    },
    fileId: {
      type: String,
      default: '',
    },
    maxUploadSize: {
      type: Number,
      default: () => DEFAULT_MAX_UPLOAD_SIZE_IN_MB,
    },
    saveSettingsURL: {
      type: String,
      default: '',
    }
  },
  data: () => ({
    cropOptions: {
      aspectRatio: 1280 / 225,
      viewMode: 1,
    },
  }),
  computed: {
    maxUploadSizeInBytes() {
      return this.maxUploadSize * ONE_KB * ONE_KB;
    },
    isDefaultImage() {
      return this.imageUrl && this.imageUrl.includes('/portal/rest/v1/image/default');
    },
    height() {
      let height = 171.5;
      if (this.isMobile) {
        height -= 50;
      }
      return height;
    },
  },
  mounted() {
    if (this.fileId === 'default') {
      this.getImage('default');
    }
    this.$root.$applicationLoaded();
  },
  methods: {
    getImage(uploadId) {
      return this.$imagesService.getImage(uploadId)
        .then(data => {
          this.imageUrl = data.imageUrl;
          this.fileId = data.fileId;
          this.saveSettings();
        });
    },
    uploadImage(uploadId) {
      return this.getImage(uploadId).
        then(() => {
          const message = this.$t('image.title.ImageUpdated');
          this.$root.$emit('layout-notification-alert', {
            message,
            type: 'success',
          });
          return this.$nextTick();
        });
    },
    removeImage() {
      this.getImage('default')
        .then(() => {
          const message = this.$t('image.title.ImageDeleted');
          this.$root.$emit('layout-notification-alert', {
            message,
            type: 'success',
          });
          return this.$nextTick();
        });
    },
    saveSettings() {
      this.$imagesService.saveSettings(this.saveSettingsURL ,{
        fileId: this.fileId,
        imageUrl: this.imageUrl
      });
    }
  },

};
</script>