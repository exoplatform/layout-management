
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

export function deleteNode(nodeId, delay) {
  localStorage.setItem('deletedNode', nodeId);
  return fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/v1/siteNavigation/node/${nodeId}?delay=${delay || 0}`, {
    credentials: 'include',
    method: 'DELETE'
  }).then((resp) => {
    if (resp && !resp.ok) {
      throw new Error('Error when deleting navigation node');
    }
  });
}

export function undoDeleteNode(nodeId) {
  return fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/v1/siteNavigation/node/${nodeId}/undoDelete`, {
    method: 'POST',
    credentials: 'include',
  }).then((resp) => {
    if (resp && resp.ok) {
      localStorage.removeItem('deletedNode');
    } else {
      throw new Error('Error when undoing deleting navigation node');
    }
  });
}