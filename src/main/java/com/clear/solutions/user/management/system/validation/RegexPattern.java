package com.clear.solutions.user.management.system.validation;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RegexPattern {

    public static final String EMAIL_REGEX_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    public static final String NAME_REGEX_PATTERN = "^([a-zA-Z]{2,}\\s[a-zA-Z]{1,}'?-?[a-zA-Z]{2,}\\s?([a-zA-Z]{1,})?)";

    public static final String PHONE_NUMBER_REGEX_PATTERN = "^$|\\\\+(?:[0-9] ?){6,14}[0-9]$";

}

