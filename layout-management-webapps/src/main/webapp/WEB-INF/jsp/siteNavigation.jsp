<%@ page import="org.exoplatform.navigation.webui.Utils" %>
<%@ page import="org.exoplatform.services.security.ConversationState" %>
<%@ page import="org.exoplatform.commons.api.settings.ExoFeatureService" %>
<%@ page import="org.exoplatform.commons.utils.CommonsUtils"%>

<%
  ExoFeatureService featureService = CommonsUtils.getService(ExoFeatureService.class);
  org.exoplatform.services.security.Identity currentIdentity = ConversationState.getCurrent().getIdentity();
  String currentUser = currentIdentity.getUserId();
  boolean editNavigationDrawerEnabled = featureService.isFeatureActiveForUser("newEditNavigationDrawer", currentUser);
  boolean canManageSiteNavigation = (Utils.hasEditPermissionOnNavigation() || Utils.hasEditPermissionOnPortal()) && editNavigationDrawerEnabled;
%>
<div class="VuetifyApp">
  <div id="siteNavigation">
    <script type="text/javascript">
      require(['PORTLET/layout-management/SiteNavigation'], app => app.init(<%=canManageSiteNavigation%>));
    </script>
  </div>
</div>