package nia.ui;

import java.util.Scanner;
/**
 * Reads raw user input from stdin. Does nothing else - no trimming, no parsing,
 * no printing - so it stays reusable regardless of how input ends up sanitized.
 */
public class InputReader {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static String getUserInput() {
        return SCANNER.nextLine();
    }
}