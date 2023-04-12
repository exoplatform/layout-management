<template>
  <div>
    <span class="font-weight-bold text-start text-color body-2">{{ $t('siteNavigation.label.whoCanView') }}</span>
    <v-select
      v-model="visibility"
      :items="visibilityLabel"
      item-text="text"
      item-value="value"
      dense
      class="caption"
      outlined />
    <exo-identity-suggester
      v-if="showAccessPermissionsGroupSuggester"
      ref="NavigationNodeAccessPermissions"
      :labels="suggesterLabels"
      v-model="accessPermission"
      name="accessPermissions"
      height="40"
      include-groups
      required />
    <span v-if="!accessPermissions.length" class="caption mx-2 mt-n4 position-absolute error-color">
      {{ $t('siteNavigation.label.filed.required') }}
    </span>
    <template v-if="showAccessPermissionsList">
      <site-navigation-node-permission-item
        v-for="(permission, index) in accessPermissions"
        :key="index"
        :permission-group="permission.group"
        :membership-type="permission.membershipType"
        @membership-type-changed="updatePermissionMembershipType(permission.group, $event)"
        @remove-permission="$root.$emit('remove-access-permission', permission)" />
    </template>
  </div>
</template>

<script>
export default {
  props: {
    accessPermissions: {
      type: Array,
      default: () => {
        return [];
      }
    },
    visibility: {
      type: String,
      default: 'Everyone'
    },
  },
  data() {
    return {
      loading: false,
      navigationNode: null,
      accessPermission: null
    };
  },
  computed: {
    suggesterLabels() {
      return {
        placeholder: this.$t('siteNavigation.label.groupSuggester.placeholder'),
        noDataLabel: this.$t('siteNavigation.label.groupSuggester.noData')
      };
    },
    visibilityLabel(){
      return [
        {
          text: this.$t('siteNavigation.label.view.everyone'),
          value: 'Everyone',
        },
        {
          text: this.$t('siteNavigation.label.view.designedGroup'),
          value: 'GROUP',
        },
      ];
    },
    showAccessPermissionsGroupSuggester(){
      return this.visibility === 'GROUP';
    },
    showAccessPermissionsList(){
      return this.showAccessPermissionsGroupSuggester && this.accessPermissions[0]?.group.id;
    },
  },
  watch: {
    accessPermission() {
      if (this.accessPermission?.id) {
        const permission = {
          group: this.accessPermission,
          membershipType: '*'};
        this.$root.$emit('add-new-access-permission', permission);
        this.$refs.NavigationNodeAccessPermissions.emitSelectedValue({});
      }
    },
    visibility(){
      this.$root.$emit('change-access-permission-visibility', this.visibility);
    }
  },
  methods: {
    updatePermissionMembershipType(permissionGroup, membershipType) {
      const permission = {
        group: permissionGroup,
        membershipType: membershipType};
      this.$root.$emit('update-access-permission-membership-type', permission);
    }
  }
};
</script>