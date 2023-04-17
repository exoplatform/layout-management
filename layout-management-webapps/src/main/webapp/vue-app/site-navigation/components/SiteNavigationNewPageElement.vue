<template>
  <div>
    <span class="font-weight-bold text-start text-color body-2 mt-8">{{ $t('siteNavigation.label.pageTemplate') }}</span>
    <v-select
      v-model="pageTemplate"
      :items="pageTemplates"
      item-text="text"
      item-value="value"
      dense
      class="caption pt-1 mb-5"
      outlined />
    <v-img :src="templateSkeleton" />
  </div>
</template>

<script>
export default {
  data() {
    return {
      pageTemplate: 'normalPage',
    };
  },
  computed: {
    pageTemplates() {
      return [
        {
          text: this.$t('siteNavigation.label.normalPage'),
          value: 'normalPage',
        },
        {
          text: this.$t('siteNavigation.label.blankPage'),
          value: 'blankPage',
        },
        {
          text: this.$t('siteNavigation.label.analytics'),
          value: 'analytics',
        },
      ];
    },
    templateSkeleton() {
      switch (this.pageTemplate) {
      case 'normalPage': return '/layout-management/images/NormalPageSkeleton.png';
      case 'blankPage': return '/layout-management/images/BlankPageSkeleton.png';
      case 'analytics': return '/layout-management/images/AnalyticsPageSkeleton.png';
      }
      return '';
    },
  },
  watch: {
    pageTemplate() {
      this.$root.$emit('page-template-changed', this.pageTemplate);
    }
  }
};
</script>
