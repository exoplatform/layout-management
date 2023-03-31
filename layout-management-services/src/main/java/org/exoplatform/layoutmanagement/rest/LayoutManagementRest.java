package org.exoplatform.layoutmanagement.rest;

import io.swagger.v3.oas.annotations.Operation;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.exoplatform.layoutmanagement.service.LayoutManagementService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.picocontainer.Startable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Path("v1/layoutManagement")
@Tag(name = "v1/layoutManagement", description = "Managing layout")
public class LayoutManagementRest implements ResourceContainer, Startable {
  private static final Log         LOG = ExoLogger.getLogger(LayoutManagementRest.class);

  private LayoutManagementService  layoutManagementService;

  private ScheduledExecutorService scheduledExecutor;

  public LayoutManagementRest(LayoutManagementService layoutManagementService) {
    this.layoutManagementService = layoutManagementService;
  }

  @DELETE
  @Path("{nodeId}")
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed("users")
  @Operation(summary = "Delete Node ", method = "DELETE", description = "This deletes the node")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Node deleted"),
      @ApiResponse(responseCode = "400", description = "Invalid query input"),
      @ApiResponse(responseCode = "500", description = "Internal server error") })
  public Response removeNode(@Context
  HttpServletRequest request,
                             @Parameter(description = "Node id", required = true)
                             @PathParam("nodeId")
                             Long nodeId) {
    if (nodeId == null) {
      return Response.status(Response.Status.BAD_REQUEST).entity("Node id is mandatory").build();
    }
    try {
      layoutManagementService.removeNode(nodeId);
      return Response.ok().build();
    } catch (Exception e) {
      LOG.error("Error while deleting a node", e);
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
  }

  @Override
  public void start() {
    scheduledExecutor = Executors.newScheduledThreadPool(1);
  }

  @Override
  public void stop() {
    if (scheduledExecutor != null) {
      scheduledExecutor.shutdown();
    }
  }
}
