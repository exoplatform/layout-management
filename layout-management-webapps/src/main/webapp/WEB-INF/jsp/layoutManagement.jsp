<%@ page import="org.exoplatform.navigation.webui.Utils" %>
<%
  boolean canAcceed = Utils.hasEditPermissionOnNavigation() || Utils.hasEditPermissionOnPortal();
%>
<div class="VuetifyApp">
  <div id="layoutManagement">
    <script type="text/javascript">
      require(['PORTLET/layout-management/layoutManagement'], app => app.init(<%=canAcceed%>));
    </script>
  </div>
</div>