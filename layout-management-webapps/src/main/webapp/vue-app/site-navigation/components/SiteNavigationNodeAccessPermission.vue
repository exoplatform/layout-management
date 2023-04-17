<template>
  <div>
    <span class="font-weight-bold text-start text-color body-2">{{ $t('siteNavigation.label.whoCanView') }}</span>
    <v-select
      v-model="type"
      :items="typeLabel"
      item-text="text"
      item-value="value"
      dense
      class="caption"
      outlined />
    <exo-identity-suggester
      v-if="showAccessPermissionsGroupSuggester"
      ref="NavigationNodeAccessPermissions"
      v-model="accessPermission"
      :group-type="groupType"
      :all-groups-for-admin="allGroupsForAdmin"
      :group-member="userGroup"
      :search-options="searchOptions"
      :labels="suggesterLabels"
      name="accessPermissions"
      height="40"
      include-groups
      include-spaces
      required />
    <span v-if="!accessPermissions.length && showAccessPermissionsGroupSuggester" class="caption mx-2 mt-n3 position-absolute error-color">
      {{ $t('siteNavigation.required.error.message') }}
    </span>
    <template v-if="showAccessPermissionsList">
      <site-navigation-node-permission-item
        v-for="(permission, index) in accessPermissions"
        :key="index"
        :permission-group="permission.group"
        :membership-type="permission.membershipType"
        @membership-type-changed="updatePermissionMembershipType(index, $event)"
        @remove-permission="$root.$emit('remove-access-permission', index)" />
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
    type: {
      type: String,
      default: 'Everyone'
    },
  },
  data() {
    return {
      allGroupsForAdmin: true,
      groupType: 'GROUP',
      userGroup: '/platform/users',
      searchOptions: {filterType: eXo.env.portal.isAdministrator && 'all' || 'member'},
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
    typeLabel() {
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
    showAccessPermissionsGroupSuggester() {
      return this.type === 'GROUP';
    },
    showAccessPermissionsList() {
      return this.showAccessPermissionsGroupSuggester;
    },
  },
  watch: {
    accessPermission() {
      if (this.accessPermission?.id) {
        const permission = {
          group: this.accessPermission,
          membershipType: '*'};
        this.$root.$emit('add-access-permission', permission);
        this.$refs.NavigationNodeAccessPermissions.emitSelectedValue({});
      }
    },
    type() {
      this.$root.$emit('change-access-permission-type', this.type);
    }
  },
  methods: {
    updatePermissionMembershipType(index, membershipType) {
      const permission = {
        index: index,
        membershipType: membershipType};
      this.$root.$emit('update-access-permission-membership-type', permission);
    }
  }
};
</script>