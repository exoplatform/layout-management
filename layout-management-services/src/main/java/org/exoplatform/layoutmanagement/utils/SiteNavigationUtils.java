package org.exoplatform.layoutmanagement.utils;
import org.exoplatform.services.security.Identity;
public class SiteNavigationUtils {

  private SiteNavigationUtils() {
  }

  private static final String EDITOR_MEMBERSHIP_NAME          = "editor";

  private static final String MANAGER_MEMBERSHIP_NAME         = "manager";

  private static final String PLATFORM_WEB_CONTRIBUTORS_GROUP = "/platform/web-contributors";

  private static final String PLATFORM_ADMINISTRATORS_GROUP   = "/platform/administrators";

  public static boolean canEditNavigation(Identity currentIdentity) {
    return currentIdentity.isMemberOf(PLATFORM_WEB_CONTRIBUTORS_GROUP, EDITOR_MEMBERSHIP_NAME)
        || currentIdentity.isMemberOf(PLATFORM_ADMINISTRATORS_GROUP, MANAGER_MEMBERSHIP_NAME);
  }
}
