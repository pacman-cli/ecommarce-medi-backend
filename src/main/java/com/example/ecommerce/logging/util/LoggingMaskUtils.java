package com.example.ecommerce.logging.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility for masking sensitive information (passwords, tokens, credit card numbers) in log strings.
 */
public final class LoggingMaskUtils {

    private static final Pattern SENSITIVE_JSON_PATTERN = Pattern.compile(
            "\"(password|confirmPassword|token|accessToken|refreshToken|creditCard|cardNumber|cvv|secret)\"\\s*:\\s*\"([^\"]+)\"",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern CREDIT_CARD_PATTERN = Pattern.compile("\\b(?:\\d[ -]*?){13,16}\\b");

    private LoggingMaskUtils() {
    }

    /**
     * Masks sensitive JSON fields and payment card numbers in text payloads.
     */
    public static String maskSensitiveData(String input) {
        if (input == null || input.isBlank()) {
            return input;
        }

        Matcher jsonMatcher = SENSITIVE_JSON_PATTERN.matcher(input);
        StringBuffer sb = new StringBuffer();
        while (jsonMatcher.find()) {
            String fieldName = jsonMatcher.group(1);
            jsonMatcher.appendReplacement(sb, "\"" + fieldName + "\":\"***MASKED***\"");
        }
        jsonMatcher.appendTail(sb);

        String result = sb.toString();

        Matcher ccMatcher = CREDIT_CARD_PATTERN.matcher(result);
        return ccMatcher.replaceAll("****-****-****-****");
    }
}
