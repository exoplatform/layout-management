package org.exoplatform.layoutmanagement.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.service.LinkProvider;

public class ImageUtils {
  
  public static final String ATTACHMENT_TYPE         = "image";

  public static final String BASE_URL_IMAGE_REST_API = "/v1/image";
  
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

  public static String getBaseURLImageRest() {
    return "/" + PortalContainer.getCurrentPortalContainerName() + "/" + PortalContainer.getCurrentRestContextName()
        + BASE_URL_IMAGE_REST_API;
  }

}
