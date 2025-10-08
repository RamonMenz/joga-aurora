package br.feevale.joga_aurora.service;

import br.feevale.joga_aurora.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static br.feevale.joga_aurora.enums.LogStatusEnum.FINISHED;
import static br.feevale.joga_aurora.enums.LogStatusEnum.STARTED;

@Slf4j
@AllArgsConstructor
@Service
public class QueryService {

    private static final String LINE_FEED = String.valueOf((char) 10);
    private static final String CARRIAGE_RETURN = String.valueOf((char) 13);
    private final JdbcTemplate jdbcTemplate;

    public JsonNode getQueryResult(final String queryBase64) {
        final var start = Instant.now();

        final var query = new String(Base64.getDecoder().decode(queryBase64));
        final var queryLog = queryLog(query);

        log.info("status={} query={}", STARTED, queryLog);

        final var result = new ArrayList<Map<String, Object>>();

        jdbcTemplate.query(connection -> connection
                .prepareStatement(query, Statement.RETURN_GENERATED_KEYS), resultSet -> {
            final var resultSetMetaData = resultSet.getMetaData();
            final var columnCount = resultSetMetaData.getColumnCount();
            final var columnNames = IntStream.range(0, columnCount)
                    .mapToObj(columnNumber -> {
                        try {
                            return resultSetMetaData.getColumnName(columnNumber + 1);
                        } catch (final SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }).toList();

            do {
                final var resultRow = new HashMap<String, Object>();
                columnNames.forEach(columnName -> {
                    try {
                        resultRow.put(columnName, resultSet.getObject(columnName));
                    } catch (final SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                result.add(resultRow);
            } while (resultSet.next());
        });

        final var response = JsonUtil.objectToJsonNode(result);

        final var isArray = response.isArray() && response.size() > 1;
        final var responseLog = isArray ? String.format("size[%s]", response.size()) : JsonUtil.objectToJson(response);

        log.info("status={} query={} response={} timeMillis={}", FINISHED, queryLog, responseLog, Duration.between(start, Instant.now()).toMillis());
        return response;
    }

    public Integer getRowsAffected(final String queryBase64) {
        final var start = Instant.now();

        final var query = new String(Base64.getDecoder().decode(queryBase64));
        final var queryLog = queryLog(query);

        log.info("status={} query={}", STARTED, queryLog);

        final var result = jdbcTemplate.update(query);

        log.info("status={} query={} response={} timeMillis={}", FINISHED, queryLog, result, Duration.between(start, Instant.now()).toMillis());
        return result;
    }

    private static String queryLog(final String query) {
        var queryLog = query;
        if (queryLog.contains(LINE_FEED) && queryLog.contains(CARRIAGE_RETURN))
            queryLog = queryLog.replace(CARRIAGE_RETURN, "");
        else
            queryLog = queryLog.replace(CARRIAGE_RETURN, " ");

        queryLog = queryLog.replace(LINE_FEED, " ");
        return queryLog;
    }

}
