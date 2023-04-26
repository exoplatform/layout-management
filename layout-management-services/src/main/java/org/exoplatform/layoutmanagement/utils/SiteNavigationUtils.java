package org.exoplatform.layoutmanagement.utils;

import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.portal.config.UserACL;
import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.portal.mop.SiteKey;
import org.exoplatform.portal.mop.navigation.NodeData;
import org.exoplatform.portal.mop.page.PageContext;
import org.exoplatform.portal.mop.service.LayoutService;
import org.exoplatform.services.resources.ResourceBundleService;
import org.exoplatform.services.security.Identity;

import java.util.Locale;
import java.util.ResourceBundle;

public class SiteNavigationUtils {

  private static final String EDITOR_MEMBERSHIP_NAME          = "editor";

  private static final String MANAGER_MEMBERSHIP_NAME         = "manager";

  private static final String PLATFORM_WEB_CONTRIBUTORS_GROUP = "/platform/web-contributors";

  private static final String PLATFORM_ADMINISTRATORS_GROUP   = "/platform/administrators";

  private static final String EMPTY_PAGE_DEFAULT_SKELETON     =
                                                          "/eXoSkin/skin/images/themes/default/Container/default-layout.png";

  private static final String NORMAL_PAGE_DEFAULT_SKELETON    = "/eXoSkin/skin/images/themes/default/Container/ItemSelector.png";

  private static final String ANALYTICS_PAGE_DEFAULT_SKELETON = "/analytics/skin/images/analytics-layout.png";

  private SiteNavigationUtils() {
  }

  public static boolean canEditNavigation(Identity currentIdentity, NodeData nodeData) {
    SiteKey siteKey = nodeData.getSiteKey();
    LayoutService layoutService = CommonsUtils.getService(LayoutService.class);
    PortalConfig sitePortalConfig = layoutService.getPortalConfig(siteKey);
    UserACL userACL = CommonsUtils.getService(UserACL.class);
    return (currentIdentity.isMemberOf(PLATFORM_WEB_CONTRIBUTORS_GROUP, EDITOR_MEMBERSHIP_NAME)
        || currentIdentity.isMemberOf(PLATFORM_ADMINISTRATORS_GROUP, MANAGER_MEMBERSHIP_NAME))
        && (userACL.hasEditPermissionOnPortal(siteKey.getTypeName(), siteKey.getName(), sitePortalConfig.getEditPermission())
            || userACL.hasEditPermissionOnNavigation(siteKey));
  }

  public static boolean canEditPage(PageContext pageContext) {
    UserACL userACL = CommonsUtils.getService(UserACL.class);
    return userACL.hasEditPermission(pageContext);
  }

  public static boolean isAdministrator() {
    UserACL userACL = CommonsUtils.getService(UserACL.class);
    return userACL.isSuperUser() || userACL.isUserInGroup(PLATFORM_ADMINISTRATORS_GROUP);
  }

  public static String getI18NMessage(Locale userLocale, String messageKey) {
    ResourceBundleService resourceBundleService = CommonsUtils.getService(ResourceBundleService.class);
    if (userLocale == null) {
      userLocale = Locale.ENGLISH;
    }
    try {
      ResourceBundle resourceBundle =
                                    resourceBundleService.getResourceBundle(resourceBundleService.getSharedResourceBundleNames(),
                                                                            userLocale);
      String key = "UIWizardPageSelectLayoutForm.label." + messageKey;
      if (resourceBundle != null && messageKey != null && resourceBundle.containsKey(key)) {
        return resourceBundle.getString(key);
      }
      return messageKey;
    } catch (Exception e) {
      return messageKey;
    }
  }

  public static String getPageTemplateDefaultSkeleton(String templateName) {
    return switch (templateName) {
    case "empty" -> EMPTY_PAGE_DEFAULT_SKELETON;
    case "normal" -> NORMAL_PAGE_DEFAULT_SKELETON;
    case "analytics" -> ANALYTICS_PAGE_DEFAULT_SKELETON;
    default -> " ";
    };
  }

}
