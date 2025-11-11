import java.io.*;

public class HackAssembler {

   /**  public String noSymbolsAssembler(String filepath) {
        try {
            Code code = new Code();
            Parser parse = new Parser(filepath);
            File outputFile = new File(filepath.replace(".asm", ".hack"));
            FileWriter writer = new FileWriter(outputFile);
    
            System.out.println("Parsing file: " + filepath);
            
            while(parse.hasMoreLines()){
                parse.advance();
                String instructionType = parse.instructionType();
                System.out.println("Current instruction type: " + instructionType);
                
                if (instructionType.equals("A_INSTRUCTION")) {
                    int number = Integer.parseInt(parse.symbol());
                    String binary = String.format("%16s", Integer.toBinaryString(number)).replace(' ', '0');
                    writer.write(binary + "\n");
                } else if (instructionType.equals("C_INSTRUCTION")) {
                    System.out.println("the comp instruction before code= "+parse.comp());
                    String comp = code.comp(parse.comp());
                    System.out.println("the dest instruction before code= "+parse.dest());
                    String dest = code.dest(parse.dest());
                    System.out.println("the jump instruction before code= "+parse.jump());
                    String jump = code.jump(parse.jump());
                    String newline = "111" + comp + dest + jump;
                    System.out.println("bitsToPrint= "+newline);
                    writer.write(newline + "\n");
                }
            }
            parse.close();
            writer.close();
            return outputFile.getAbsolutePath();
    
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }
        
    } */

    public String SymbolsAssembler(String filepath) {
        try {
            Code code = new Code();
            Parser parse = new Parser(filepath);
            SymbolTable table = new SymbolTable();
            File outputFile = new File(filepath.replace(".asm", ".hack"));
            FileWriter writer = new FileWriter(outputFile);
            int counter = 0;

            System.out.println("Parsing file: " + filepath);

            while(parse.hasMoreLines()){    //first run
                parse.advance();
                System.out.println("curr line- "+parse.getCurrentInstruction());

                if(parse.instructionType().equals("A_INSTRUCTION")||parse.instructionType().equals("C_INSTRUCTION")){
                    counter++;
                     System.out.println("num of instructions- "+counter);
                }

                if(parse.instructionType().equals("L_INSTRUCTION")){
                   System.out.println("added to map- "+parse.getCurrentInstruction().substring(1,parse.getCurrentInstruction().length()-1 ));
                   table.addEntry(parse.getCurrentInstruction().substring(1,parse.getCurrentInstruction().length()-1 ),counter);
                }
            }
            parse.reopen(filepath);
            int symbolsNum = 15;

            while (parse.hasMoreLines()) {    // Second run
                parse.advance();
                String type = parse.instructionType();
                String symbol = parse.symbol();
                String newline = null;  // Initialize newline
                System.out.println("Current instruction type: " + type);
            
                if (type.equals("C_INSTRUCTION")) {
                    String comp = code.comp(parse.comp());
                    String dest = code.dest(parse.dest());
                    String jump = code.jump(parse.jump());
                    newline = "111" + comp + dest + jump;
                    writer.write(newline + "\n");
                } else if (type.equals("A_INSTRUCTION")) {
                    if (symbol != null && !symbol.isEmpty() && symbol.matches("\\d+")) {  // If it's a numeric value
                        int symbolasnum = Integer.parseInt(symbol);
                        System.out.println("Numeric A_INSTRUCTION: " + symbolasnum);
                        newline = String.format("%16s", Integer.toBinaryString(symbolasnum)).replace(' ', '0');
                        writer.write(newline + "\n");
                    } else {  // Handle symbols
                        int address = table.getAddress(symbol);
                        if (address != -1) {  // Symbol is in the table
                            System.out.println("Symbol found in table: " + symbol + " -> " + address);
                            newline = String.format("%16s", Integer.toBinaryString(address)).replace(' ', '0');
                        } else {  // Symbol not in table, add it
                            symbolsNum++;
                            table.addEntry(symbol, symbolsNum);
                            System.out.println("New symbol added: " + symbol + " -> " + symbolsNum);
                            newline = String.format("%16s", Integer.toBinaryString(symbolsNum)).replace(' ', '0');
                        }
                        writer.write(newline + "\n");
                    }
                }
            }
            
            parse.close();
            writer.close();
            return outputFile.getAbsolutePath();
    
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }

    
}