package org.exoplatform.layoutmanagement.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DescriptionRestEntity {
    private Map<String, String> supportedLanguages;

    private Map<String, String> descriptions;

    private String defaultLanguage;
}
