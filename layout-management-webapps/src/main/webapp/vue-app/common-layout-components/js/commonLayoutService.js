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
export function updateNodePagePermission(pageRef, editPermission, accessPermissions) {
  const formData = new FormData();
  formData.append('pageRef', pageRef);
  formData.append('editPermission', editPermission);
  formData.append('accessPermissions', accessPermissions);
  const params = new URLSearchParams(formData).toString();
  return fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/v1/siteNavigation/page/permissions?${params}`, {
    method: 'PATCH',
    credentials: 'include',
  }).then((resp) => {
    if (resp && !resp.ok) {
      throw new Error(resp.status);
    }
  });
}


export function updateSitePermissions(siteType, siteName, editPermission, accessPermissions) {
  const formData = new FormData();
  formData.append('siteType', siteType);
  formData.append('siteName', siteName);
  if (editPermission) {
    formData.append('editPermission', editPermission);
  }
  if (accessPermissions) {
    formData.append('accessPermissions', accessPermissions);
  }
  formData.append('lang', eXo.env.portal.language);

  const params = new URLSearchParams(formData).toString();

  return fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/v1/sites/permissions?${params}`, {
    method: 'PATCH',
    credentials: 'include',
  }).then((resp) => {
    if (resp && !resp.ok) {
      throw new Error(resp.status);
    }
  });
}