package br.feevale.joga_aurora.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Objects;

public final class JsonUtil {

    private JsonUtil() {
        throw new IllegalStateException("Utility class.");
    }

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .setDateFormat(new StdDateFormat().withColonInTimeZone(true))
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public static <T> String objectToJson(final T object) {
        if (Objects.isNull(object))
            return "{}";

        try {
            return MAPPER.writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            return "";
        }
    }

}
