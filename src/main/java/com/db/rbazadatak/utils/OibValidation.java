package com.db.rbazadatak.utils;

public class OibValidation {

	private static final int asciiDigitsOffset = '0';

    /**
     * Validates the format and control number of a given OIB (Unique Identification Number) string.
     * The method throws an IllegalArgumentException if the OIB does not meet the following criteria:
     * - It must be exactly 11 characters long.
     * - All characters must be numerical digits ('0' - '9').
     * - The last digit must match the control number, which is calculated based on the first 10 digits.
     *
     *
     * @param oib The OIB string to be checked.
     * @throws IllegalArgumentException If the OIB string does not meet the length requirement, contains non-numerical characters,
     *                                  or the control number does not match the last digit.
     */
	public static void checkOIB(String oib) {
        if (oib.length() != 11) {
            throw new IllegalArgumentException("Invalid OIB length. OIB must be 11 characters long.");
        }

        char[] chars = oib.toCharArray();
        
        int a = 10;
        for (int i = 0; i < 10; i++) {
        	char c = chars[i];
        	if (c < '0' || c > '9') {
                throw new IllegalArgumentException("Invalid OIB format. OIB must only contain numerical characters.");
        	}
            a = a + (c - asciiDigitsOffset);
            a = a % 10;
            if (a == 0) {
                a = 10;
            }
            a *= 2;
            a = a % 11;
        }
        int controlNumber = 11 - a;
        controlNumber = controlNumber % 10;

        if (controlNumber != (chars[10] - asciiDigitsOffset)) {
            throw new IllegalArgumentException("Invalid OIB. Control number does not match.");
        }
    }
}