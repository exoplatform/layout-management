/*
 * Copyright (C) 2023 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import SiteNavigation from './components/SiteNavigation.vue';
import SiteNavigationButton from './components/SiteNavigationButton.vue';
import SiteNavigationDrawer from './components/SiteNavigationDrawer.vue';
import SiteNavigationNodesList from './components/NodesList.vue';
import SiteNavigationNodeItem from './components/NodeItem.vue';
import SiteNavigationNodeItemMenu from './components/NodeItemMenu.vue';
import SiteNavigationNotificationAlert from './snackbar/SiteNavigationNotificationAlert.vue';
import SiteNavigationNotificationAlerts from './snackbar/SiteNavigationNotificationAlerts.vue';
import SiteNavigationNodeDrawer from './components/SiteNavigationNodeDrawer.vue';
import SiteNavigationElementDrawer from './components/SiteNavigationElementDrawer.vue';
import SiteNavigationPageElement from './components/SiteNavigationPageElement.vue';
import SiteNavigationNewPageElement from './components/SiteNavigationNewPageElement.vue';
import SiteNavigationScheduleDatePickers from './components/SiteNavigationScheduleDatePickers.vue';
import SiteNavigationExistingPageElement from './components/SiteNavigationExistingPageElement.vue';
import SiteNavigationPageSuggester from './components/SiteNavigationPageSuggester.vue';
import SiteNavigationSiteSuggester from './components/SiteNavigationSiteSuggester.vue';

const components = {
  'site-navigation': SiteNavigation,
  'site-navigation-button': SiteNavigationButton,
  'site-navigation-drawer': SiteNavigationDrawer,
  'site-navigation-nodes-list': SiteNavigationNodesList,
  'site-navigation-node-item': SiteNavigationNodeItem,
  'site-navigation-node-item-menu': SiteNavigationNodeItemMenu,
  'site-navigation-notification-alert': SiteNavigationNotificationAlert,
  'site-navigation-notification-alerts': SiteNavigationNotificationAlerts,
  'site-navigation-node-drawer': SiteNavigationNodeDrawer,
  'site-navigation-element-drawer': SiteNavigationElementDrawer,
  'site-navigation-page-element': SiteNavigationPageElement,
  'site-navigation-new-page-element': SiteNavigationNewPageElement,
  'site-navigation-schedule-date-pickers': SiteNavigationScheduleDatePickers,
  'site-navigation-existing-page-element': SiteNavigationExistingPageElement,
  'site-navigation-page-suggester': SiteNavigationPageSuggester,
  'site-navigation-site-suggester': SiteNavigationSiteSuggester,
};

for (const key in components) {
  Vue.component(key, components[key]);
}

import * as siteNavigationService from './siteNavigationService';

if (!Vue.prototype.$siteNavigationService) {
  window.Object.defineProperty(Vue.prototype, '$siteNavigationService', {
    value: siteNavigationService,
  });
}