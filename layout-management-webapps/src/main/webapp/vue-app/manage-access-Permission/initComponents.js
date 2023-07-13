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
import ManageAccessDrawer from './components/ManageAccessDrawer.vue';
import ManageEditPermission from './components/ManageEditPermission.vue';
import ManageAccessPermission from './components/ManageAccessPermission.vue';
import ManagePermissionItem from './components/ManagePermissionItem.vue';
import PermissionTypeSelector from './components/PermissionTypeSelector.vue';

const components = {
  'manage-access-drawer': ManageAccessDrawer,
  'manage-edit-permission': ManageEditPermission,
  'manage-access-permission': ManageAccessPermission,
  'manage-permission-item': ManagePermissionItem,
  'permission-type-selector': PermissionTypeSelector,
};

for (const key in components) {
  Vue.component(key, components[key]);
}

import * as ManageAccessService from './ManageAccessService.js';

window.Object.defineProperty(Vue.prototype, '$ManageAccessService', {
  value: ManageAccessService,
});