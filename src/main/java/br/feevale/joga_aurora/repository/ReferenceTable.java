package br.feevale.joga_aurora.repository;

import br.feevale.joga_aurora.entity.StudentEntity;
import br.feevale.joga_aurora.enums.GenderEnum;
import br.feevale.joga_aurora.util.DateUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

import static br.feevale.joga_aurora.enums.GenderEnum.FEMALE;
import static br.feevale.joga_aurora.enums.GenderEnum.MALE;

public class ReferenceTable {

    private final Map<String, Reference> references = new HashMap<>();

    public record Reference(
            Integer age,
            GenderEnum gender,
            Double bmi,
            Double waistHeightRatio,
            Double sixMinutes,
            Double flex,
            Double rml,
            Double twentyMeters,
            Double throwTwoKg
    ) {
    }

    public ReferenceTable() {
        loadReferences();
    }

    private void loadReferences() {
        add(new Reference(6, MALE, 17.7, 0.5, 675.0, 29.0, 18.0, 4.81, 147.0));
        add(new Reference(6, FEMALE, 17.0, 0.5, 630.0, 40.5, 18.0, 5.22, 125.0));

        add(new Reference(7, MALE, 17.8, 0.5, 730.0, 29.0, 18.0, 4.52, 168.7));
        add(new Reference(7, FEMALE, 17.1, 0.5, 683.0, 40.5, 18.0, 4.88, 140.0));

        add(new Reference(8, MALE, 19.2, 0.5, 768.0, 32.5, 25.0, 4.31, 190.0));
        add(new Reference(8, FEMALE, 18.2, 0.5, 715.0, 39.5, 24.0, 4.66, 158.1));

        add(new Reference(9, MALE, 19.3, 0.5, 820.0, 29.0, 26.0, 4.20, 210.0));
        add(new Reference(9, FEMALE, 19.1, 0.5, 745.0, 35.0, 20.0, 4.50, 175.0));

        add(new Reference(10, MALE, 20.7, 0.5, 856.0, 29.5, 31.0, 4.09, 232.0));
        add(new Reference(10, FEMALE, 20.9, 0.5, 790.0, 36.5, 26.0, 4.44, 202.0));

        add(new Reference(11, MALE, 22.1, 0.5, 930.0, 29.5, 37.0, 4.00, 260.0));
        add(new Reference(11, FEMALE, 22.3, 0.5, 840.0, 34.5, 30.0, 4.32, 228.0));

        add(new Reference(12, MALE, 22.2, 0.5, 966.0, 29.5, 39.0, 3.88, 290.0));
        add(new Reference(12, FEMALE, 22.6, 0.5, 900.0, 39.5, 30.0, 4.28, 260.0));

        add(new Reference(13, MALE, 22.0, 0.5, 1020.0, 26.5, 42.0, 3.72, 335.0));
        add(new Reference(13, FEMALE, 22.0, 0.5, 940.0, 38.5, 32.0, 4.17, 280.0));

        add(new Reference(14, MALE, 22.2, 0.5, 1060.0, 30.5, 44.0, 3.54, 400.0));
        add(new Reference(14, FEMALE, 22.0, 0.5, 985.0, 38.5, 35.0, 4.10, 290.0));

        add(new Reference(15, MALE, 23.0, 0.5, 1130.0, 31.0, 45.0, 3.40, 440.0));
        add(new Reference(15, FEMALE, 22.4, 0.5, 1005.0, 38.5, 34.0, 4.00, 306.0));

        add(new Reference(16, MALE, 24.0, 0.5, 1190.0, 34.5, 46.0, 3.28, 480.0));
        add(new Reference(16, FEMALE, 24.0, 0.5, 1070.0, 39.5, 34.0, 3.91, 370.0));

        add(new Reference(17, MALE, 25.4, 0.5, 1190.0, 34.0, 47.0, 3.22, 500.0));
        add(new Reference(17, FEMALE, 24.0, 0.5, 1110.0, 39.5, 34.0, 3.91, 315.0));
    }

    private void add(final Reference reference) {
        references.put(key(reference.age(), reference.gender()), reference);
    }

    private String key(final Integer age, final GenderEnum gender) {
        return age + "_" + gender.getCode();
    }

    private Reference getReference(final Integer age, final GenderEnum gender) {
        return references.get(key(age, gender));
    }

    public Reference getReference(final StudentEntity student) {
        final var age = DateUtil.getAgeByBirthDate(student.getBirthDate());

        if (age < 6 || age > 17 || student.getGender().equals(GenderEnum.NOT_INFORMED))
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "O estudante deve ter entre 6 e 17 anos e ter o gênero informado.");

        return getReference(age, student.getGender());
    }

}
