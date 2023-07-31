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

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.exoplatform.portal.mop.SiteType;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.security.Identity;
import org.gatein.api.Util;
import org.gatein.api.site.Site;

import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.commons.utils.I18N;
import org.exoplatform.layoutmanagement.rest.model.NodeLabelRestEntity;
import org.exoplatform.layoutmanagement.rest.model.PageTemplateRestEntity;
import org.exoplatform.layoutmanagement.rest.model.SiteRestEntity;
import org.exoplatform.layoutmanagement.utils.SiteNavigationUtils;
import org.exoplatform.portal.mop.State;
import org.exoplatform.services.resources.LocaleConfig;
import org.exoplatform.services.resources.LocaleConfigService;
import org.exoplatform.webui.core.model.SelectItemOption;

public class EntityBuilder {

  private static final Log LOG = ExoLogger.getLogger(EntityBuilder.class);

  private EntityBuilder() {
  }

  public static PageTemplateRestEntity toPageTemplateRestEntity(SelectItemOption<String> pageTemplate, Locale userLocal) {
    if (pageTemplate == null) {
      return null;
    }
    return new PageTemplateRestEntity(SiteNavigationUtils.getI18NLabel(userLocal, pageTemplate.getLabel()),
                                      pageTemplate.getValue());
  }

  public static List<PageTemplateRestEntity> toPageTemplateRestEntities(List<SelectItemOption<String>> pageTemplates, Locale userLocal) {
    return pageTemplates.stream().map(pageTemplate -> toPageTemplateRestEntity(pageTemplate, userLocal)).toList();
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

  public static SiteRestEntity toSiteRestEntity(Site site) {
    if (site == null) {
      return null;
    }
    SiteType siteType = Util.from(site.getType());
    String displayName = site.getDisplayName();
    Identity userIdentity = ConversationState.getCurrent().getIdentity();
    if (SiteType.GROUP.equals(siteType)) {
      try {
        Group siteGroup = getOrganizationService().getGroupHandler().findGroupById(site.getName());
        if (siteGroup == null || !userIdentity.isMemberOf(siteGroup.getId())) {
          return null;
        } else if (StringUtils.isBlank(displayName)) {
          displayName = siteGroup.getLabel();
        }
      } catch (Exception e) {
        LOG.error("Error while retrieving group with name ", site.getName(), e);
      }
    }
    List<Map<String, Object>> accessPermissions = computePermissions(Util.from(site.getAccessPermission()));
    Map<String, Object> editPermission = computePermission(Util.from(site.getEditPermission())[0]);
    return new SiteRestEntity(site.getId(),
                              siteType,
                              site.getName(),
                              !StringUtils.isBlank(displayName) ? displayName : site.getName(),
                              site.getDescription(),
                              accessPermissions,
                              editPermission);
  }

  public static List<SiteRestEntity> toSiteRestEntities(List<Site> sites) {
    return sites.stream()
                .map(site -> toSiteRestEntity(site))
                .filter(Objects::nonNull)
                .toList();
  }
  private static OrganizationService getOrganizationService() {
    return CommonsUtils.getService(OrganizationService.class);
  }

  private static Map<String, Object> computePermission(String permission) {
    Map<String, Object> accessPermission = new HashMap<>();
    try {
      accessPermission.put("membershipType", permission.split(":")[0]);
      accessPermission.put("group", getOrganizationService().getGroupHandler().findGroupById(permission.split(":")[1]));
    } catch (Exception e) {
      LOG.error("Error while computing user permission ", permission, e);
    }
    return accessPermission;
  }

  private static List<Map<String, Object>> computePermissions(String[] permissions) {
    return Arrays.stream(permissions).map(EntityBuilder::computePermission).toList();
  }
}
