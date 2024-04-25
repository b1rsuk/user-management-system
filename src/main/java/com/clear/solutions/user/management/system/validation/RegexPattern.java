package com.clear.solutions.user.management.system.validation;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RegexPattern {

    @UtilityClass
    public class EMAIL {
        public static final String EMAIL_REGEX_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        public static final String INVALID_EMAIL_MESSAGE = "Invalid email address";
    }

    @UtilityClass
    public class NAME {
        public static final String NAME_REGEX_PATTERN = "^([a-zA-Z]{2,}\\s[a-zA-Z]{1,}'?-?[a-zA-Z]{2,}\\s?([a-zA-Z]{1,})?)";
        public static final String INVALID_NAME_MESSAGE = "Invalid name format";
    }

    @UtilityClass
    public class PHONE {
        public static final String PHONE_NUMBER_REGEX_PATTERN = "^$|\\\\+(?:[0-9] ?){6,14}[0-9]$";
        public static final String INVALID_PHONE_MESSAGE = "Invalid phone number";
    }


}

