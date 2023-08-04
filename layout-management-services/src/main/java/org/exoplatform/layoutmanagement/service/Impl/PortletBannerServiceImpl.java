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
package org.exoplatform.layoutmanagement.service.Impl;

import org.exoplatform.commons.api.settings.data.Scope;
import org.exoplatform.commons.file.model.FileItem;
import org.exoplatform.commons.file.services.FileService;
import org.exoplatform.layoutmanagement.rest.model.PortletBannerEntity;
import org.exoplatform.layoutmanagement.service.PortletBannerService;
import org.exoplatform.layoutmanagement.utils.PortletBannerUtils;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.upload.UploadResource;
import org.exoplatform.upload.UploadService;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;

public class PortletBannerServiceImpl implements PortletBannerService {

  private static final Log LOG = ExoLogger.getLogger(PortletBannerServiceImpl.class);

  private FileService      fileService;

  private UploadService    uploadService;

  public PortletBannerServiceImpl(FileService fileService, UploadService uploadService) {
    this.fileService = fileService;
    this.uploadService = uploadService;
  }

  @Override
  public PortletBannerEntity updatePortletBanner(String remoteId, String uploadFileId) {
    String fileId = "default";
    if (!uploadFileId.equals("default")) {
      UploadResource uploadResource = uploadService.getUploadResource(uploadFileId);
      FileItem fileItem = null;
      try {
        fileItem = fileService.writeFile(new FileItem(null,
                                                      uploadResource.getFileName(),
                                                      uploadResource.getMimeType(),
                                                      null,
                                                      Double.doubleToLongBits(uploadResource.getUploadedSize()),
                                                      new Date(),
                                                      remoteId,
                                                      false,
                                                      new FileInputStream(uploadResource.getStoreLocation())));
      } catch (Exception ex) {
        LOG.error("Can not store banner for " + remoteId, ex);
      }
      fileId = String.valueOf(fileItem.getFileInfo().getId());
    }
    String bannerUrl = PortletBannerUtils.buildBannerUrl(fileId, PortletBannerUtils.ATTACHMENT_TYPE);
    return new PortletBannerEntity(fileId, bannerUrl);
  }

  @Override
  public InputStream getPortletBannerStream(String fileId) {
    return getImageAsStream(fileId);
  }

  public InputStream getImageAsStream(String fileId) {
    try {
      FileItem fileItem = fileService.getFile(Long.parseLong(fileId));
      return fileItem == null || fileItem.getFileInfo() == null ? null : fileItem.getAsStream();
    } catch (Exception e) {
      LOG.warn("Error retrieving image with id {}", fileId, e);
      return null;
    }

  }

}
