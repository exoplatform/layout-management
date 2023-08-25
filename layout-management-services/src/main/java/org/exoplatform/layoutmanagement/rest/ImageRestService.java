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
package org.exoplatform.layoutmanagement.rest;

import static org.exoplatform.layoutmanagement.utils.ImageUtils.ATTACHMENT_TYPE;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.layoutmanagement.rest.model.ImageRestEntity;
import org.exoplatform.layoutmanagement.service.ImageService;
import org.exoplatform.layoutmanagement.utils.ImageUtils;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.security.Identity;
import org.exoplatform.social.core.service.LinkProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Path("v1/image")
@Tag(name = "v1/image", description = "Managing image")
public class ImageRestService implements ResourceContainer {

  private static final Log    LOG                = ExoLogger.getLogger(ImageRestService.class);

  private ImageService        imageService;

  private PortalContainer     portalContainer;

  private static final String DEFAULT_IMAGE_PATH = "/skin/images/default_image.png";

  public ImageRestService(PortalContainer portalContainer, ImageService imageService) {
    this.imageService = imageService;
    this.portalContainer = portalContainer;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed("administrators")
  @Operation(summary = "Retrieve image", method = "GET", description = "This retrieves the image")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Request fulfilled"),
      @ApiResponse(responseCode = "400", description = "Invalid query input"),
      @ApiResponse(responseCode = "500", description = "Internal server error"), })
  public Response getImage(@Parameter(description = "upload file id")
  @QueryParam("uploadFileId")
  String uploadFileId) {
    try {
      if (StringUtils.isBlank(uploadFileId)) {
        return Response.status(Response.Status.BAD_REQUEST).entity("uploadFileId is mandatory").build();
      }
      Identity identity = ConversationState.getCurrent().getIdentity();
      String fileId = String.valueOf(uploadFileId.equals("default") ? uploadFileId
                                                                    : imageService.getImage(identity.getUserId(), uploadFileId));
      Long lastModifiedDate = System.currentTimeMillis();
      String imageUrl = ImageUtils.buildImageUrl(fileId, identity.getUserId(), lastModifiedDate);
      ImageRestEntity imageRestEntity = new ImageRestEntity(fileId, imageUrl);
      return Response.ok().entity(imageRestEntity).build();
    } catch (Exception e) {
      LOG.error("Error when retrieving image", e);
      return Response.serverError().build();
    }
  }

  @GET
  @Path("{fileId}")
  @Produces("image/png")
  @RolesAllowed("administrators")
  @Operation(summary = "Retrieve image stream", method = "GET", description = "This retrieves image stream")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Request fulfilled"),
      @ApiResponse(responseCode = "400", description = "Invalid query input"),
      @ApiResponse(responseCode = "403", description = "Forbidden operation"),
      @ApiResponse(responseCode = "500", description = "Internal server error"), })
  public Response getImageStream(@Context
  Request request,
                                 @Parameter(description = "image file id")
                                 @PathParam("fileId")
                                 String fileId,
                                 @Parameter(description = "last modified date")
                                 @QueryParam("lastModified")
                                 String lastModified,
                                 @Parameter(description = "A mandatory valid token that is used to authorize anonymous request", required = true)
                                 @QueryParam("r")
                                 String token) {
    try {
      if (StringUtils.isBlank(fileId)) {
        return Response.status(Response.Status.BAD_REQUEST).entity("fileId is mandatory").build();
      }
      Identity identity = ConversationState.getCurrent().getIdentity();
      if (!StringUtils.isBlank(token)
          && !LinkProvider.isAttachmentTokenValid(token, fileId, identity.getUserId(), ATTACHMENT_TYPE, lastModified)) {
        LOG.warn("An anonymous user attempts to access image of portlet without a valid access token");
        return Response.status(Response.Status.FORBIDDEN).build();
      }
      InputStream stream = fileId.equals("default") ? getDefaultImageInputStream() : imageService.getImageStream(fileId);
      Response.ResponseBuilder builder = Response.ok(stream, "image/png");
      return builder.build();
    } catch (Exception e) {
      LOG.error("Error when retrieving image stream", e);
      return Response.serverError().build();
    }
  }

  private InputStream getDefaultImageInputStream() throws IOException {
    byte[] defaultImage;
    InputStream is = portalContainer.getPortalContext().getResourceAsStream(DEFAULT_IMAGE_PATH);
    if (is == null) {
      defaultImage = new byte[] {};
    } else {
      defaultImage = IOUtil.getStreamContentAsBytes(is);
    }
    return new ByteArrayInputStream(defaultImage);
  }
}
