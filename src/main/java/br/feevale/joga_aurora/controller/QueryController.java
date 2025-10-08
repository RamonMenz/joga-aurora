package br.feevale.joga_aurora.controller;

import br.feevale.joga_aurora.service.QueryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/query")
public class QueryController {

    private static final String RESULT_ROWS_AFFECTED = "%s ROW(S) AFFECTED";
    private final QueryService service;

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<?> postQuery(@RequestBody final String queryBase64) {
        return ResponseEntity.ok(service.getQueryResult(queryBase64));
    }

    @Secured("ROLE_ADMIN")
    @PutMapping()
    public ResponseEntity<?> update(@RequestBody final String queryBase64) {
        return ResponseEntity.ok(String.format(RESULT_ROWS_AFFECTED, service.getRowsAffected(queryBase64)));
    }

}
