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

  formData.append('editPermission', editPermission);

  formData.append('accessPermissions', accessPermissions);

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