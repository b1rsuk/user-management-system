package com.clear.solutions.user.management.system.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@UtilityClass
public class DateUtils {

    public static int convertToYears(Date startDate) {
        LocalDate birthDate = convertDateToLocalDate(startDate);
        LocalDate currentDate = LocalDate.now();

        Period difference = Period.between(birthDate, currentDate);
        return difference.getYears();
    }

    private static LocalDate convertDateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
