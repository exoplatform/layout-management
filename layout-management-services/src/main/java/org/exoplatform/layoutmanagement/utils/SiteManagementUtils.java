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

import org.apache.commons.lang.StringUtils;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.portal.config.UserACL;
import org.exoplatform.portal.config.UserPortalConfigService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.web.security.codec.CodecInitializer;
import org.exoplatform.web.security.security.TokenServiceInitializationException;
import org.gatein.api.Util;
import org.gatein.api.site.Site;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SiteManagementUtils {

  private static final Log LOG = ExoLogger.getLogger(SiteManagementUtils.class);

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

  public static final String ATTACHMENT_TYPE         = "banner";

  public static final String BASE_URL_IMAGE_REST_API = "/v1/image";

  public static String buildImageUrl(String fileId, String remoteId, Long lastModifiedDate) {
    if (remoteId == null) {
      return null;
    }

    String token = generateAttachmentToken(fileId, remoteId, ATTACHMENT_TYPE, String.valueOf(lastModifiedDate));
    if (org.apache.commons.lang.StringUtils.isNotBlank(token)) {
      try {
        token = URLEncoder.encode(token, "UTF8");
      } catch (UnsupportedEncodingException e) {
        LOG.warn("Error encoding token", e);
        token = org.apache.commons.lang.StringUtils.EMPTY;
      }
    }
    return new StringBuilder(getBaseURLImageRest()).append("/")
                                                   .append(fileId)
                                                   .append("?lastModified=")
                                                   .append(lastModifiedDate)
                                                   .append("&r=")
                                                   .append(token)
                                                   .toString();
  }

  public static String getBaseURLImageRest() {
    return "/" + PortalContainer.getCurrentPortalContainerName() + "/" + PortalContainer.getCurrentRestContextName()
        + BASE_URL_IMAGE_REST_API;
  }

  public static String generateAttachmentToken(String fileId, String remoteId, String attachmentType, String lastModifiedDate) {
    String token = null;
    CodecInitializer codecInitializer = ExoContainerContext.getService(CodecInitializer.class);
    if (codecInitializer == null) {
      LOG.debug("Can't find an instance of CodecInitializer, an empty token will be generated");
      token = org.apache.commons.lang.StringUtils.EMPTY;
    } else {
      try {
        String tokenPlain = attachmentType + ":" + fileId + ":" + remoteId + ":" + lastModifiedDate;
        token = codecInitializer.getCodec().encode(tokenPlain);
      } catch (TokenServiceInitializationException e) {
        LOG.warn("Error generating token of {} for program {}. An empty token will be used", attachmentType, fileId, e);
        token = org.apache.commons.lang.StringUtils.EMPTY;
      }
    }
    return token;
  }

  public static boolean isAttachmentTokenValid(String token,
                                               String fileId,
                                               String remoteId,
                                               String attachmentType,
                                               String lastModifiedDate) {
    if (StringUtils.isBlank(token)) {
      LOG.warn("An empty token is used for portlet banner");
      return false;
    }
    String validToken = generateAttachmentToken(fileId, remoteId, attachmentType, lastModifiedDate);
    return StringUtils.equals(validToken, token);
  }
}
