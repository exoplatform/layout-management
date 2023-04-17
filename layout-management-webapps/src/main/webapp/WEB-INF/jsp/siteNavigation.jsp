<%@ page import="org.exoplatform.navigation.webui.Utils" %>
<%@ page import="org.exoplatform.services.security.ConversationState" %>
<%@ page import="org.exoplatform.commons.api.settings.ExoFeatureService" %>
<%@ page import="org.exoplatform.commons.utils.CommonsUtils"%>
<%@ page import="org.exoplatform.portal.mop.user.UserNavigation" %>
<%@ page import="org.exoplatform.layoutmanagement.utils.SiteNavigationUtils" %>

<%
  ExoFeatureService featureService = CommonsUtils.getService(ExoFeatureService.class);
  org.exoplatform.services.security.Identity currentIdentity = ConversationState.getCurrent().getIdentity();
  String currentUser = currentIdentity.getUserId();
  boolean editNavigationDrawerEnabled = featureService.isFeatureActiveForUser("newEditNavigationDrawer", currentUser);
  boolean canManageSiteNavigation = (Utils.hasEditPermissionOnNavigation() || Utils.hasEditPermissionOnPortal()) && editNavigationDrawerEnabled;
  UserNavigation userNavigation = Utils.getSelectedNavigation();
  String siteType = userNavigation.getKey().getTypeName();
  String siteName = userNavigation.getKey().getName();
  boolean isAdministrator = SiteNavigationUtils.isAdministrator();
%>
<div class="VuetifyApp">
  <div id="siteNavigation">
    <script type="text/javascript">
        eXo.env.portal.siteKeyType = '<%=siteType%>';
        eXo.env.portal.siteKeyName = '<%=siteName%>';
        eXo.env.portal.isAdministrator = <%=isAdministrator%>;
      require(['PORTLET/layout-management/SiteNavigation'], app => app.init(<%=canManageSiteNavigation%>));
    </script>
  </div>
</div>
