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

import java.util.List;
import java.util.Locale;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.gatein.api.Portal;
import org.gatein.api.Util;
import org.gatein.api.site.Site;
import org.gatein.api.site.SiteId;

import org.exoplatform.layoutmanagement.utils.SiteManagementUtils;
import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.portal.mop.SiteKey;
import org.exoplatform.portal.mop.service.LayoutService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.http.PATCH;
import org.exoplatform.services.rest.resource.ResourceContainer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.exoplatform.social.rest.api.EntityBuilder;

@Path("v1/sites")
@Tag(name = "v1/sites", description = "Managing sites")
public class SiteManagementRestService implements ResourceContainer {

  private static final Log LOG = ExoLogger.getLogger(SiteManagementRestService.class);

  private Portal           portal;

  private LayoutService    layoutService;

  public SiteManagementRestService(Portal portal, LayoutService layoutService) {
    this.portal = portal;
    this.layoutService = layoutService;
  }

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed("administrators")
  @Operation(summary = "Delete a site", method = "GET", description = "This deletes the given site")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Request fulfilled"),
      @ApiResponse(responseCode = "500", description = "Internal server error"), })
  public Response deleteSite(@Parameter(description = "site type")
  @QueryParam("siteType")
  String siteType,
                             @Parameter(description = "site name")
                             @QueryParam("siteName")
                             String siteName) {
    try {
      SiteId siteId = Util.from(new SiteKey(siteType, siteName));
      Site site = portal.getSite(siteId);
      if (!SiteManagementUtils.canEditSite(site)) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      portal.removeSite(siteId);
      return Response.ok().build();
    } catch (Exception e) {
      LOG.error("Error when deleting the site with name {} and type {}", siteName, siteType, e);
      return Response.serverError().build();
    }
  }

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed("administrators")
  @Operation(summary = "update a site", method = "PUT", description = "This updates the given site")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Request fulfilled"),
      @ApiResponse(responseCode = "500", description = "Internal server error"), })
  public Response updateSite(@Context
  HttpServletRequest request,
                             @Parameter(description = "site type")
                             @QueryParam("siteType")
                             String siteType,
                             @Parameter(description = "site name")
                             @QueryParam("siteName")
                             String siteName,
                             @Parameter(description = "site Label")
                             @QueryParam("siteLabel")
                             String siteLabel,
                             @Parameter(description = "site description")
                             @QueryParam("siteDescription")
                             String siteDescription,
                             @Parameter(description = "site displayed in meta site")
                             @QueryParam("displayed")
                             boolean displayed,
                             @Parameter(description = "site display order")
                             @QueryParam("displayOrder")
                             int displayOrder,
                             @Parameter(description = "Used to retrieve the site label and description in the requested language")
                             @QueryParam("lang")
                             String lang) {
    try {
      SiteId siteId = Util.from(new SiteKey(siteType, siteName));
      Site site = portal.getSite(siteId);
      if (!SiteManagementUtils.canEditSite(site)) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      PortalConfig portalConfig = layoutService.getPortalConfig(new SiteKey(siteType, siteName));
      portalConfig.setDescription(siteDescription);
      portalConfig.setLabel(siteLabel);
      portalConfig.setDisplayed(displayed);
      portalConfig.setDisplayOrder(displayed ? displayOrder : 0);
      layoutService.save(portalConfig);
      return Response.ok(EntityBuilder.buildSiteEntity(portalConfig, request, false, false, getLocale(lang))).build();
    } catch (Exception e) {
      LOG.error("Error when updating the site with name {} and type {}", siteName, siteType, e);
      return Response.serverError().build();
    }
  }

  @Path("/permissions")
  @PATCH
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed("administrators")
  @Operation(summary = "Update a page access and edit permission", method = "PATCH", description = "This updates the given page access and edit permission")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Page permissions updated"),
      @ApiResponse(responseCode = "400", description = "Invalid query input"),
      @ApiResponse(responseCode = "404", description = "Page not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized operation"),
      @ApiResponse(responseCode = "500", description = "Internal server error"), })
  public Response updateSitePermissions(@Context
  HttpServletRequest request,
                                        @Parameter(description = "Site type", required = true)
                                        @QueryParam("siteType")
                                        String siteType,
                                        @Parameter(description = "Site name", required = true)
                                        @QueryParam("siteName")
                                        String siteName,
                                        @Parameter(description = "Site new edit permission", required = true)
                                        @QueryParam("editPermission")
                                        String editPermission,
                                        @Parameter(description = "Site new access permissions", required = true)
                                        @QueryParam("accessPermissions")
                                        String accessPermissions,
                                        @Parameter(description = "Used to retrieve the site label and description in the requested language")
                                        @QueryParam("lang")
                                        String lang) {
    try {
      if (StringUtils.isBlank(siteName) || StringUtils.isBlank(siteType)) {
        return Response.status(Response.Status.BAD_REQUEST).entity("params are mandatory").build();
      }
      SiteId siteId = Util.from(new SiteKey(siteType, siteName));
      Site site = portal.getSite(siteId);
      if (site == null) {
        return Response.status(Response.Status.NOT_FOUND).build();
      }
      if (!SiteManagementUtils.canEditSite(site)) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      if (!StringUtils.isBlank(editPermission)) {
        site.setEditPermission(Util.from(editPermission));
      }
      if (!StringUtils.isBlank(accessPermissions)) {
        List<String> accessPermissionsList = List.of(accessPermissions.split(",")).stream().distinct().toList();
        site.setAccessPermission(Util.from(accessPermissionsList));
      }
      portal.saveSite(site);
      PortalConfig portalConfig = layoutService.getPortalConfig(new SiteKey(siteType, siteName));
      return Response.ok(EntityBuilder.buildSiteEntity(portalConfig, request, false, false, getLocale(lang))).build();
    } catch (Exception e) {
      LOG.error("Error when updating site permissions with name {} and type {}", siteName, siteType, e);
      return Response.serverError().build();
    }
  }
    private Locale getLocale(String lang) {
        return StringUtils.isBlank(lang) ? null : Locale.forLanguageTag(lang);
    }
}
