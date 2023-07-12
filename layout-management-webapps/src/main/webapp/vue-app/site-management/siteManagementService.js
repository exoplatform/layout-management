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


export function getSites() {
  return fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/v1/sites`, {
    method: 'GET',
    credentials: 'include',
    headers: {
      'Content-Type': 'application/json'
    },
  }).then(resp => {
    if (resp || resp.ok) {
      return resp.json();
    } else {
      throw new Error('Error when retrieving sites');
    }
  });
}

export function deleteSite(siteType, siteName) {
  const formData = new FormData();
  formData.append('siteName', siteName);
  formData.append('siteType', siteType);
  const params = new URLSearchParams(formData).toString();
  return fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/v1/sites?${params}`, {
    method: 'DELETE',
    credentials: 'include',
  }).then(resp => {
    if (!resp || !resp.ok) {
      throw new Error('Error when deleting site');

    }
  });
}
  
export function editSiteLayout(portalName) {
  const params = [
    {name: 'portalName', value: portalName}
  ] ;
  return fetch(`${eXo.env.server.createPortalURL('UIWorkingWorkspace', 'EditPortalLayout', true, params)}`, {
    method: 'GET',
    credentials: 'include',
  }).then(resp => {
    if (!resp || !resp.ok) {
      throw new Error(resp.status);
    }
    else {
      window.location.reload();
    }
  });
}

export function updateSite(siteName, siteType, siteLabel, siteDescription) {
  const formData = new FormData();
  formData.append('siteName', siteName);
  formData.append('siteType', siteType);
  formData.append('siteLabel', siteLabel);
  formData.append('siteDescription', siteDescription);

  const params = new URLSearchParams(formData).toString();
  return fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/v1/sites?${params}`, {
    credentials: 'include',
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
  }).then((resp) => {
    if (resp?.ok) {
      return resp.ok;
    } else {
      throw new Error('Error when updating site');
    }
  });
}


