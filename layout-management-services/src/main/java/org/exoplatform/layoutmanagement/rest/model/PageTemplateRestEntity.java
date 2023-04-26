package org.exoplatform.layoutmanagement.rest.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageTemplateRestEntity {

  public String  label;

  public String  value;

  public String  icon;

  public boolean selected = false;

  public String  description;

  public String  skeleton;

}
