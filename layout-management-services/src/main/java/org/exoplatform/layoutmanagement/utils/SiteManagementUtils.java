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
package org.exoplatform.layoutmanagement.utils;

import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.portal.config.UserACL;
import org.exoplatform.portal.config.UserPortalConfigService;
import org.gatein.api.Util;
import org.gatein.api.site.Site;

public class SiteManagementUtils {

  private SiteManagementUtils() {
  }

  public static boolean canEditSite(Site site) {
    UserACL userACL = CommonsUtils.getService(UserACL.class);
    return userACL.hasPermission(Util.from(site.getEditPermission())[0]);
  }

  public static boolean isDefaultSite(String siteName) {
    UserPortalConfigService portalConfig = CommonsUtils.getService(UserPortalConfigService.class);
    return portalConfig.getDefaultPortal().equals(siteName);
  }
}