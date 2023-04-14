
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
import org.apache.commons.lang.StringUtils;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.layoutmanagement.utils.SiteNavigationUtils;
import org.exoplatform.portal.mop.Visibility;
import org.exoplatform.portal.mop.navigation.NodeData;
import org.exoplatform.portal.mop.navigation.NodeState;
import org.exoplatform.portal.mop.page.PageContext;
import org.exoplatform.portal.mop.page.PageKey;
import org.exoplatform.portal.mop.page.PageState;
import org.exoplatform.portal.mop.service.LayoutService;
import org.exoplatform.portal.mop.service.NavigationService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.http.PATCH;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.services.security.ConversationState;
import org.picocontainer.Startable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.exoplatform.services.security.Identity;

@Path("v1/siteNavigation")
@Tag(name = "v1/siteNavigation", description = "Managing site navigation")
public class SiteNavigationRestService implements ResourceContainer, Startable {
  private static final Log         LOG                         = ExoLogger.getLogger(SiteNavigationRestService.class);

  private final NavigationService  navigationService;

  private ScheduledExecutorService scheduledExecutor;

  private final PortalContainer    container;

  private LayoutService            layoutService;

  private final Map<Long, String>  navigationNodeToDeleteQueue = new HashMap<>();

  public SiteNavigationRestService(NavigationService navigationService, PortalContainer container, LayoutService layoutService) {
    this.navigationService = navigationService;
    this.container = container;
    this.layoutService = layoutService;
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed("users")
  @Operation(summary = "Create a navigation node", method = "POST", description = "This creates the navigation node")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "navigation node created"),
      @ApiResponse(responseCode = "400", description = "Invalid query input"),
      @ApiResponse(responseCode = "404", description = "Node not found"),
      @ApiResponse(responseCode = "401", description = "User not authorized to create the navigation node"),
      @ApiResponse(responseCode = "500", description = "Internal server error") })
  public Response createNode(@Parameter(description = "navigation node id")
  @QueryParam("parentNodeId")
  Long parentNodeId,
                             @Parameter(description = "previous node id")
                             @QueryParam("previousNodeId")
                             Long previousNodeId,
                             @Parameter(description = "node label")
                             @QueryParam("nodeLabel")
                             String nodeLabel,
                             @Parameter(description = "node id")
                             @QueryParam("nodeId")
                             String nodeId,
                             @Parameter(description = "isVisible")
                             @QueryParam("isVisible")
                             boolean isVisible) {
    if (parentNodeId == null || StringUtils.isBlank(nodeLabel) || StringUtils.isBlank(nodeId)) {
      return Response.status(Response.Status.BAD_REQUEST).entity("params are mandatory").build();
    }
    try {
      NodeData parentNodeData = navigationService.getNodeById(parentNodeId);
      if (parentNodeData == null) {
        return Response.status(Response.Status.NOT_FOUND).entity("Node data with parent node id is not found").build();
      }
      Identity currentIdentity = ConversationState.getCurrent().getIdentity();
      if (!SiteNavigationUtils.canEditNavigation(currentIdentity, parentNodeData)) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      NodeState nodeState;
      nodeState = new NodeState(nodeLabel, null, -1, -1, isVisible ? Visibility.DISPLAYED : Visibility.HIDDEN, null, null);
      navigationService.createNode(parentNodeId, previousNodeId, nodeId, nodeState);
      return Response.ok().build();
    } catch (Exception e) {
      LOG.error("Error when creating a new navigation node", e);
      return Response.serverError().entity(e.getMessage()).build();
    }
  }

  @DELETE
  @Path("node/{nodeId}")
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed("users")
  @Operation(summary = "Delete Node ", method = "DELETE", description = "This deletes the navigation node")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "navigation node deleted"),
      @ApiResponse(responseCode = "400", description = "Invalid query input"),
      @ApiResponse(responseCode = "404", description = "Node not found"),
      @ApiResponse(responseCode = "401", description = "User not authorized to delete the navigation node"),
      @ApiResponse(responseCode = "500", description = "Internal server error") })
  public Response deleteNode(@Context
  HttpServletRequest request,
                             @Parameter(description = "Node id", required = true)
                             @PathParam("nodeId")
                             Long nodeId,
                             @Parameter(description = "Time to effectively delete navigation node", required = false)
                             @QueryParam("delay")
                             long delay) {
    try {
      if (nodeId == null) {
        return Response.status(Response.Status.BAD_REQUEST).entity("Node id is mandatory").build();
      }
      NodeData nodeData = navigationService.getNodeById(nodeId);
      if (nodeData == null) {
        return Response.status(Response.Status.NOT_FOUND).entity("Node data with node id is not found").build();
      }
      Identity currentIdentity = ConversationState.getCurrent().getIdentity();
      if (!SiteNavigationUtils.canEditNavigation(currentIdentity, nodeData)) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }

      if (delay > 0) {
        navigationNodeToDeleteQueue.put(nodeId, currentIdentity.getUserId());
        scheduledExecutor.schedule(() -> {
          if (navigationNodeToDeleteQueue.containsKey(nodeId)) {
            ExoContainerContext.setCurrentContainer(container);
            RequestLifeCycle.begin(container);
            try {
              navigationNodeToDeleteQueue.remove(nodeId);
              navigationService.deleteNode(nodeId);
            } catch (Exception e) {
              LOG.warn("Error when deleting the navigation node with id {}", nodeId, e);
            } finally {
              RequestLifeCycle.end();
            }
          }
        }, delay, TimeUnit.SECONDS);
      } else {
        navigationNodeToDeleteQueue.remove(nodeId);
        navigationService.deleteNode(nodeId);
      }
      return Response.ok().build();
    } catch (Exception e) {
      LOG.error("Error when deleting the navigation node with id {}", nodeId, e);
      return Response.serverError().entity(e.getMessage()).build();
    }
  }

  @Path("node/{nodeId}/undoDelete")
  @POST
  @RolesAllowed("users")
  @Operation(summary = "Undo deleting node if not yet effectively deleted", method = "POST", description = "Undo deleting node if not yet effectively deleted")
  @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Request fulfilled"),
      @ApiResponse(responseCode = "400", description = "Invalid query input"),
      @ApiResponse(responseCode = "404", description = "Node not found"),
      @ApiResponse(responseCode = "403", description = "Forbidden operation"),
      @ApiResponse(responseCode = "401", description = "Unauthorized operation"),
      @ApiResponse(responseCode = "500", description = "Internal server error"), })
  public Response undoDeleteNode(@Context
  HttpServletRequest request,
                                 @Parameter(description = "Node identifier", required = true)
                                 @PathParam("nodeId")
                                 Long nodeId) {
    try {
      if (nodeId == null) {
        return Response.status(Response.Status.BAD_REQUEST).entity("Node id is mandatory").build();
      }
      NodeData nodeData = navigationService.getNodeById(nodeId);
      if (nodeData == null) {
        return Response.status(Response.Status.NOT_FOUND).entity("Node data with node id is not found").build();
      }
      Identity currentIdentity = ConversationState.getCurrent().getIdentity();
      if (!SiteNavigationUtils.canEditNavigation(currentIdentity, nodeData)) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }

      if (navigationNodeToDeleteQueue.containsKey(nodeId)) {
        String authenticatedUser = request.getRemoteUser();
        String originalModifierUser = navigationNodeToDeleteQueue.get(nodeId);
        if (!originalModifierUser.equals(authenticatedUser)) {
          LOG.warn("User {} attempts to cancel deletion of navigation node deleted by user {}",
                   authenticatedUser,
                   originalModifierUser);
          return Response.status(Response.Status.FORBIDDEN).build();
        }
        navigationNodeToDeleteQueue.remove(nodeId);
        return Response.noContent().build();
      } else {
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity("Node with id {} was already deleted or isn't planned to be deleted" + nodeId)
                       .build();
      }
    } catch (Exception e) {
      LOG.error("Error when undo deleting the navigation node with id {}", nodeId, e);
      return Response.serverError().build();
    }
  }

  @PATCH
  @Path("/node/move")
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed("users")
  @Operation(summary = "Move navigation node", method = "POST", description = "This move the navigation node")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Request fulfilled"),
      @ApiResponse(responseCode = "400", description = "Invalid query input"),
      @ApiResponse(responseCode = "401", description = "User not authorized to move the navigation node"),
      @ApiResponse(responseCode = "404", description = "Node not found") })
  public Response moveNode(@Parameter(description = "node id")
  @QueryParam("nodeId")
  Long nodeId,
                           @Parameter(description = "previous id")
                           @QueryParam("previousNodeId")
                           Long previousNodeId) {

    try {
      if (nodeId == null) {
        return Response.status(Response.Status.BAD_REQUEST).entity("Node id is mandatory").build();
      }
      NodeData nodeData = navigationService.getNodeById(nodeId);
      if (nodeData == null) {
        return Response.status(Response.Status.NOT_FOUND).entity("Node data with node id is not found").build();
      }
      Identity currentIdentity = ConversationState.getCurrent().getIdentity();
      if (!SiteNavigationUtils.canEditNavigation(currentIdentity, nodeData)) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      long parentId = Long.parseLong(nodeData.getParentId());
      navigationService.moveNode(nodeId, parentId, parentId, previousNodeId);
      return Response.ok().build();
    } catch (Exception e) {
      LOG.error("Error when moving the navigation node with id {}", nodeId, e);
      return Response.serverError().build();
    }
  }

  @Path("/page/permissions")
  @PATCH
  @RolesAllowed("users")
  @Operation(summary = "update page access abd edit permission", method = "PUT", description = "update page access abd edit permission")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Page permissions updated"),
      @ApiResponse(responseCode = "400", description = "Invalid query input"),
      @ApiResponse(responseCode = "404", description = "Page not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized operation"),
      @ApiResponse(responseCode = "500", description = "Internal server error"), })
  public Response updatePagePermissions(@Context
  HttpServletRequest request,
                                        @Parameter(description = "Page reference", required = true)
                                        @QueryParam("pageRef")
                                        String pageRef,
                                        @Parameter(description = "Page new edit permission", required = true)
                                        @QueryParam("editPermission")
                                        String editPermission,
                                        @Parameter(description = "Page new access permissions", required = true)
                                        @QueryParam("accessPermissions")
                                        String accessPermissions) {
    try {
      if (StringUtils.isBlank(pageRef) || StringUtils.isBlank(editPermission) || StringUtils.isBlank(accessPermissions)) {
        return Response.status(Response.Status.BAD_REQUEST).entity("params are mandatory").build();
      }
      PageContext pageContext = layoutService.getPageContext(PageKey.parse(pageRef));
      if (pageContext == null) {
        return Response.status(Response.Status.NOT_FOUND).build();
      }
      if (!SiteNavigationUtils.canEditPage(pageContext)) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      PageState pageState = pageContext.getState();
      List<String> accessPermissionsList = List.of(accessPermissions.split(","));
      pageContext.setState(new PageState(pageState.getDisplayName(),
                                         pageState.getDescription(),
                                         pageState.getShowMaxWindow(),
                                         pageState.getFactoryId(),
                                         accessPermissionsList,
                                         editPermission,
                                         pageState.getMoveAppsPermissions(),
                                         pageState.getMoveContainersPermissions()));
      layoutService.save(pageContext);
      return Response.ok().build();
    } catch (Exception e) {
      LOG.error("Error when updating page permissions with reference {}", pageRef, e);
      return Response.serverError().build();
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
