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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.commons.utils.I18N;
import org.exoplatform.layoutmanagement.rest.model.NodeLabelRestEntity;
import org.exoplatform.layoutmanagement.rest.model.PageTemplateRestEntity;
import org.exoplatform.layoutmanagement.utils.SiteNavigationUtils;
import org.exoplatform.portal.mop.State;
import org.exoplatform.services.resources.LocaleConfig;
import org.exoplatform.services.resources.LocaleConfigService;
import org.exoplatform.webui.core.model.SelectItemOption;

public class EntityBuilder {

  private EntityBuilder() {
  }

  public static PageTemplateRestEntity toRestEntity(SelectItemOption<String> pageTemplate, Locale userLocal) {
    if (pageTemplate == null) {
      return null;
    }
    return new PageTemplateRestEntity(SiteNavigationUtils.getI18NLabel(userLocal, pageTemplate.getLabel()),
                                      pageTemplate.getValue());
  }

  public static List<PageTemplateRestEntity> toRestEntities(List<SelectItemOption<String>> pageTemplates, Locale userLocal) {
    return pageTemplates.stream().map(pageTemplate -> toRestEntity(pageTemplate, userLocal)).toList();
  }

  public static NodeLabelRestEntity toNodeLabelRestEntity(Map<Locale, State> nodeLabels) {
    LocaleConfigService localeConfigService = CommonsUtils.getService(LocaleConfigService.class);
    Locale defaultLocale = localeConfigService.getDefaultLocaleConfig() == null ? Locale.ENGLISH
                                                                                : localeConfigService.getDefaultLocaleConfig()
                                                                                                     .getLocale();
    String defaultLanguage = defaultLocale.getLanguage();
    Map<String, String> supportedLanguages =
                                           localeConfigService.getLocalConfigs() == null ? Collections.singletonMap(defaultLocale.getLanguage(),
                                                                                                                    defaultLocale.getDisplayName())
                                                                                         : localeConfigService.getLocalConfigs()
                                                                                                              .stream()
                                                                                                              .filter(localeConfig -> !StringUtils.equals(localeConfig.getLocaleName(),
                                                                                                                                                          "ma"))
                                                                                                              .collect(Collectors.toMap(LocaleConfig::getLocaleName,
                                                                                                                                        localeConfig -> localeConfig.getLocale()
                                                                                                                                                                    .getDisplayName()));
    Map<String, String> localized = new HashMap<>();
    NodeLabelRestEntity nodeLabelRestEntity = new NodeLabelRestEntity();
    if (nodeLabels != null && nodeLabels.size() != 0) {
      for (Map.Entry<Locale, State> entry : nodeLabels.entrySet()) {
        Locale locale = entry.getKey();
        State state = entry.getValue();
        localized.put(I18N.toTagIdentifier(locale), state.getName());
      }
      if (!nodeLabels.containsKey(defaultLocale)) {
        localized.put(I18N.toTagIdentifier(defaultLocale), null);
      }
      nodeLabelRestEntity.setLabels(localized);
    }
    nodeLabelRestEntity.setDefaultLanguage(defaultLanguage);
    nodeLabelRestEntity.setSupportedLanguages(supportedLanguages);
    return nodeLabelRestEntity;
  }
}
