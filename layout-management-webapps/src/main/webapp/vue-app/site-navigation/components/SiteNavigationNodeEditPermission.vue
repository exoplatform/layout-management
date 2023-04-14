<template>
  <div>
    <span class="font-weight-bold text-start text-color body-2">{{ $t('siteNavigation.label.whoCanEdit') }}</span>
    <exo-identity-suggester
      ref="NavigationNodeEditPermissions"
      :labels="suggesterLabels"
      v-model="permission.group"
      :group-type="groupType"
      :all-groups-for-admin="allGroupsForAdmin"
      :group-member="userGroup"
      name="editPermissions"
      height="40"
      include-groups
      include-spaces
      required />
    <span v-if="!permission.group" class="caption mt-n4 mx-2 position-absolute error-color">
      {{ $t('siteNavigation.required.error.message') }}
    </span>
    <site-navigation-node-permission-item
      v-if="permission.group && permission.group"
      :permission-group="permission.group"
      :membership-type="permission.membershipType"
      @membership-type-changed="$root.$emit('edit-permission-membership-type-changed', $event)"
      @remove-permission="$root.$emit('reset-edit-permission')" />
  </div>
</template>

<script>
export default {
  props: {
    permission: {
      type: Object,
      default: () => ({
        group: {},
        membershipType: '*'}),
    },
  },
  data: () => ({
    loading: false,
    allGroupsForAdmin: true,
    groupType: 'GROUP',
    userGroup: '/platform/users',
    searchOptions: {
      currentUser: '',
    },
  }),
  computed: {
    suggesterLabels() {
      return {
        placeholder: this.$t('siteNavigation.label.groupSuggester.placeholder'),
        noDataLabel: this.$t('siteNavigation.label.groupSuggester.noData')
      };
    }
  },
  watch: {
    'permission.group'(newVal) {
      if (newVal?.spaceId){
        this.$set(this.permission,'membershipType', '*');
      }
    }
  }
};
</script>