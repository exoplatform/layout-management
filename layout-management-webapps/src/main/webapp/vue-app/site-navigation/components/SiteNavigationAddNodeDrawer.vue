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
      <v-form
        v-model="isValidInputs">
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
                value="pageOrLink" />
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
          :disabled="disabled"
          v-if="displayNextBtn"
          :loading="loading"
          class="btn btn-primary ms-2"
          @click="openAddElementDrawer">
          {{ $t('siteNavigation.label.btn.next') }}
        </v-btn>
        <v-btn
          v-else
          :disabled="disabled"
          :loading="loading"
          @click="createNode"
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
      navigationNode: null,
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
      isValidInputs: true,
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
  computed: {
    disabled() {
      return !(this.isValidInputs && this.nodeId && this.nodeLabel);
    },
    displayNextBtn() {
      return this.nodeType === 'pageOrLink';
    }
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
      this.navigationNode = parentNavigationNode;
      const siteKey = parentNavigationNode.siteKey;
      if (siteKey.typeName === 'portal') {
        this.parentNavigationNodeUrl = `/portal/${siteKey.name}/${parentNavigationNode.uri}`;
      } else {
        this.parentNavigationNodeUrl = `/portal/g/${siteKey.name.replaceAll('/', ':')}/${parentNavigationNode.uri}`;
      }
      this.$refs.siteNavigationAddNodeDrawer.open();
    },
    close() {
      this.nodeId = null;
      this.nodeLabel = null;
      this.visible = true;
      this.scheduleVisibility = false;
      this.nodeType = 'Group';
      this.disabled = true;
      this.$refs.siteNavigationAddNodeDrawer.close();
    },
    createNode() {
      const nodeChildrenLength = this.navigationNode.children.length;
      const previousNodeId = nodeChildrenLength ? this.navigationNode.children[nodeChildrenLength -1].id : null;
      this.$siteNavigationService.createNode(this.navigationNode.id, previousNodeId, this.nodeLabel, this.nodeId, this.visible)
        .then(() => {
          this.$root.$emit('refresh-navigation-nodes');
        })
        .finally(() => {
          this.close();
        });
    },
    openAddElementDrawer() {
      this.$root.$emit('open-add-element-drawer', this.open);
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