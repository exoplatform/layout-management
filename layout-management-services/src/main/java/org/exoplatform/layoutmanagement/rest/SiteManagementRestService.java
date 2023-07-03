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

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gatein.api.Portal;
import org.gatein.api.common.Filter;
import org.gatein.api.site.Site;
import org.gatein.api.site.SiteQuery;
import org.gatein.api.site.SiteType;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Path("v1/sites")
@Tag(name = "v1/sites", description = "Managing sites")
public class SiteManagementRestService implements ResourceContainer {

  private static final Log LOG = ExoLogger.getLogger(SiteManagementRestService.class);

  private Portal           portal;

  public SiteManagementRestService(Portal portal) {
    this.portal = portal;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed("users")
  @Operation(summary = "Retrieve sites", method = "GET", description = "This retrieves sites")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Request fulfilled"),
      @ApiResponse(responseCode = "500", description = "Internal server error"), })
  public Response getSites() {
    try {
      Filter<Site> filter = new Filter<>() {
        @Override
        public boolean accept(Site site) {
          return !site.getName().startsWith("/spaces/");
        }
      };
      SiteQuery pageQuery = new SiteQuery.Builder().withSiteTypes(SiteType.SITE, SiteType.SPACE).withFilter(filter).build();
      List<Site> sites = portal.findSites(pageQuery);
      return Response.ok().entity(EntityBuilder.toSiteRestEntities(sites)).build();
    } catch (Exception e) {
      LOG.error("Error when retrieving sites", e);
      return Response.serverError().build();
    }
  }
}
