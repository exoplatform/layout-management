<template>
  <exo-drawer
    id="siteNavigationAddNodeDrawer"
    ref="siteNavigationAddNodeDrawer"
    right
    allow-expand
    @closed="close">
    <template slot="title">
      <div class="d-flex">
        <v-icon
          size="16"
          class="clickable"
          @click="close()">
          fas fa-arrow-left
        </v-icon>
        <span> {{ $t('siteNavigation.label.addNode.title') }} </span>
      </div>
    </template>
    <template slot="content">
      <v-form>
        <v-card-text class="d-flex pb-2">
          <v-label>
            <span class="text-color font-weight-bold">
              {{ $t('siteNavigation.label.nodeLabel.title') }} *              
            </span>
            <p class="caption">{{ $t('siteNavigation.label.nodeLabel.description') }} </p>
          </v-label>
        </v-card-text>
        <v-card-text class="d-flex py-0">
          <input
            type="text"
            class="ignore-vuetify-classes flex-grow-1"
            maxlength="2000"
            required>
        </v-card-text>
        <v-card-text class="d-flex flex-grow-1 pb-2">
          <v-label>
            <span class="text-color font-weight-bold mr-6">
              {{ $t('siteNavigation.label.nodeId.title') }} *              
            </span>
          </v-label>
        </v-card-text>
        <v-card-text class="d-flex py-0">
          <input
            type="text"
            class="ignore-vuetify-classes flex-grow-1"
            maxlength="2000"
            required>
        </v-card-text>
        <v-card-text class="d-flex flex-grow-1 pb-2">
          <v-label>
            <span class="text-color font-weight-bold">
              {{ $t('siteNavigation.label.nodeIcon.title') }}            
            </span>
          </v-label>
        </v-card-text>
        <v-card-text>
          <div class="d-flex flex-row py-0">
            <v-label>
              <span class="caption">
                {{ $t('siteNavigation.label.nodeIcon.description') }}
              </span>
            </v-label>
            <v-spacer />
            <v-file-input
              prepend-icon="fas fa-camera z-index-two rounded-circle primary-border-color py-1 ms-3"
              class="file-selector pa-0 ma-0" />
          </div>
        </v-card-text>
        <v-card-text class="d-flex flex-grow-1 pb-2">
          <v-label>
            <span class="text-color font-weight-bold pt-2">
              {{ $t('siteNavigation.label.visibility.title') }} 
            </span>
          </v-label>
          <v-icon
            color="black"
            size="21"
            class="pl-2">
            fa-info-circle
          </v-icon>
        </v-card-text>
        <v-card-text>
          <div class="d-flex flex-row">
            <v-switch      
              v-model="visible"
              class="mt-0 me-1" />
            <span class="caption">
              {{ $t('siteNavigation.label.visibility.visible') }}
            </span>
          </div>
          <div class="d-flex flex-row">
            <v-switch     
              v-model="scheduleVisibility" 
              class="mt-0 me-1" />
            <span class="caption">
              {{ $t('siteNavigation.label.visibility.scheduleVisibility') }}
            </span>
          </div>
        </v-card-text>
        <v-card-text class="d-flex flex-grow-1 text-no-wrap text-left font-weight-bold pb-2">
          <v-label>
            <span class="text-color font-weight-bold">
              {{ $t('siteNavigation.label.nodeType.title') }}
            </span>
          </v-label>
        </v-card-text>
        <v-card-text class="d-flex flex-row">
          <div class="d-flex flex-column">
            <v-radio-group
              v-model="nodeType"
              class="mt-0">
              <v-radio
                :label="$t('siteNavigation.label.nodeType.group')"
                value="Group" />
              <v-radio
                :label="$t('siteNavigation.label.nodeType.pageOrLink')"
                value="pagrOrLink" />
            </v-radio-group>
          </div>
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
  data: () => ({
    visible: true,
    scheduleVisibility: false,
    nodeType: 'Group',
  }),
  created() {
    this.$root.$on('open-site-navigation-add-node-drawer', this.open);
  },
  methods: {
    open() {
      this.$refs.siteNavigationAddNodeDrawer.open();
    },
    close() {
      this.$refs.siteNavigationAddNodeDrawer.close();
    }
  },
};
</script>