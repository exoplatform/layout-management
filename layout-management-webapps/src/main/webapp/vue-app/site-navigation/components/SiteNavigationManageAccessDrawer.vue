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
    ref="siteNavigationManageAccessDrawer"
    id="siteNavigationManageAccessDrawer"
    :right="!$vuetify.rtl"
    eager
    @closed="close">
    <template slot="title">
      <div class="d-flex">
        <i
          class="uiIcon uiArrowBAckIcon clickable"
          @click="close"></i>
        <span class="ms-2"> {{ $t('siteNavigation.manageAccessDrawer.title') }}</span>
      </div>
    </template>
    <template slot="content">
      <v-card class="mx-4 my-4 px-2 py-2 elevation-0">
        <site-navigation-node-edit-permission
          :permission="editPermission" />
        <v-divider class="my-8" />
        <site-navigation-node-access-permission
          :access-permissions="accessPermissions"
          :type="accessPermissionType" />
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
          :disabled="!enableSave"
          @click="save"
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
      loading: false,
      navigationNode: null,
      editPermission: {
        group: {},
        membershipType: '*'},
      accessPermissions: [],
      accessPermissionType: ''
    };
  },
  computed: {
    enableSave() {
      return (this.accessPermissions.length || this.accessPermissionType ==='Everyone' ) && this.editPermission?.group?.id;
    },
  },
  created() {
    this.$root.$on('open-site-navigation-manage-access-drawer', this.open);
    this.$root.$on('reset-edit-permission', this.resetEditPermission);
    this.$root.$on('edit-permission-membership-type-changed', this.updateEditPermissionMembershipType);
    this.$root.$on('add-access-permission', this.addAccessPermission);
    this.$root.$on('remove-access-permission', this.removeAccessPermission);
    this.$root.$on('update-access-permission-membership-type', this.updateAccessPermissionMembership);
    this.$root.$on('change-access-permission-type', this.changeAccessPermissionType);
  },
  methods: {
    open(navigationNode) {
      this.navigationNode = JSON.parse(JSON.stringify(navigationNode));
      this.editPermission = JSON.parse(JSON.stringify(navigationNode.pageEditPermission));
      this.accessPermissionType = navigationNode.pageAccessPermissions[0].membershipType === 'Everyone' && 'Everyone' || 'GROUP';
      this.accessPermissions = this.accessPermissionType === 'Everyone' && [] ||  JSON.parse(JSON.stringify(navigationNode.pageAccessPermissions));

      this.$nextTick()
        .then(() => {
          this.$refs.siteNavigationManageAccessDrawer.open();
        });
    },
    close() {
      this.resetEditPermission();
      this.accessPermissions = [];
      this.$refs.siteNavigationManageAccessDrawer.close();
    },
    resetEditPermission() {
      this.editPermission = {
        membershipType: '*'
      };
    },
    updateEditPermissionMembershipType(membershipType) {
      this.editPermission.membershipType = membershipType;
    },
    addAccessPermission(accessPermission) {
      this.accessPermissions.push(accessPermission);
    },
    removeAccessPermission(index) {
      this.accessPermissions.splice(index, 1);
    },
    updateAccessPermissionMembership(accessPermission) {
      this.accessPermissions[accessPermission.index].membershipType = accessPermission.membershipType;
    },
    changeAccessPermissionType(accessPermissionType) {
      this.accessPermissionType = accessPermissionType;
      if (accessPermissionType === 'Everyone') {
        this.accessPermissions = ['Everyone'];
      } else if (accessPermissionType === 'GROUP') {
        this.accessPermissions =  this.navigationNode?.pageAccessPermissions[0]?.membershipType === 'Everyone' && [] || this.navigationNode.pageAccessPermissions;
      }
    },
    save() {
      this.loading = true;
      this.$refs.siteNavigationManageAccessDrawer.startLoading();
      const pageEditPermission =this.convertPermission(this.editPermission);
      let pageAccessPermissions = ['Everyone'];
      if (this.accessPermissions[0] !== 'Everyone') {
        pageAccessPermissions = [];
        this.accessPermissions.forEach(permission => {
          if (permission.group?.id) {
            const accessPermission = this.convertPermission(permission);
            pageAccessPermissions.push(accessPermission);
          }
        });
      }
      const pageRef = this.navigationNode.pageKey.ref ||`${ this.navigationNode.pageKey.site.typeName}::${ this.navigationNode.pageKey.site.name}::${this.navigationNode.pageKey.name}`;
      return this.$siteNavigationService.updateNodePagePermission(pageRef, pageEditPermission, pageAccessPermissions)
        .then(() => {
          const message = this.$t('siteNavigation.label.updatePermission.success');
          this.$root.$emit('navigation-node-notification-alert', {
            message,
            type: 'success',
          });
          this.$root.$emit('refresh-navigation-nodes');
          this.close();
        }).catch((e) => {
          const message = e.message ==='401' &&  this.$t('siteNavigation.label.updatePermission.unauthorized') || this.$t('siteNavigation.label.updatePermission.error');
          this.$root.$emit('navigation-node-notification-alert', {
            message,
            type: 'error',
          });
        })
        .finally(() => {
          this.loading = false;
          this.$refs.siteNavigationManageAccessDrawer.endLoading();
        });
    },
    convertPermission(permission){
      if (permission.group.providerId === 'space'){
        return `${permission.membershipType}:/spaces/${permission.group.remoteId}`;
      } else {
        return `${permission.membershipType}:${permission.group.spaceId || permission.group.id}`;
      }
    }
  }
};
</script>