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
            :rules="[rules.required]"
            outlined
            dense />
        </v-card-text>
        <v-card-text class="d-flex flex-grow-1 pb-2">
          <v-label>
            <span class="text-color font-weight-bold mr-6">
              {{ $t('siteNavigation.label.nodeId.title') }} *              
            </span>
          </v-label>
        </v-card-text>
        <v-card-text class="d-flex py-0">
          <v-text-field
            v-model="nodeId"
            class="pt-0"
            type="text"
            required="required"
            :rules="[rules.required]"
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
      rules: {
        required: value => (value == null || value.length) || this.$t('siteNavigation.required.error.message'),
      },
    };
  },
  created() {
    this.$root.$on('open-site-navigation-add-node-drawer', this.open);
  },
  methods: {
    open() {
      this.$refs.siteNavigationAddNodeDrawer.open();
    },
    close() {
      this.nodeId = null;
      this.nodeLabel = null;
      this.visible = true;
      this.scheduleVisibility = false;
      this.nodeType = 'Group';
      this.$refs.siteNavigationAddNodeDrawer.close();
    }
  },
};
</script>