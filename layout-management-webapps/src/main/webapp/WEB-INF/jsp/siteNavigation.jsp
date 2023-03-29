<%@ page import="org.exoplatform.navigation.webui.Utils" %>
<%
  boolean canManageSiteNavigation = Utils.hasEditPermissionOnNavigation() || Utils.hasEditPermissionOnPortal();
%>
<div class="VuetifyApp">
  <div id="siteNavigation">
    <script type="text/javascript">
      require(['PORTLET/layout-management/SiteNavigation'], app => app.init(<%=canManageSiteNavigation%>));
    </script>
  </div>
</div>