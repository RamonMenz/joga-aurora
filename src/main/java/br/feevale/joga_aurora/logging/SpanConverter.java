package br.feevale.joga_aurora.logging;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.Objects;

public class SpanConverter extends ClassicConverter {

    @Override
    public String convert(final ILoggingEvent iLoggingEvent) {
        final var spanId = iLoggingEvent.getMDCPropertyMap().get("spanId");
        return Objects.isNull(spanId) ? "" : spanId;
    }

}
