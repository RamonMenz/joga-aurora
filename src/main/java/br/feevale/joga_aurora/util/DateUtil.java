package br.feevale.joga_aurora.util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class DateUtil {

    private DateUtil() {
        throw new IllegalStateException("Utility class.");
    }

    public static Date thisDateOrToday(final Date date) {
        return Objects.nonNull(date) ? date : Date.valueOf(LocalDate.now());

    }

    public static int getAgeByBirthDate(final Date birthDate) {
        return Period.between(birthDate.toLocalDate(), LocalDate.now()).getYears();
    }

}
