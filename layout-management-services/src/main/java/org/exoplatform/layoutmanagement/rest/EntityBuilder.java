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

import org.apache.commons.lang3.StringUtils;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.commons.utils.I18N;
import org.exoplatform.layoutmanagement.rest.model.DescriptionRestEntity;
import org.exoplatform.layoutmanagement.rest.model.PageTemplateRestEntity;
import org.exoplatform.layoutmanagement.utils.SiteNavigationUtils;
import org.exoplatform.portal.jdbc.entity.DescriptionState;
import org.exoplatform.portal.mop.State;
import org.exoplatform.services.resources.LocaleConfig;
import org.exoplatform.services.resources.LocaleConfigService;
import org.exoplatform.webui.core.model.SelectItemOption;

import java.util.*;
import java.util.stream.Collectors;

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

  public static DescriptionRestEntity toDescriptionEntity(Map<Locale, State> descriptions) {

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
    DescriptionRestEntity descriptionRestEntity = new DescriptionRestEntity();
    if (descriptions != null && descriptions.size() != 0) {
      for (Map.Entry<Locale, org.exoplatform.portal.mop.State> entry : descriptions.entrySet()) {
        Locale locale = entry.getKey();
        org.exoplatform.portal.mop.State state = entry.getValue();
        localized.put(I18N.toTagIdentifier(locale), state.getName());
      }
      descriptionRestEntity.setDescriptions(localized);
    }
    descriptionRestEntity.setDefaultLanguage(defaultLanguage);
    descriptionRestEntity.setSupportedLanguages(supportedLanguages);

    return descriptionRestEntity;
  }
}
