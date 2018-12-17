package com.seregy77.mdnrd.socialnetwork.domain;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.orientechnologies.orient.core.record.OElement;

import java.util.List;
import java.util.stream.Collectors;

public class Json {
    private final String value;

    public Json(String value) {
        this.value = value;
    }

    public static List<Json> fromOElements(List<OElement> oElements) {
        return oElements.stream()
                .map(Json::fromOElement)
                .collect(Collectors.toList());
    }

    public static Json fromOElement(OElement oElement) {
        return new Json(oElement.toJSON());
    }

    @JsonValue
    @JsonRawValue
    public String value() {
        return value;
    }
}
