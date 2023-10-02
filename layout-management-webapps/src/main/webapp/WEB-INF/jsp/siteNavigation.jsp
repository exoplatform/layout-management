<%@ page import="org.exoplatform.navigation.webui.Utils" %>
<%@ page import="org.exoplatform.portal.mop.user.UserNavigation" %>
<%@ page import="org.exoplatform.layoutmanagement.utils.SiteNavigationUtils" %>
<%@ page import="org.exoplatform.portal.mop.service.LayoutService" %>
<%@ page import="org.exoplatform.portal.config.model.PortalConfig" %>
<%@ page import="org.exoplatform.commons.utils.CommonsUtils"%>

<%
  UserNavigation userNavigation = Utils.getSelectedNavigation();
  String siteType = userNavigation.getKey().getTypeName();
  String siteName = userNavigation.getKey().getName();
  boolean isAdministrator = SiteNavigationUtils.isAdministrator();
  boolean canManageSiteNavigation = Utils.hasEditPermissionOnNavigation() || Utils.hasEditPermissionOnPortal();
  LayoutService layoutService = CommonsUtils.getService(LayoutService.class);
  PortalConfig sitePortalConfig = layoutService.getPortalConfig(siteType, siteName);
  String siteId = sitePortalConfig.getStorageId().split("_")[1];
%>
<div class="VuetifyApp">
  <div id="siteNavigation">
    <script type="text/javascript">
        eXo.env.portal.siteKeyName = '<%=siteName%>';
        eXo.env.portal.siteId = '<%=siteId%>';
        eXo.env.portal.isAdministrator = <%=isAdministrator%>;
      require(['PORTLET/layout-management/SiteNavigation'], app => app.init(<%=canManageSiteNavigation%>));
    </script>
  </div>
</div>
