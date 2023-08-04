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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.layoutmanagement.rest.model.PortletBannerEntity;
import org.exoplatform.layoutmanagement.service.PortletBannerService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.security.Identity;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.exoplatform.layoutmanagement.utils.PortletBannerUtils.ATTACHMENT_TYPE;
import static org.exoplatform.layoutmanagement.utils.PortletBannerUtils.isAttachmentTokenValid;

@Path("v1/portletBanner")
@Tag(name = "v1/portletBanner", description = "Managing portlet Banner")
public class PortletBannerRestService implements ResourceContainer {

  private static final Log     LOG                 = ExoLogger.getLogger(PortletBannerRestService.class);

  private static final String  DEFAULT_BANNER_PATH = "/skin/images/portlet_default_banner.png";

  private PortletBannerService portletBannerService;

  private PortalContainer      portalContainer;

  public byte[]                defaultBanner       = null;                                                // NOSONAR

  public PortletBannerRestService(PortletBannerService portletBannerService, PortalContainer portalContainer) {
    this.portletBannerService = portletBannerService;
    this.portalContainer = portalContainer;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed("administrators")
  @Operation(summary = "Retrieve portlet banner", method = "PUT", description = "This retrieves the portlet banner")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Request fulfilled"),
      @ApiResponse(responseCode = "400", description = "Invalid query input"),
      @ApiResponse(responseCode = "500", description = "Internal server error"), })
  public Response updatePortletBanner(@Parameter(description = "upload file id")
  @QueryParam("uploadFileId")
  String uploadFileId) {
    try {
      if (StringUtils.isBlank(uploadFileId)) {
        return Response.status(Response.Status.BAD_REQUEST).entity("uploadFileId is mandatory").build();
      }
      Identity identity = ConversationState.getCurrent().getIdentity();
      String remoteId = identity.getUserId();
      PortletBannerEntity portletBannerEntity = portletBannerService.updatePortletBanner(remoteId, uploadFileId);
      return Response.ok().entity(portletBannerEntity).build();
    } catch (Exception e) {
      LOG.error("Error when retrieving portlet banner", e);
      return Response.serverError().build();
    }
  }

  @GET
  @Path("{fileId}/banner")
  @Produces("image/png")
  @RolesAllowed("administrators")
  @Operation(summary = "Retrieve portlet banner stream", method = "PUT", description = "This retrieves portlet banner stream")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Request fulfilled"),
      @ApiResponse(responseCode = "400", description = "Invalid query input"),
      @ApiResponse(responseCode = "403", description = "Forbidden operation"),
      @ApiResponse(responseCode = "500", description = "Internal server error"), })
  public Response getPortletBannerStream(@Context
  Request request,
                                         @Parameter(description = "portlet reference")
                                         @PathParam("fileId")
                                         String fileId,
                                         @Parameter(description = "A mandatory valid token that is used to authorize anonymous request", required = true)
                                         @QueryParam("r")
                                         String token) {
    try {
      if (StringUtils.isBlank(fileId)) {
        return Response.status(Response.Status.BAD_REQUEST).entity("fileId is mandatory").build();
      }
      if (!StringUtils.isBlank(token) && !isAttachmentTokenValid(token, fileId, ATTACHMENT_TYPE)) {
        LOG.warn("An anonymous user attempts to access banner of portlet without a valid access token");
        return Response.status(Response.Status.FORBIDDEN).build();
      }
      InputStream stream = portletBannerService.getPortletBannerStream(fileId);
      Response.ResponseBuilder builder = Response.ok(stream, "image/png");
      return builder.build();
    } catch (Exception e) {
      LOG.error("Error when retrieving portlet banner stream", e);
      return Response.serverError().build();
    }

  }

  @GET
  @Path("default/banner")
  @Produces("image/png")
  @RolesAllowed("administrators")
  @Operation(summary = "Retrieve default portlet banner stream", method = "PUT", description = "This retrieves default portlet banner stream")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Request fulfilled"),
      @ApiResponse(responseCode = "400", description = "Invalid query input"),
      @ApiResponse(responseCode = "403", description = "Forbidden operation"),
      @ApiResponse(responseCode = "500", description = "Internal server error"), })
  public Response getDefaultPortletBannerStream(@Context
  Request request,
                                                @Parameter(description = "A mandatory valid token that is used to authorize anonymous request", required = true)
                                                @QueryParam("r")
                                                String token) {
    try {
      if (!StringUtils.isBlank(token) && !isAttachmentTokenValid(token, "default", ATTACHMENT_TYPE)) {
        LOG.warn("An anonymous user attempts to access banner of portlet without a valid access token");
        return Response.status(Response.Status.FORBIDDEN).build();
      }
      InputStream stream = getDefaultBannerInputStream();
      Response.ResponseBuilder builder = Response.ok(stream, "image/png");
      return builder.build();
    } catch (Exception e) {
      LOG.error("Error when retrieving a default portlet banner", e);
      return Response.serverError().build();
    }

  }

  private InputStream getDefaultBannerInputStream() throws IOException {
    if (defaultBanner == null) {
      InputStream is = portalContainer.getPortalContext().getResourceAsStream(DEFAULT_BANNER_PATH);
      if (is == null) {
        defaultBanner = new byte[] {};
      } else {
        defaultBanner = IOUtil.getStreamContentAsBytes(is);
      }
    }
    return new ByteArrayInputStream(defaultBanner);
  }
}
