package org.exoplatform.layoutmanagement.service;

import org.exoplatform.portal.mop.storage.NavigationStorage;

public class LayoutManagementServiceImpl implements LayoutManagementService {

  private NavigationStorage navigationStorage;

  public LayoutManagementServiceImpl(NavigationStorage navigationStorage) {
    this.navigationStorage = navigationStorage;
  }

  @Override
  public void removeNode(Long nodeId) {
    navigationStorage.destroyNode(nodeId);
  }

}
