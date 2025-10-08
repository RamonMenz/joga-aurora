package br.feevale.joga_aurora.config.logging;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.Objects;

public class TraceConverter extends ClassicConverter {

    @Override
    public String convert(final ILoggingEvent iLoggingEvent) {
        final var traceId = iLoggingEvent.getMDCPropertyMap().get("traceId");
        return Objects.isNull(traceId) ? "" : traceId;
    }

}
