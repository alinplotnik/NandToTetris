import java.io.*;


public class CodeWriter {
    private File outputFile;
    private FileWriter writer;
    private int return_labales_num=0;
    private String fileName;

    public CodeWriter(String filepath) throws IOException {
        this.outputFile = new File(filepath.replace(".vm", ".asm"));
        this.fileName = new File(filepath).getName().replace(".vm", "");
        this.writer = new FileWriter(outputFile);
    }

    public String bootstrap(){
        StringBuilder newLine = new StringBuilder();;
        try {
            writer.write("// next is a bootstrap\n");

            newLine.append("@256\n");
            newLine.append("D=A\n");
            newLine.append("@SP\n");
            newLine.append("M=D\n");
           
            writer.write(newLine.toString());
            writeCall("Sys.init", 0);
            return "Operation completed successfully";
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String filepath) throws IOException {
        this.fileName = new File(filepath).getName().replace(".vm", "");

        writer.write("// Translating file: " + this.fileName + "\n");
    }

public String writeArithmetic(String instruction) {   // translate arguments like add, neg, eq...
        String newLine = null;
        try {
           switch (instruction) {
            case "add":          // pop [sp-1] and push it to [sp-2]
            newLine = """   
            @SP
            AM=M-1
            D=M
            A=A-1
            M=D+M                     
                     """;
                    writer.write("// next is a writeArithmetic with instruction: " + instruction + "\n");
                    writer.write( "\n" + newLine);
                    break;

                    case "sub":
                    newLine = """
                        @SP
                        AM=M-1        // Decrement SP, get y
                        D=M           // Store y in D
                        A=A-1         // Point to x
                        D=M-D         // Compute x - y and store result in D
                        M=D           // Write the result (x - y) back to the stack
                        """;
                     writer.write("// next is a writeArithmetic with instruction: " + instruction + "\n");
                     writer.write( "\n //sub \n" + newLine);
                     break;  
             case "neg":
             newLine = """
            @SP
            A=M-1
            M=-M 
                   """;
                   writer.write("// next is a writeArithmetic with instruction: " + instruction + "\n");
                   writer.write( "\n" + newLine);
                   break;
             case "eq":
               String trueLabel = "TRUE_" + return_labales_num;
               String endLabel = "END_" + return_labales_num;
         
              newLine = 
        "@SP\n" +
        "AM=M-1         // Decrement SP, get y\n" +
        "D=M            // Store y in D\n" +
        "A=A-1          // Point to x\n" +
        "D=D-M          // Compute x - y\n" +
        "@" + trueLabel + "\n" +
        "D;JEQ          // If x == y, jump to TRUE\n" +
        "@SP\n" +
        "A=M-1          // Stay at the current top of the stack\n" +
        "M=0            // Write false (0) to the stack\n" +
        "@" + endLabel + "\n" +
        "0;JMP          // Jump to END\n" +
        "(" + trueLabel + ")\n" +
        "@SP\n" +
        "A=M-1          // Stay at the current top of the stack\n" +
        "M=-1           // Write true (-1) to the stack\n" +
        "(" + endLabel + ")";
                     writer.write("// next is a writeArithmetic with instruction: " + instruction + "\n");
                     writer.write( "\n" + newLine);
                     return_labales_num++;
                     break;


                     case "gt":
                     String trueLabelGt = "TRUE_" + return_labales_num;
                     String endLabelGt = "END_" + return_labales_num;
                 
                     newLine = 
                         "@SP\n" +
                         "AM=M-1         // Decrement SP, get y\n" +
                         "D=M            // Store y in D\n" +
                         "A=A-1          // Point to x\n" +
                         "D=M-D          // Compute x - y (correct order)\n" +
                         "@" + trueLabelGt + "\n" +
                         "D;JGT          // If x > y, jump to TRUE\n" +
                         "@SP\n" +
                         "A=M-1          // Stay at the current top of the stack\n" +
                         "M=0            // Write false (0) to the stack\n" +
                         "@" + endLabelGt + "\n" +
                         "0;JMP          // Jump to END\n" +
                         "(" + trueLabelGt + ")\n" +
                         "@SP\n" +
                         "A=M-1          // Stay at the current top of the stack\n" +
                         "M=-1           // Write true (-1) to the stack\n" +
                         "(" + endLabelGt + ")";
                     writer.write("// next is a writeArithmetic with instruction: " + instruction + "\n");
                     writer.write( "\n" + newLine);
                     return_labales_num++; // Increment the label counter for the next instruction
                     break;
                 
                     case "lt":
                     String trueLabelLt = "TRUE_" + return_labales_num;
                     String endLabelLt = "END_" + return_labales_num;
                 
                     newLine = 
                         "@SP\n" +
                         "AM=M-1         // Decrement SP, get y\n" +
                         "D=M            // Store y in D\n" +
                         "A=A-1          // Point to x\n" +
                         "D=M-D          // Compute x - y (correct order)\n" +
                         "@" + trueLabelLt + "\n" +
                         "D;JLT          // If x < y, jump to TRUE\n" +
                         "@SP\n" +
                         "A=M-1          // Stay at the current top of the stack\n" +
                         "M=0            // Write false (0) to the stack\n" +
                         "@" + endLabelLt + "\n" +
                         "0;JMP          // Jump to END\n" +
                         "(" + trueLabelLt + ")\n" +
                         "@SP\n" +
                         "A=M-1          // Stay at the current top of the stack\n" +
                         "M=-1           // Write true (-1) to the stack\n" +
                         "(" + endLabelLt + ")\n";
                     writer.write("// next is a writeArithmetic with instruction: " + instruction + "\n");
                     writer.write( "\n" + newLine);
                     return_labales_num++; // Increment the label counter for the next instruction
                     break;
                 
             case "and":
             newLine = """
            @SP
            AM=M-1
            D=M
            A=A-1
            D=D&M
            M=D   
                    """;
                    writer.write("// next is a writeArithmetic with instruction: " + instruction + "\n");
                    writer.write( "\n" + newLine);
                    break;

           case "or":
            newLine = """
            @SP
            AM=M-1
            D=M
            A=A-1
            D=D|M
            M=D   
                    """;
                    writer.write("// next is a writeArithmetic with instruction: " + instruction + "\n");
                    writer.write( "\n" + newLine);
                    break;
                           
           case "not":
           newLine = """
            @SP
            A=M-1
            M=!M    
                    """;
                    writer.write("// next is a writeArithmetic with instruction: " + instruction + "\n");
                    writer.write("\n" + newLine);
                    break;
            default:
                break;
           }
            return outputFile.getAbsolutePath();
    } catch (IOException e) {
        System.err.println("Error: " + e.getMessage());
        return null;
    }
 }

 public String writePushPop(String instructionType, String arg1, int arg2) {
    String newLine = null;
    try {
        if (instructionType.equals("C_PUSH")) {     
            switch (arg1) {
                case "constant":
                    newLine =
                        "@" + arg2 + "\n" +
                        "D=A\n" +
                        "@SP\n" +
                        "A=M\n" +
                        "M=D\n" +
                        "@SP\n" +
                        "M=M+1\n";
                    writer.write("// next is a writePushPop with instructionType: " + instructionType + ", arg1: " + arg1 + ", arg2: " + arg2 + "\n");
                    writer.write(newLine);
                    break;

                case "local":
                    newLine =
                        "@" + arg2 + "\n" +
                        "D=A\n" +
                        "@LCL\n" +
                        "A=D+M\n" +
                        "D=M\n" +
                        "@SP\n" +
                        "A=M\n" +
                        "M=D\n" +
                        "@SP\n" +
                        "M=M+1\n";
                    writer.write("// next is a writePushPop with instructionType: " + instructionType + ", arg1: " + arg1 + ", arg2: " + arg2 + "\n");
                    writer.write( "\n" + newLine);
                    break;

                case "argument":
                    newLine =
                        "@" + arg2 + "\n" +
                        "D=A\n" +
                        "@ARG\n" +
                        "A=D+M\n" +
                        "D=M\n" +
                        "@SP\n" +
                        "A=M\n" +
                        "M=D\n" +
                        "@SP\n" +
                        "M=M+1\n";
                    writer.write("// next is a writePushPop with instructionType: " + instructionType + ", arg1: " + arg1 + ", arg2: " + arg2 + "\n");
                    writer.write( "\n" + newLine);
                    break;

                case "static":
                    newLine =
                        "@" + this.getFileName() + "." + arg2 + "\n" +
                        "D=M\n" +
                        "@SP\n" +
                        "A=M\n" +
                        "M=D\n" +
                        "@SP\n" +
                        "M=M+1\n";
                    writer.write("// next is a writePushPop with instructionType: " + instructionType + ", arg1: " + arg1 + ", arg2: " + arg2 + "\n");
                    writer.write( "\n" + newLine);
                    break;

                case "this":
                    newLine =
                        "@" + arg2 + "\n" +
                        "D=A\n" +
                        "@THIS\n" +
                        "A=D+M\n" +
                        "D=M\n" +
                        "@SP\n" +
                        "A=M\n" +
                        "M=D\n" +
                        "@SP\n" +
                        "M=M+1\n";
                    writer.write("// next is a writePushPop with instructionType: " + instructionType + ", arg1: " + arg1 + ", arg2: " + arg2 + "\n");
                    writer.write( "\n" + newLine);
                    break;

                case "that":
                    newLine =
                        "@" + arg2 + "\n" +
                        "D=A\n" +
                        "@THAT\n" +
                        "A=D+M\n" +
                        "D=M\n" +
                        "@SP\n" +
                        "A=M\n" +
                        "M=D\n" +
                        "@SP\n" +
                        "M=M+1\n";
                    writer.write("// next is a writePushPop with instructionType: " + instructionType + ", arg1: " + arg1 + ", arg2: " + arg2 + "\n");
                    writer.write( "\n" + newLine);
                    break;

                case "temp":
                    newLine =
                        "@" + (5 + arg2) + "\n" +
                        "D=M\n" +
                        "@SP\n" +
                        "A=M\n" +
                        "M=D\n" +
                        "@SP\n" +
                        "M=M+1\n";
                    writer.write("// next is a writePushPop with instructionType: " + instructionType + ", arg1: " + arg1 + ", arg2: " + arg2 + "\n");
                    writer.write( "\n" + newLine);
                    break;

                case "pointer":
                    newLine =
                        "@" + (3 + arg2) + "\n" +
                        "D=M\n" +
                        "@SP\n" +
                        "A=M\n" +
                        "M=D\n" +
                        "@SP\n" +
                        "M=M+1\n";
                    writer.write("// next is a writePushPop with instructionType: " + instructionType + ", arg1: " + arg1 + ", arg2: " + arg2 + "\n");
                    writer.write( "\n" + newLine);
                    break;

                default:
                    throw new IllegalArgumentException("Invalid segment for PUSH: " + arg1);
            }
        } else if (instructionType.equals("C_POP")) {
            switch (arg1) {
                case "local":
                case "argument":
                case "this":
                case "that":
                    String baseAddress = switch (arg1) {
                        case "local" -> "LCL";
                        case "argument" -> "ARG";
                        case "this" -> "THIS";
                        case "that" -> "THAT";
                        default -> throw new IllegalStateException("Unexpected segment: " + arg1);
                    };
                    newLine =
                        "@" + arg2 + "\n" +
                        "D=A\n" +
                        "@" + baseAddress + "\n" +
                        "D=D+M\n" +
                        "@R13\n" +
                        "M=D\n" +
                        "@SP\n" +
                        "AM=M-1\n" +
                        "D=M\n" +
                        "@R13\n" +
                        "A=M\n" +
                        "M=D\n";
                    writer.write("// next is a writePushPop with instructionType: " + instructionType + ", arg1: " + arg1 + ", arg2: " + arg2 + "\n");
                    writer.write( "\n" + newLine);
                    break;

                case "constant":
                    newLine =
                        "@SP\n" +
                        "AM=M-1\n" +
                        "D=M\n" +
                        "@" + arg2 + "\n" +
                        "M=D\n";
                    writer.write("// next is a writePushPop with instructionType: " + instructionType + ", arg1: " + arg1 + ", arg2: " + arg2 + "\n");
                    writer.write( "\n" + newLine);
                    break;

                case "static":
                    newLine =
                        "@SP\n" +
                        "AM=M-1\n" +
                        "D=M\n" +
                        "@" + fileName + "." + arg2 + "\n" +
                        "M=D\n";
                    writer.write("// next is a writePushPop with instructionType: " + instructionType + ", arg1: " + arg1 + ", arg2: " + arg2 + "\n");
                    writer.write( "\n" + newLine);
                    break;

                case "temp":
                    newLine =
                        "@SP\n" +
                        "AM=M-1\n" +
                        "D=M\n" +
                        "@" + (arg2 + 5) + "\n" +
                        "M=D\n";
                    writer.write("// next is a writePushPop with instructionType: " + instructionType + ", arg1: " + arg1 + ", arg2: " + arg2 + "\n");
                    writer.write( "\n" + newLine);
                    break;

                case "pointer":
                    if (arg2 == 0) {
                        newLine =
                            "@SP\n" +
                            "AM=M-1\n" +
                            "D=M\n" +
                            "@THIS\n" +
                            "M=D\n";
                    } else if (arg2 == 1) {
                        newLine =
                            "@SP\n" +
                            "AM=M-1\n" +
                            "D=M\n" +
                            "@THAT\n" +
                            "M=D\n";
                    } else {
                        throw new IllegalArgumentException("Invalid index for pointer: " + arg2);
                    }
                    writer.write("// next is a writePushPop with instructionType: " + instructionType + ", arg1: " + arg1 + ", arg2: " + arg2 + "\n");
                    writer.write( "\n" + newLine);
                    break;

                default:
                    throw new IllegalArgumentException("Invalid segment for POP: " + arg1);
            }
        }

        return "Operation completed successfully";

    } catch (IOException e) {
        System.err.println("Error: " + e.getMessage());
        return null;
    } catch (IllegalArgumentException | IllegalStateException e) {
        System.err.println("Error: " + e.getMessage());
        return null;
    }
}

public String writeLabel (String label){
    String newLine = null;
    try {
            newLine = "("+label+")\n";
            writer.write("// next is a writeLabel with label: " + label + "\n");
            writer.write(newLine);

         return "Operation completed successfully";
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }
}

public String writeGoto (String label){
    String newLine = null;
    try {
       
            newLine = "@"+ label + "\n" +
            "0;JMP\n";  

         writer.write("// next is a writeGoto with label: " + label + "\n");
         writer.write(newLine);
         return "Operation completed successfully";
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }

}

public String writeIf (String label){
    String newLine = null;
    try {
         newLine =
          "@SP\n" +
          "AM=M-1\n" +
          "D=M\n" +
          "@"+label + "\n" +
          "D;JNE\n ";
         writer.write("// next is a writeIf with label: " + label + "\n");
         writer.write(newLine);
         return "Operation completed successfully";

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }
}

public String writeFunction(String functionName, int nVars) {
    StringBuilder newLine = new StringBuilder();
    try {
        newLine.append("(").append(functionName).append(")\n");
        for (int i = 0; i < nVars; i++) {
            newLine.append("@0\n") // Push 0 onto the stack
                    .append("D=A\n")
                    .append("@SP\n")
                    .append("A=M\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("M=M+1\n");
        }
        writer.write("// next is a writeFunction with functionName: " + functionName + ", nVars: " + nVars + "\n");
        writer.write(newLine.toString());
        return "Operation completed successfully";

    } catch (IOException e) {
        System.err.println("Error: " + e.getMessage());
        return null;
    }
}

public String writeCall (String functionName, int nArgs){
String newLine = null;
     try {
        String returnLabel = null;
        return_labales_num++;
        returnLabel = functionName + "$ret." + return_labales_num;
           

        // Push return address
        newLine =
            "@" + returnLabel + "\n" +
            "D=A\n" +
            "@SP\n" +
            "A=M\n" +
            "M=D\n" +
            "@SP\n" +
            "M=M+1\n";

        // Push LCL
        newLine +=
            "@LCL\n" +
            "D=M\n" +
            "@SP\n" +
            "A=M\n" +
            "M=D\n" +
            "@SP\n" +
            "M=M+1\n";

        // Push ARG
        newLine +=
            "@ARG\n" +
            "D=M\n" +
            "@SP\n" +
            "A=M\n" +
            "M=D\n" +
            "@SP\n" +
            "M=M+1\n";

        // Push THIS
        newLine +=
            "@THIS\n" +
            "D=M\n" +
            "@SP\n" +
            "A=M\n" +
            "M=D\n" +
            "@SP\n" +
            "M=M+1\n";

        // Push THAT
        newLine +=
            "@THAT\n" +
            "D=M\n" +
            "@SP\n" +
            "A=M\n" +
            "M=D\n" +
            "@SP\n" +
            "M=M+1\n";

        //reposition ARG
        newLine +=
            "@SP\n"+
            "D=M\n"+
            "@"+nArgs+"\n" +
            "D=D-A\n"+
            "@5\n"+
            "D=D-A\n"+
            "@ARG\n"+
            "M=D\n";

        //reposition LCL
        newLine +=
            "@SP\n"+
            "D=M\n"+
            "@LCL\n"+
            "M=D\n";
            
        newLine+=
            "@"+ functionName + "\n" +
            "0;JMP\n" +
            "(" +returnLabel+")\n";    
    writer.write("// writeCall with functionName: " + functionName + ", nArgs: " + nArgs + "\n");
    writer.write(newLine);
    return "Operation completed successfully";
    
   } catch (IOException e) {
       System.err.println("Error: " + e.getMessage());
       return null;
   }
}

public String writeReturn (){
    String newLine = null;
    try {
        newLine =
        "@LCL\n" +         
        "D=M\n" +
        "@R13\n" +
        "M=D\n" + 

        "@5\n" +         // get return address
        "A=D-A\n" +
        "D=M\n" +
        "@R14\n" +
        "M=D\n" +    

        "@SP\n" +      // put return value for caller
        "AM=M-1\n" +
        "D=M\n" +
        "@ARG\n" +
        "A=M\n" +
        "M=D\n" +

        "@ARG\n"+        // SP = ARG + 1
        "D=M+1\n"+
        "@SP\n"+
        "M=D\n"+      

        "@R13\n" +    // Restore THAT, THIS, ARG, LCL
        "D=M\n" +
        "@1\n" +
        "A=D-A\n" +
        "D=M\n" +
        "@THAT\n" +
        "M=D\n" +
        
        "@R13\n" +    
        "D=M\n" +
        "@2\n" +
        "A=D-A\n" +
        "D=M\n" +
        "@THIS\n" +
        "M=D\n" +
        
        "@R13\n" +    
        "D=M\n" +
        "@3\n" +
        "A=D-A\n" +
        "D=M\n" +
        "@ARG\n" +
        "M=D\n" +
        
        "@R13\n" +    
        "D=M\n" +
        "@4\n" +
        "A=D-A\n" +
        "D=M\n" +
        "@LCL\n" +
        "M=D\n" +
        
        "@R14\n" +     // Jump to the return address
        "A=M\n" +
        "0;JMP\n";

        writer.write("// next is a writeReturn\n");
        writer.write(newLine);
        return "Return Operation completed successfully";
    
       } catch (IOException e) {
           System.err.println("Error: " + e.getMessage());
           return null;
       }
}

    public void close() throws IOException {
        writer.close();
    }

}
