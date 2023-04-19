<template>
  <div>
    <span class="font-weight-bold text-start text-color body-2 mt-8">{{ $t('siteNavigation.label.pageTemplate') }}</span>
    <v-select
      v-model="pageTemplate"
      :items="pageTemplates"
      item-text="value"
      dense
      class="caption pt-1 mb-5"
      outlined >
      <template slot="item" slot-scope="data">
        {{ $t(`siteNavigation.label.${data.item.value}`) }}
      </template></v-select>
    <v-img
      :src="templateSkeleton"
      class="align-center "
      max-height="350"
      max-width="500" />
  </div>
</template>

<script>
export default {
  data() {
    return {
      pageTemplate: 'empty',
      pageTemplates: [],
    };
  },
  computed: {
    templateSkeleton() {
      switch (this.pageTemplate) {
      case 'normal': return '/eXoSkin/skin/images/themes/default/Container/ItemSelector.png';
      case 'empty': return '/eXoSkin/skin/images/themes/default/Container/default-layout.png';
      case 'analytics': return '/analytics/skin/images/analytics-layout.png';
      }
      return '';
    },
  },
  created() {
    this.getPageTemplates();
  },
  methods: {
    getPageTemplates(){
      return this.$siteNavigationService.getPageTemplateCategories()
        .then(pageTemplates => {
          this.pageTemplates = pageTemplates[0]?.selectItemOptions || [];
        });
    }
  }
};
</script>
