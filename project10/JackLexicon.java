
import java.util.*;

public class JackLexicon {

    // Define the sets of keywords and symbols in the Jack language
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
            "class", "constructor", "function", "method", "field", "static",
            "var", "int", "char", "boolean", "void", "true", "false", "null",
            "this", "let", "do", "if", "else", "while", "return"
    ));

    private static final Set<Character> SYMBOLS = new HashSet<>(Arrays.asList(
            '{', '}', '(', ')', '[', ']', '.', ',', ';', '+', '-', '*', '/', '&', '|', '<', '>', '=', '~'
    ));

    /**
     * Maps a word to its type: keyword, symbol, integerConstant, stringConstant, or identifier.
     *
     * @param word The word to classify.
     * @return The type of the word.
     */
    public static String mapWordToType(String word) {
        // Check if the word is a keyword
        if (KEYWORDS.contains(word)) {
            return "KEYWORD";
        }

        // Check if the word is a symbol
        if (word.length() == 1 && SYMBOLS.contains(word.charAt(0))) {
            return "SYMBOL";
        }

        // Check if the word is an integer constant
        if (word.matches("\\d+") && Integer.parseInt(word) >= 0 && Integer.parseInt(word) <= 32767) {
            return "INT_CONST";
        }

        // Check if the word is a string constant
        if (word.startsWith("\"") && word.endsWith("\"")) {
            System.out.println("STRING_CONST was found: " + word);
            return "STRING_CONST";
        }

        // If none of the above, it must be an identifier
        if (word.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            return "IDENTIFIER";
        }
        System.out.println("Unknown token type: " + word);
        // If the word does not match any valid type, return "unknown"
        return "unknown";
    }

    /**
 * Checks if a character is a symbol in the Jack language.
 *
 * @param c The character to check.
 * @return True if the character is a symbol, false otherwise.
 */

 public static boolean isSymbol(char c) {
    return SYMBOLS.contains(c);
}

// Alternative isSymbol method that accepts a String
public static boolean isSymbol(String s) {
    return s.length() == 1 && SYMBOLS.contains(s.charAt(0));
}

public static boolean isKeyword(String word) {
    return KEYWORDS.contains(word);
}


}
