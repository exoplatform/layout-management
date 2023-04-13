
export function getNavigationNodes(siteType, siteName, includeGlobal, expandPageDetails) {
  const formData = new FormData();
  if (siteName) {
    formData.append('siteName', siteName);
  }
  formData.append('includeGlobal', includeGlobal);

  formData.append('expandPageDetails', expandPageDetails);

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
      window.location.href = targetPageUrl;
    }
  });
}

export function moveNode(nodeId, previousNodeId) {
  const formData = new FormData();
  if (nodeId) {
    formData.append('nodeId', nodeId);
  }
  if (previousNodeId) {
    formData.append('previousNodeId', previousNodeId);
  }

  const params = new URLSearchParams(formData).toString();
  return fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/v1/siteNavigation/node/move?${params}`, {
    credentials: 'include',
    method: 'PATCH',
  }).then((resp) => {
    if (resp && resp.ok) {
      return resp.ok;
    } else {
      throw resp;
    }
  });
}

export function createNode(parentNodeId, previousNodeId, nodeLabel, nodeId, isVisible) {
  const formData = new FormData();
  if (parentNodeId) {
    formData.append('parentNodeId', parentNodeId);
  }
  if (previousNodeId) {
    formData.append('previousNodeId', previousNodeId);
  }
  if (nodeLabel) {
    formData.append('nodeLabel', nodeLabel);
  }
  if (nodeId) {
    formData.append('nodeId', nodeId);
  }
  formData.append('isVisible', isVisible);

  const params = new URLSearchParams(formData).toString();
  return fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/v1/siteNavigation?${params}`, {
    credentials: 'include',
    method: 'POST',
  }).then((resp) => {
    if (resp && resp.ok) {
      return resp.ok;
    } else {
      throw resp;
    }
  });
}

export function updateNodePagePermission(pageRef, editPermission, accessPermissions) {

  const formData = new FormData();

  formData.append('editPermission', editPermission);

  formData.append('accessPermissions', accessPermissions);

  const params = new URLSearchParams(formData).toString();

  return fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/v1/siteNavigation/page/permissions/${pageRef}?${params}`, {
    method: 'PATCH',
    credentials: 'include',
  }).then((resp) => {
    if (resp && !resp.ok) {
      throw new Error(resp.status);
    }
  });
}

export function getMembershipTypes() {
  return fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/v1/membershipTypes`, {
    method: 'GET',
    credentials: 'include',
  }).then(resp => {
    if (!resp || !resp.ok) {
      throw new Error('Error when retrieving membership types');
    } else {
      return resp.json();
    }
  });
}
