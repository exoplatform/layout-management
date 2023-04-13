<template>
  <exo-drawer
    ref="siteNavigationAddElementDrawer"
    id="siteNavigationAddElementDrawer"
    :right="!$vuetify.rtl"
    eager
    @closed="close">
    <template slot="title">
      <div class="d-flex">
        <v-icon
          size="16"
          class="clickable"
          @click="close()">
          fas fa-arrow-left
        </v-icon>
        <span class="ms-2"> {{ $t('siteNavigation.addElementDrawer.title') }}</span>
      </div>
    </template>
    <template slot="content">
      <v-card class="mx-4 my-4 px-2 py-2 elevation-0">
        <template>
          <span class="font-weight-bold text-start text-color body-2">{{ $t('siteNavigation.label.selectElementType') }}</span>
          <v-select
            v-model="elementType"
            :items="elementTypes"
            item-text="text"
            item-value="value"
            dense
            class="caption pt-1 mb-5"
            outlined />
        </template>
        <template>
          <span class="font-weight-bold text-start text-color body-2 mt-8">{{ $t('siteNavigation.label.selectOpenType') }}</span>
          <v-select
            v-model="openMode"
            :items="openModes"
            item-text="text"
            item-value="value"
            dense
            class="caption pt-1 mb-5"
            outlined />
        </template>
        <template v-if="isLinkElement">
          <span class="font-weight-bold text-start text-color body-2 mt-8">{{ $t('siteNavigation.label.link') }}</span>
          <v-text-field
            v-model="link"
            :placeholder="$t('siteNavigation.label.enterUrl') "
            class="pt-0"
            type="text"
            required="required"
            outlined
            dense />
        </template>
      </v-card>
    </template>
    <template slot="footer">
      <div class="d-flex justify-end">
        <v-btn
          class="btn ms-2"
          @click="close">
          {{ $t('siteNavigation.label.btn.cancel') }}
        </v-btn>
        <v-btn
          :loading="loading"
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
      elementType: 'newPage',
      openMode: 'sameTab',
      link: ''
    };
  },
  computed: {
    elementTypes() {
      return [
        {
          text: this.$t('siteNavigation.label.newPage'),
          value: 'newPage',
        },
        {
          text: this.$t('siteNavigation.label.existingPage'),
          value: 'existingPage',
        },
        {
          text: this.$t('siteNavigation.label.link'),
          value: 'link',
        },
      ];
    },
    openModes() {
      return [
        {
          text: this.$t('siteNavigation.label.sameTab'),
          value: 'sameTab',
        },
        {
          text: this.$t('siteNavigation.label.newTab'),
          value: 'newTab',
        },
      ];
    },
    isLinkElement() {
      return this.elementType === 'link';
    },
  },
  created() {
    this.$root.$on('open-add-element-drawer', this.open);
  },
  methods: {
    open() {
      this.$refs.siteNavigationAddElementDrawer.open();
    },
    close() {
      this.$refs.siteNavigationAddElementDrawer.close();
    }
  }
};
</script>