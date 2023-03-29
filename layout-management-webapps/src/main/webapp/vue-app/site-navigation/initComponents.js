import SiteNavigation from './components/SiteNavigation.vue';

const components = {
  'site-navigation': SiteNavigation
};

for (const key in components) {
  Vue.component(key, components[key]);
}
