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
import ImagesPortlet from './components/ImagesPortlet.vue';

const components = {
  'images-portlet': ImagesPortlet,
};


for (const key in components) {
  Vue.component(key, components[key]);
}

import * as imagesService from './imagesService.js';

if (!Vue.prototype.$imagesService) {
  window.Object.defineProperty(Vue.prototype, '$imagesService', {
    value: imagesService,
  });
}
