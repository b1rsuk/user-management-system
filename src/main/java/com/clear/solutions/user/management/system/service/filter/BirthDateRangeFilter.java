package com.clear.solutions.user.management.system.service.filter;

import com.clear.solutions.user.management.system.service.exception.BirthDateRangeException;
import com.clear.solutions.user.management.system.utils.DateUtils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BirthDateRangeFilter {
    Date from;
    Date to;

    public BirthDateRangeFilter(LocalDate from, LocalDate to) {
        validateRange(from, to);

        this.from = DateUtils.convertLocalDateToDate(from);
        this.to = DateUtils.convertLocalDateToDate(to);
    }

    private static void validateRange(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new BirthDateRangeException(from, to);
        }
    }
}