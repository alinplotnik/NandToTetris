import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JackTokenizer {
    private BufferedReader reader;
    private List<String> tokens;
    private int currentTokenIndex;
   
    

    public JackTokenizer(String inputFilePath) throws IOException {
        this.reader = new BufferedReader(new FileReader(inputFilePath));
        this.tokens = new ArrayList<>();
        this.currentTokenIndex = -1;
        tokenize();
    }

    private void tokenize() throws IOException {
        String line;
        boolean inMultilineComment = false; // Tracks if we are inside a multiline comment
    
        while ((line = reader.readLine()) != null) {
            line = line.trim();
    
            // Skip multiline comments
            if (inMultilineComment) {
                if (line.endsWith("*/")) {
                    inMultilineComment = false; // End of multiline comment
                }
                continue;
            }
            if (line.startsWith("/*")) {
                inMultilineComment = true; // Start of multiline comment
                if (line.endsWith("*/")) {
                    inMultilineComment = false; // Handle single-line block comments
                }
                continue;
            }
    
            // Skip single-line comments and empty lines
            if (line.startsWith("//") || line.isEmpty()) {
                continue;
            }
    
            // Normalize and process the line
            String normalizedLine = line.replaceAll("\\s+", " ");
            int end = normalizedLine.indexOf("//");
            if (end != -1) {
                normalizedLine = normalizedLine.substring(0, end);
            }
    
            StringBuilder currToken = new StringBuilder();
            boolean inString = false;
    
            for (int i = 0; i < normalizedLine.length(); i++) {
                char c = normalizedLine.charAt(i);
                if (c == '"') {
                    if (inString) {
                        currToken.append(c);
                        tokens.add(currToken.toString());
                        currToken.setLength(0);
                        inString = false;
                    } else {
                        if (currToken.length() > 0) {
                            tokens.add(currToken.toString());
                            currToken.setLength(0);
                        }
                        currToken.append(c);
                        inString = true;
                    }
                } else if (inString) {
                    currToken.append(c);
                } else if (isSymbol(c)) {
                    if (currToken.length() > 0) {
                        tokens.add(currToken.toString());
                        currToken.setLength(0);
                    }
                    tokens.add(String.valueOf(c));
                } else if (Character.isWhitespace(c)) {
                    if (currToken.length() > 0) {
                        tokens.add(currToken.toString());
                        currToken.setLength(0);
                    }
                } else {
                    currToken.append(c);
                }
            }
    
            if (currToken.length() > 0) {
                tokens.add(currToken.toString());
            }
        }
        reader.close();
    }
    

    public boolean hasMoreTokens() {
        return currentTokenIndex < tokens.size() - 1;
    }

    public void advance() {
        if (hasMoreTokens()) {
            currentTokenIndex++;
        } else {
            System.out.println("Cannot advance: no tokens available. Current token: " + getCurrentToken());
        }
    }

    public void decrementIndex() {
        if (currentTokenIndex > 0) {
            currentTokenIndex--;
        } else {
            System.out.println("Cannot decrement index: already at the beginning of the token list.");
        }
    }
    

    public String getCurrentToken() {
        if (currentTokenIndex >= 0 && currentTokenIndex < tokens.size()) {
            return tokens.get(currentTokenIndex);
        }
        return null;
    }
    
    public String printCurrentToken() {
        if (currentTokenIndex >= 0 && currentTokenIndex < tokens.size()) {
            String tokenType = tokenType();
            switch (tokenType) {
                case "keyword":
                    return keyWord();
                case "symbol":
                    return String.valueOf(symbol());
                case "integerConstant":
                    return String.valueOf(intVal());
                case "stringConstant":
                    return stringVal();
                case "identifier":
                    return identifier();
                default:
                    throw new IllegalStateException("Unknown token type: " + tokenType);
            }
        }
        return null;
    }

    public static boolean isSymbol(char c) {
        return "{}()[].,;+-*/&|<>=~".indexOf(c) != -1;
    }


    public String tokenType() {
        try {
            String currentToken = getCurrentToken();
            if (currentToken == null || currentToken.isEmpty()) {
                throw new IllegalStateException("No current token to process.");
            }
            String tokenType = JackLexicon.mapWordToType(currentToken);
            if (tokenType.equals("KEYWORD")) {
                return "keyword";
            } else if (tokenType.equals("SYMBOL")) {
                return "symbol";
            } else if (tokenType.equals("INT_CONST")) {
                return "integerConstant";
            } else if (tokenType.equals("STRING_CONST")) {
                return "stringConstant";
            } else if (tokenType.equals("IDENTIFIER")) {
                return "identifier";
            } else {
                throw new IllegalStateException("Unknown token type: " + currentToken);
            }
        } catch (Exception e) {
            System.err.println("Exception in tokenType: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw the exception to ensure it is not silently ignored
        }
    }

    public void close() throws IOException {
        if (reader != null) reader.close();
    }

    public void reopen(String filepath) throws IOException {
        close();
        this.reader = new BufferedReader(new FileReader(filepath));
        this.tokens.clear();
        this.currentTokenIndex = -1;
        tokenize();
    }

    public String getFilePath() {
        return this.reader.toString();
    }

    public String keyWord() {
        String currentToken = getCurrentToken();
        if (currentToken != null) {
            // Map the current token to a predefined set of constants
            switch (currentToken.toUpperCase()) {
                case "CLASS":
                case "METHOD":
                case "FUNCTION":
                case "CONSTRUCTOR":
                case "INT":
                case "BOOLEAN":
                case "CHAR":
                case "VOID":
                case "VAR":
                case "STATIC":
                case "FIELD":
                case "LET":
                case "DO":
                case "IF":
                case "ELSE":
                case "WHILE":
                case "RETURN":
                case "TRUE":
                case "FALSE":
                case "NULL":
                case "THIS":
                    return currentToken;
                default:
                    throw new IllegalStateException("Unknown keyword: " + currentToken);
            }
        }
        return null;
    }

    public String symbol() {
        String currentToken = getCurrentToken();
        if (currentToken != null && currentToken.length() == 1) {
            switch (currentToken) {
                case "<":
                    return "&lt;";
                case ">":
                    return "&gt;";
                case "&":
                    return "&amp;";
                default:
                    return currentToken;
            }
        }
        throw new IllegalStateException("No valid symbol token: " + currentToken);
    }

    public int intVal() {
        String currentToken = getCurrentToken();
        if (currentToken != null && currentToken.matches("\\d+")) {
            int value = Integer.parseInt(currentToken);
            if (value >= 0 && value <= 32767) {
                return value;
            }
        }
        throw new IllegalStateException("No valid integer constant token: " + currentToken);
    }

    public String stringVal() {
        String currentToken = getCurrentToken();
        if (currentToken != null && currentToken.startsWith("\"") && currentToken.endsWith("\"")) {
            return currentToken.substring(1, currentToken.length() - 1);
        }
        throw new IllegalStateException("No valid string constant token: " + currentToken);
    }

    public String identifier() {
        String currentToken = getCurrentToken();
        if (currentToken != null && currentToken.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            return currentToken;
        }
        throw new IllegalStateException("No valid identifier token: " + currentToken);
    }

    public List<String> getTokens() {
        return tokens;
    }

}
