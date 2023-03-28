import layoutManagement from './components/layoutManagement.vue';

const components = {
  'layout-management': layoutManagement
};

for (const key in components) {
  Vue.component(key, components[key]);
}
