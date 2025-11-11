import java.io.*;

public class VMTranslator_class {

    public String translator(String filepath) {
        try {
            File inputPath = new File(filepath);

            if (inputPath.isDirectory()) {
                // If the input is a directory
                File[] vmFiles = inputPath.listFiles((dir, name) -> name.endsWith(".vm"));
                if (vmFiles == null || vmFiles.length == 0) {
                    throw new FileNotFoundException("No .vm files found in the directory.");
                }

                // Create the output .asm file in the same directory
                String outputFileName = filepath.endsWith("/") ? filepath.substring(0, filepath.length() - 1) : filepath;
                outputFileName = outputFileName + "/" + inputPath.getName() + ".asm";
                CodeWriter codeWriter = new CodeWriter(outputFileName);
                
                // Check if the directory contains a Sys.init file
                boolean hasSysInit = false;
                for (File vmFile : vmFiles) {
                    if (vmFile.getName().equals("Sys.vm")) {
                        hasSysInit = true;
                        break;
                    }
                }

                // Run bootstrap only if Sys.init file is present
                if (hasSysInit) {
                    codeWriter.bootstrap();
                }

                for (File vmFile : vmFiles) {
                    Parser parse = new Parser(vmFile.getPath());
                    codeWriter.setFileName(vmFile.getPath()); // Set the file name for each file

                    while (parse.hasMoreLines()) {
                        parse.advance();
                        String newLine = parse.getCurrentInstruction();

                      
                        if (newLine.startsWith("//") || newLine.isEmpty() || newLine.startsWith("\n")) {
                            continue; // Skip comments or empty lines
                        }
    
                        String type = parse.instructionType();
                       String arg1 = null;
                        int arg2 = -1;
    
                        // Retrieve arguments based on instruction type
                        if (!type.equals("comment")&&!type.equals("C_RETURN")) {
                            arg1 = parse.arg1();
                        }
    
                            if (type.equals("C_PUSH") || type.equals("C_POP")|| type.equals("C_FUNCTION")|| type.equals("C_CALL")) {
                                arg2 = parse.arg2();
                            }
    
                        // Handle the instruction type
                        if (type.equals("C_ARITHMETIC")) {
                            codeWriter.writeArithmetic(newLine);
                        }
                        else if (type.equals("C_PUSH") || type.equals("C_POP")) {
                            codeWriter.writePushPop(type, arg1, arg2);
                        }
                        else if (type.equals("C_LABEL")){
                            codeWriter.writeLabel(arg1);
                            System.out.println("label was printed");
                        }
                        else if (type.equals("C_GOTO")){
                            codeWriter.writeGoto(arg1);
                        }
                        else if (type.equals("C_IF")){
                            codeWriter.writeIf(arg1);
                        }
                        else if (type.equals("C_FUNCTION")){
                            codeWriter.writeFunction(arg1, arg2);
                        }
                        else if (type.equals("C_CALL")){
                            System.out.println("call was printed");
                            codeWriter.writeCall(arg1, arg2);
                        }
                        else if (type.equals("C_RETURN")){
                            codeWriter.writeReturn();
                        }
                    
                }
                    parse.close();
                }

                codeWriter.close();
                return "Translation completed: " + outputFileName;


            } else if (inputPath.isFile() && filepath.endsWith(".vm")) {
                // If the input is a single .vm file
                Parser parse = new Parser(filepath);
                CodeWriter codeWriter = new CodeWriter(filepath.replace(".vm", ".asm"));
                codeWriter.setFileName(filepath); // Set the file name for the single file

                while (parse.hasMoreLines()) {
                    parse.advance();
                    String newLine = parse.getCurrentInstruction();

                    if (newLine.startsWith("//") || newLine.isEmpty() || newLine.startsWith("\n")) {
                        continue; // Skip comments or empty lines
                    }

                    String type = parse.instructionType();
                    String arg1 = null;
                    int arg2 = -1;

                    // Retrieve arguments based on instruction type
                    if (!type.equals("comment")&&!type.equals("C_RETURN")) {
                        arg1 = parse.arg1();
                    }

                        if (type.equals("C_PUSH") || type.equals("C_POP")|| type.equals("C_FUNCTION")|| type.equals("C_CALL")) {
                            arg2 = parse.arg2();
                        }
                    

                    // Handle the instruction type
                    if (type.equals("C_ARITHMETIC")) {
                        codeWriter.writeArithmetic(newLine);
                    }
                    else if (type.equals("C_PUSH") || type.equals("C_POP")) {
                        codeWriter.writePushPop(type, arg1, arg2);
                    }
                    else if (type.equals("C_LABEL")){
                        codeWriter.writeLabel(arg1);
                        System.out.println("label was printed");
                    }
                    else if (type.equals("C_GOTO")){
                        codeWriter.writeGoto(arg1);
                    }
                    else if (type.equals("C_IF")){
                        codeWriter.writeIf(arg1);
                    }
                    else if (type.equals("C_FUNCTION")){
                        codeWriter.writeFunction(arg1, arg2);
                    }
                    else if (type.equals("C_CALL")){
                        System.out.println("call was printed");
                        codeWriter.writeCall(arg1, arg2);
                    }
                    else if (type.equals("C_RETURN")){
                        codeWriter.writeReturn();
                    }
                }

                parse.close();
                codeWriter.close();
                return "Translation completed: " + filepath.replace(".vm", ".asm");
            } else {
                throw new IllegalArgumentException("Input path must be a .vm file or a directory containing .vm files.");
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }
}
