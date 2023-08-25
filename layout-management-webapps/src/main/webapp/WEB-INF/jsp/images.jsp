<%@ page import="javax.portlet.PortletURL" %>
<%@ page import="javax.portlet.PortletMode" %>
<%@ page import="javax.portlet.PortletPreferences" %>
<%@ page import="org.exoplatform.social.core.manager.IdentityManager"%>
<%@ page import="org.exoplatform.commons.utils.CommonsUtils"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<portlet:defineObjects />
<portlet:actionURL var="saveSettingsURL" />
<%
  int generatedId = (int) (Math.random() * 1000000l);
  String appId = "image-" + generatedId;
  IdentityManager identityManager = CommonsUtils.getService(IdentityManager.class);
  PortletPreferences preferences = renderRequest.getPreferences();
  String imageUrl = preferences.getValue("imageUrl", "");
  String fileId = preferences.getValue("fileId", "default");
  int maxUploadSize = identityManager.getImageUploadLimit();
%>
<div id="ImagesPortlet" class="VuetifyApp">
  <div id="<%= appId %>">
    <script type="text/javascript">
      require(['PORTLET/layout-management/ImagesPortlet'], app => app.init(
        '<%=appId%>',
        '<%=imageUrl%>',
        '<%=fileId%>',
        <%=maxUploadSize%>,
        <%= "'" + saveSettingsURL + "'" %>
      ));
    </script>
  </div>
</div>