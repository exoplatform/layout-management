package org.exoplatform.layoutmanagement.rest;

import org.exoplatform.layoutmanagement.rest.model.PageTemplateRestEntity;
import org.exoplatform.webui.core.model.SelectItemOption;

import java.util.List;
import java.util.Locale;

import static org.exoplatform.layoutmanagement.utils.SiteNavigationUtils.getI18NMessage;
import static org.exoplatform.layoutmanagement.utils.SiteNavigationUtils.getPageTemplateDefaultSkeleton;

public class EntityBuilder {

  private EntityBuilder() {
  }

  public static PageTemplateRestEntity toRestEntity(SelectItemOption<String> pageTemplate, Locale userLocal) {
    if (pageTemplate == null) {
      return null;
    }
    return new PageTemplateRestEntity(getI18NMessage(userLocal, pageTemplate.getLabel()),
                                      pageTemplate.getValue(),
                                      pageTemplate.getIcon(),
                                      pageTemplate.isSelected(),
                                      pageTemplate.getDescription(),
                                      getPageTemplateDefaultSkeleton(pageTemplate.getValue()));
  }

  public static List<PageTemplateRestEntity> toRestEntities(List<SelectItemOption<String>> pageTemplates, Locale userLocal) {
    return pageTemplates.stream().map(pageTemplate -> toRestEntity(pageTemplate, userLocal)).toList();
  }

}
