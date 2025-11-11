import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
//import java.util.List;

public class CompilationEngine {
    private JackTokenizer tokenizer;
    private BufferedWriter writer;

    private void writeToken() throws IOException {
        System.out.println("printing token: " + tokenizer.printCurrentToken() + ". ** token type: " + tokenizer.tokenType());
        writer.write("<" + tokenizer.tokenType() + "> " + tokenizer.printCurrentToken() + " </" + tokenizer.tokenType() + ">\n");
        tokenizer.advance();
    }

    public CompilationEngine(String inputFilePath) throws IOException {
        String outputFilePath = inputFilePath.replace(".jack", ".xml");
        this.writer = new BufferedWriter(new FileWriter(outputFilePath));
        this.tokenizer = new JackTokenizer(inputFilePath);
        
        try {
          //  printTokens(); // Print tokens before starting compilation
            compileClass(); // Perform the compilation process
        } catch (IOException e) {
            System.err.println("Error during compilation: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ensure the writer is always flushed and closed
            try {
                if (writer != null) {
                    writer.flush(); // Flush any remaining data
                    writer.close(); // Close the writer
                    System.out.println("Output file successfully closed.");
                }
            } catch (IOException closeException) {
                System.err.println("Error while closing the writer: " + closeException.getMessage());
            }
        }
    }

   /*  private void printTokens() {
        List<String> tokens = tokenizer.getTokens();
        System.out.println("Tokens:");
        for (String token : tokens) {
            System.out.println(token);
        }
    } */

    public void compileClass() throws IOException {  //compiles a complete class
        tokenizer.advance();
        writer.write("<class>\n");
        writeToken(); // 'class'
        writeToken(); // className
        writeToken(); // '{'
        while (tokenizer.getCurrentToken().equals("static") || tokenizer.getCurrentToken().equals("field")) {
            compileClassVarDec();
        }
        while (tokenizer.getCurrentToken().equals("constructor") || tokenizer.getCurrentToken().equals("function") || tokenizer.getCurrentToken().equals("method")) {
            compileSubroutine();
        }
        writeToken(); // '}'
        writer.write("</class>\n");
    }

    public void compileClassVarDec() throws IOException { //compiles static variable or field declaration
        writer.write("<classVarDec>\n");
        writeToken(); // 'static' or 'field'
        writeToken(); // type
        writeToken(); // varName
        while (tokenizer.getCurrentToken().equals(",")) {
            writeToken(); // ','
            writeToken(); // varName
        }
        writeToken(); // ';'
        writer.write("</classVarDec>\n");
    }

    public void compileSubroutine() throws IOException { //compiles a complete method, function, or constructor
        writer.write("<subroutineDec>\n");
        writeToken(); // subroutine keyword
        writeToken(); // return type
        writeToken(); // subroutine name
        writeToken(); // '('
        compileParameterList(); // compile parameter list
        compileSubroutineBody(); // compile subroutine body
        writer.write("</subroutineDec>\n");
    }

    public void compileParameterList() throws IOException { //compiles a (possibly empty) parameter list
        writer.write("<parameterList>\n");
        if (!tokenizer.getCurrentToken().equals(")")) {
            writeToken(); // type
            writeToken(); // varName
            while (tokenizer.getCurrentToken().equals(",")) {
                writeToken(); // ','
                writeToken(); // type
                writeToken(); // varName
            }
        }
        writer.write("</parameterList>\n");
        writeToken(); // ')'
    }

    public void compileSubroutineBody() throws IOException { // compiles the body of the subroutine
        writer.write("<subroutineBody>\n");
        writeToken(); // '{'
        while (tokenizer.getCurrentToken().equals("var")) {
            compileVarDec(); // compile variable declarations
        }
        compileStatements(); // compile statements
        writeToken(); // '}'
        writer.write("</subroutineBody>\n");
    }

    public void compileVarDec() throws IOException { //compiles a var declaration
        writer.write("<varDec>\n");
        writeToken(); // 'var'
        writeToken(); // type
        writeToken(); // varName
        while (tokenizer.getCurrentToken().equals(",")) {
            writeToken(); // ','
            writeToken(); // varName
        }
        writeToken(); // ';'
        writer.write("</varDec>\n");
    }

    public void compileStatements() throws IOException { //compiles a sequence of statements, not including the enclosing {}
        writer.write("<statements>\n");
        while (tokenizer.getCurrentToken().equals("let") || tokenizer.getCurrentToken().equals("if") || tokenizer.getCurrentToken().equals("while") || tokenizer.getCurrentToken().equals("do") || tokenizer.getCurrentToken().equals("return")) {
            if (tokenizer.getCurrentToken().equals("let")) {
                compileLet();
            } else if (tokenizer.getCurrentToken().equals("if")) {
                compileIf();
            } else if (tokenizer.getCurrentToken().equals("while")) {
                compileWhile();
            } else if (tokenizer.getCurrentToken().equals("do")) {
                compileDo();
            } else if (tokenizer.getCurrentToken().equals("return")) {
                compileReturn();
            }
        }
        writer.write("</statements>\n");
    }

    public void compileLet() throws IOException { //compiles a let statement
        writer.write("<letStatement>\n");
        writeToken(); // 'let'
        writeToken(); // varName
        if (tokenizer.getCurrentToken().equals("[")) {
            writeToken(); // '['
            compileExpression();
            writeToken(); // ']'
        }
        writeToken(); // '='
        compileExpression();
        writeToken(); // ';'
        writer.write("</letStatement>\n");
    }

    public void compileIf() throws IOException { //compiles an if statement, possibly with a trailing else clause
        writer.write("<ifStatement>\n");
        writeToken(); // 'if'
        writeToken(); // '('
        compileExpression();
        writeToken(); // ')'
        writeToken(); // '{'
        compileStatements();
        writeToken(); // '}'
        if (tokenizer.getCurrentToken().equals("else")) {
            writeToken(); // 'else'
            writeToken(); // '{'
            compileStatements();
            writeToken(); // '}'
        }
        writer.write("</ifStatement>\n");
    }

    public void compileWhile() throws IOException { //compiles a while statement
        writer.write("<whileStatement>\n");
        writeToken();  // 'while'
        writeToken();  // '('
        compileExpression();  
        writeToken(); // ')'
        writeToken(); // '{'
        compileStatements();
        writeToken(); // '}'
        writer.write("</whileStatement>\n");
    }

    public void compileDo() throws IOException { // compiles a do statement
        writer.write("<doStatement>\n");
        writeToken(); // 'do'
        String identifier = tokenizer.getCurrentToken();
        tokenizer.advance();
        compileSubroutineCall(identifier); // compile the subroutine call with the identifier
        writeToken(); // ';'
        writer.write("</doStatement>\n");
    }

    public void compileReturn() throws IOException { //compiles a return statement
        writer.write("<returnStatement>\n");
        writeToken(); // 'return'
        if (!tokenizer.getCurrentToken().equals(";")) {
            compileExpression();
        }
        writeToken(); // ';'
        writer.write("</returnStatement>\n");
    }

    public void compileExpression() throws IOException {
        writer.write("<expression>\n");
        compileTerm();
        while (isOperator(tokenizer.getCurrentToken())) {
            writeToken(); // op
            compileTerm();
        }
        writer.write("</expression>\n");
    }
    
    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || 
               token.equals("/") || token.equals("&") || token.equals("|") || 
               token.equals("<") || token.equals(">") || token.equals("=");
    }
    
    public void compileTerm() throws IOException { 
        writer.write("<term>\n");
    
        // Handle unary operators first
        if (tokenizer.getCurrentToken().equals("-") || tokenizer.getCurrentToken().equals("~")) {
            writeToken(); // Write the unary operator
            compileTerm(); // Recursively compile the term after the unary operator
        }
        // Handle parenthesized expressions
        else if (tokenizer.getCurrentToken().equals("(")) {
            writeToken(); // '('
            compileExpression();
            writeToken(); // ')'
        }
        // Handle identifiers and their variations
        else if (tokenizer.tokenType().equals("identifier")) {
            String identifier = tokenizer.getCurrentToken();
            tokenizer.advance();
    
            if (tokenizer.getCurrentToken().equals("[")) {
                writer.write("<identifier> " + identifier + " </identifier>\n");
                writeToken(); // '['
                compileExpression();
                writeToken(); // ']'
            } else if (tokenizer.getCurrentToken().equals("(") || tokenizer.getCurrentToken().equals(".")) {
                compileSubroutineCall(identifier);
            } else {
                writer.write("<identifier> " + identifier + " </identifier>\n");
            }
        }
        // Handle constants
        else if (tokenizer.tokenType().equals("integerConstant")) {
            writer.write("<integerConstant> " + tokenizer.printCurrentToken() + " </integerConstant>\n");
            tokenizer.advance();
        } else if (tokenizer.tokenType().equals("stringConstant")) {
            writer.write("<stringConstant> " + tokenizer.printCurrentToken() + " </stringConstant>\n");
            tokenizer.advance();
        }
        // Handle keywords
        else if (tokenizer.tokenType().equals("keyword") && 
                (tokenizer.printCurrentToken().equals("this") || 
                 tokenizer.printCurrentToken().equals("null") ||
                 tokenizer.printCurrentToken().equals("false") || 
                 tokenizer.printCurrentToken().equals("true"))) {
            writer.write("<keyword> " + tokenizer.printCurrentToken() + " </keyword>\n");
            tokenizer.advance();
        }
    
        writer.write("</term>\n");
    }

    public void compileExpressionList() throws IOException { //compiles a (possibly empty) comma-separated list of expressions
        writer.write("<expressionList>\n");
        if (!tokenizer.getCurrentToken().equals(")")) {
            compileExpression();
            while (tokenizer.getCurrentToken().equals(",")) {
                writeToken(); // ','
                compileExpression();
            }
        }
        writer.write("</expressionList>\n");
    }

    public void compileSubroutineCall(String identifier) throws IOException { //compiles a subroutine call
        writer.write("<identifier> " + identifier + " </identifier>\n");
        if (tokenizer.getCurrentToken().equals(".")) {
            writeToken(); // '.'
            writeToken(); // subroutineName
        }
        writeToken(); // '('
        compileExpressionList();
        writeToken(); // ')'
    }
}
