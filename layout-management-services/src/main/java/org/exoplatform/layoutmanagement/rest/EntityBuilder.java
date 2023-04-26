/*
 * Copyright (C) 2023 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
