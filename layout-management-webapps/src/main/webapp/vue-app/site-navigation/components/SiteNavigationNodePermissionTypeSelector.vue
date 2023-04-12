<template>
  <div class="text-center pe-1">
    <span class="text-capitalize clickable" @click="showMenu = true"> {{ membershipTypeLabel }}</span>
    <v-menu
      v-model="showMenu"
      transition="slide-x-reverse-transition"
      bottom
      offset-y
      left
      :close-on-content-click="false">
      <template #activator="{ on, attrs }">
        <v-btn
          icon
          small
          v-bind="attrs"
          v-on="on">
          <v-icon
            :size="16">
            mdi-chevron-down
          </v-icon>
        </v-btn>
      </template>
      <v-list
        class="pa-0">
        <v-list-item
          class="custom-list-item-min-height"
          v-for="(membershipTypeItem, index) in membershipTypes"
          :key="index">
          <v-list-item-title @click="membershipType = membershipTypeItem.name" class="clickable text-capitalize">
            <span class="text-font-size clickable">
              {{ membershipTypeItem.name }}
            </span>
          </v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu>
  </div>
</template>

<script>
export default {
  props: {
    membershipType: {
      type: String,
      default: '*',
    },
  },
  data: () => ({
    membershipTypes: [],
    showMenu: false,
  }),
  computed: {
    membershipTypeLabel() {
      if (!this.$t(`siteNavigation.label.membershipType.${this.membershipType}`).includes('siteNavigation.label')) {
        return  this.$t(`siteNavigation.label.membershipType.${this.membershipType}`);
      } else {
        return this.membershipType;
      }
    }
  },
  watch: {
    membershipType() {
      this.$emit('membership-type-changed', this.membershipType);
    }
  },
  created() {
    $(document).on('mousedown', () => {
      if (this.showMenu) {
        window.setTimeout(() => {
          this.showMenu = false;
        }, 200);
      }
    });
    this.getMembershipTypes();
  },
  methods: {
    getMembershipTypes() {
      return this.$siteNavigationService.getMembershipTypes()
        .then(membershipTypes => {
          this.membershipTypes = membershipTypes || [];
        });
    }
  }
};
</script>