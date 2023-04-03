
export function getNavigationNodes(siteType, siteName, includeGlobal) {
  const formData = new FormData();
  if (siteName) {
    formData.append('siteName', siteName);
  }
  formData.append('includeGlobal', includeGlobal);

  const params = new URLSearchParams(formData).toString();
  return fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/v1/navigations/${siteType || 'portal'}?${params}`, {
    method: 'GET',
    credentials: 'include',
  }).then(resp => {
    if (!resp || !resp.ok) {
      throw new Error(resp.status);
    } else {
      return resp.json();
    }
  });
}