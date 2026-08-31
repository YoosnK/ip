/**
 * Reads raw user input from stdin. Does nothing else - no trimming, no parsing,
 * no printing - so it stays reusable regardless of how input ends up sanitized.
 */
public class InputReader {
    private static final java.util.Scanner SCANNER = new java.util.Scanner(System.in);

    static String getUserInput() {
        return SCANNER.nextLine();
    }
}
