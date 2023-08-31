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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.social.core.service.LinkProvider;
import org.exoplatform.social.core.space.SpaceUtils;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.exoplatform.services.security.Identity;

public class ImageUtils {
  
  public static final String ATTACHMENT_TYPE         = "image";

  public static final String BASE_URL_IMAGE_REST_API = "/v1/image";

  private static final String PLATFORM_WEB_CONTRIBUTORS_GROUP = "/platform/web-contributors";

  private static final String PUBLISHER_MEMBERSHIP_NAME       = "publisher";

  private static final Log LOG = ExoLogger.getLogger(ImageUtils.class);
  
  private ImageUtils() {
  }
  
  public static String buildImageUrl(String fileId, String remoteId, Long lastModifiedDate) {
    if (remoteId == null) {
      return null;
    }

    String token = LinkProvider.generateAttachmentToken(fileId, remoteId, ATTACHMENT_TYPE, String.valueOf(lastModifiedDate));
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

  public static boolean isPublisher() {
    Identity currentIdentity = ConversationState.getCurrent().getIdentity();
    Space space = SpaceUtils.getSpaceByContext();
    if (space != null) {
      SpaceService spaceService = CommonsUtils.getService(SpaceService.class);
      return currentIdentity != null && spaceService.isMember(space, currentIdentity.getUserId())
          && (currentIdentity.isMemberOf(PLATFORM_WEB_CONTRIBUTORS_GROUP, PUBLISHER_MEMBERSHIP_NAME)
              || spaceService.isPublisher(space, currentIdentity.getUserId()));
    }
    return currentIdentity != null && currentIdentity.isMemberOf(PLATFORM_WEB_CONTRIBUTORS_GROUP, PUBLISHER_MEMBERSHIP_NAME);
  }

  public static String getBaseURLImageRest() {
    return "/" + PortalContainer.getCurrentPortalContainerName() + "/" + PortalContainer.getCurrentRestContextName()
        + BASE_URL_IMAGE_REST_API;
  }

}
