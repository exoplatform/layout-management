
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

export function editLayout(uiPageId, pageName, pageSiteType, pageSiteName, nodeUri, nodeSiteType, nodeSiteName) {
  return fetch(`${eXo.env.server.createPortalURL(uiPageId, 'EditAnyPage', true)}&pageName=${pageName}&pageSiteType=${pageSiteType}&pageSiteName=${pageSiteName}`, {
    method: 'GET',
    credentials: 'include',
  }).then(resp => {
    if (!resp || !resp.ok) {
      throw new Error(resp.status);
    } else {
      const targetPageUrl = `/portal${nodeSiteType === 'group' ? '/g' : ''}/${nodeSiteName.replaceAll('/', ':')}/${nodeUri}`;
      window.open(targetPageUrl, '_blank');
    }
  });
}

export function moveNode(nodeId, previousId) {
  const formData = new FormData();
  if (nodeId) {
    formData.append('nodeId', nodeId);
  }
  if (previousId) {
    formData.append('previousId', previousId);
  }

  const params = new URLSearchParams(formData).toString();
  return fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/v1/siteNavigation/node/move?${params}`, {
    credentials: 'include',
    method: 'PUT',
  }).then((resp) => {
    if (resp && resp.ok) {
      return resp.ok;
    } else {
      throw resp;
    }
  });
}