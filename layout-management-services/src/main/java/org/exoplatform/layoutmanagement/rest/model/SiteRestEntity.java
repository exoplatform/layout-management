package org.exoplatform.layoutmanagement.rest.model;

import org.gatein.api.site.SiteId;
import org.exoplatform.portal.mop.SiteType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteRestEntity {

  private SiteId   id;

  private SiteType siteType;

  private String   name;

  private String   displayName;

  private String   description;

  private String[] accessPermissions;

  private String   editPermission;

}
