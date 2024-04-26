package com.clear.solutions.user.management.system.validation;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RegexPattern {

    @UtilityClass
    public class EMAIL {
        /**
         * Validates an email address.
         *
         * Requirements:
         * - The email address must contain a local part followed by "@" symbol and a domain part.
         * - The local part can contain letters (a-z, A-Z), digits (0-9), dots (.), underscores (_), percentage signs (%), plus signs (+), and hyphens (-).
         * - The domain part must contain at least one dot (.) and consist of letters (a-z, A-Z) and digits (0-9).
         * - The domain part must have at least two letters.
         */
        public static final String EMAIL_REGEX_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        public static final String INVALID_EMAIL_MESSAGE = "Invalid email address";
    }

    @UtilityClass
    public class NAME {
        /**
         * Validates a first name.
         * The first name must consist of alphabetic characters only.
         */
        public static final String NAME_REGEX_PATTERN = "^[A-Za-z]+$";
        public static final String INVALID_NAME_MESSAGE = "Invalid name format";
    }

    @UtilityClass
    public class PHONE {
        /**
         * The number may start with the character "+".
         * Then, the country code of 1 to 3 digits may follow (optional).
         */
        public static final String PHONE_NUMBER_REGEX_PATTERN = "^\\+([0-9]{1,3})?[0-9]*$";
        public static final String INVALID_PHONE_MESSAGE = "Invalid phone number";
    }


}

