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
        <span> {{ $t('siteNavigation.drawer.addNode.title') }} </span>
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
          <v-text-field
            v-model="nodeLabel"
            class="pt-0"
            type="text"
            required="required"
            :rules="[nodeLabelRules.required]"
            outlined
            dense 
            @blur="blurOnNodeLabel" />
        </v-card-text>
        <v-card-text class="d-flex flex-grow-1 pb-2">
          <v-label>
            <span class="text-color font-weight-bold mr-6">
              {{ $t('siteNavigation.label.nodeId.title') }} *              
            </span>
            <p
              v-if="nodeId && nodeId.length"
              class="caption text-break">
              {{ nodeUrl }}
            </p>
          </v-label>
        </v-card-text>
        <v-card-text class="d-flex py-0">
          <v-text-field
            v-model="nodeId"
            class="pt-0"
            type="text"
            required="required"
            :rules="nodeIdRules"
            outlined
            dense />
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
  data () {
    return {
      nodeLabel: null,
      nodeId: null,
      visible: true,
      scheduleVisibility: false,
      nodeType: 'Group',
      parentNavigationNodeUrl: '',
      nodeUrl: '',
      nodeLabelRules: {
        required: value => value == null || value.length || this.$t('siteNavigation.required.error.message'),
      },
      nodeIdRules: [
        value => {
          if (value != null && (/\s+/.test(value) || /[^a-zA-Z0-9_-]/.test(value) || /[\u0300-\u036f]/.test(value.normalize('NFD')))){
            return this.$t('siteNavigation.unauthorizedCharacters.error.message');
          } else {
            return value == null || value.length || this.$t('siteNavigation.required.error.message');
          }
        }
      ],
    };
  },
  created() {
    this.$root.$on('open-site-navigation-add-node-drawer', this.open);
  },
  watch: {
    nodeId() {
      this.nodeUrl = `${this.$t('siteNavigation.label.nodeId.description')}${this.parentNavigationNodeUrl}/${this.nodeId}`;
    }
  },
  methods: {
    open(parentNavigationNode) {
      const siteKey = parentNavigationNode.siteKey;
      if (siteKey.name === 'dw') {
        this.parentNavigationNodeUrl = `/portal/${siteKey.name}/${parentNavigationNode.uri}`;
      } else {
        this.parentNavigationNodeUrl = `/portal/g/${siteKey.name.replaceAll('/', ':')}/${parentNavigationNode.uri}`;
      }
      this.$refs.siteNavigationAddNodeDrawer.open();
    },
    close() {
      this.nodeId = null;
      this.nodeLabel = null;
      this.$refs.siteNavigationAddNodeDrawer.close();
    },
    conversionRules() {
      return this.nodeLabel.normalize('NFD').replace(/[\u0300-\u036f]/g, '').replace(/[^a-zA-Z0-9_-]/g, '').replace(/\s+/g, '').toLowerCase();
    },
    blurOnNodeLabel() {
      if (this.nodeId == null) {
        this.nodeId = this.conversionRules();
      }
    }
  },
};
</script>