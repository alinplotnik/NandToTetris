import java.io.*;


public class Parser {
    private BufferedReader reader;
    private String currentInstruction;

    public Parser(String filePath) throws IOException {
        this.reader = new BufferedReader(new FileReader(filePath));
        this.currentInstruction = null;
    }

    public boolean hasMoreLines() throws IOException {
        return this.reader.ready();
    }

    public String advance() throws IOException {
        while (this.reader.ready()) {
            String line = this.reader.readLine();
            if (line != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("//")) {  // Skip empty lines and comments
                    // Strip out comments from the line
                    int commentIndex = line.indexOf("//");
                    if (commentIndex != -1) {
                        line = line.substring(0, commentIndex).trim();
                    }
                    if (!line.isEmpty()) {
                        this.currentInstruction = line;
                        return this.currentInstruction;
                    }
                }
            }
        }
        this.currentInstruction = null;
        return null;
    }

    public String getCurrentInstruction() {
        return (this.currentInstruction == null) ? "" : this.currentInstruction.trim();
    }

    /**
     * Closes the file reader to free up resources.
     *
     * @throws IOException if an I/O error occurs during closing.
     */
    public void close() throws IOException {
        if (this.reader != null) {
            this.reader.close();
        }
    }

    public void reopen(String filepath) throws IOException {
        close(); // Close the existing reader
        reader = new BufferedReader(new FileReader(filepath)); // Reopen the file
    }
  
    public String instructionType() {
        String instruction = getCurrentInstruction();
        
        if (instruction == null || instruction.isEmpty()) {
            throw new IllegalStateException("No current instruction to process.");
        }
        
        String twoFirstLetters = instruction.length() >= 2 ? instruction.substring(0, 2) : "";
        
        if (twoFirstLetters.equals("po")) {
            return "C_POP";

            } else if (twoFirstLetters.equals("pu")) {
                return "C_PUSH";

                }  else if (twoFirstLetters.equals("//")) {
                   return "comment";

                }  else if (twoFirstLetters.equals("la")) {
                    System.out.println("label type found");
                    return "C_LABEL";

                }  else if (twoFirstLetters.equals("go")) {
                    return "C_GOTO";

                }  else if (twoFirstLetters.equals("if")) {
                    return "C_IF";

                }  else if (twoFirstLetters.equals("fu")) {
                    return "C_FUNCTION";

                }  else if (twoFirstLetters.equals("re")) {
                    return "C_RETURN";

                }  else if (twoFirstLetters.equals("ca")) {
                    System.out.println("call type found");
                    return "C_CALL";
    
                } else if (!twoFirstLetters.trim().isEmpty() && twoFirstLetters != null) {
                    return "C_ARITHMETIC";
                    }
           return "no valid type was found";
      }

    public String arg1() {

      String instruction = getCurrentInstruction();
      String type = this.instructionType();

         if (instruction == null || instruction.isEmpty() || type.equals("C_RETURN")) {
             throw new IllegalStateException("No current instruction to process.");
        }

        if (type.equals("C_ARITHMETIC"))
            return instruction; 

        String[] words = instruction.split(" ");
        
        // Check if there's a second word
        if (words.length > 1) {
            try {
                return (words[1]);  
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Invalid number format in instruction: " + words[1]);
            }
        }
    

    // Return a default value if the instruction type is not C_POP or C_PUSH, or if the third word is missing
    System.out.println("Invalid argument or wrong instruction type for instruction: "+instruction);
    return null;  // Always return an int
    }

    public int arg2() {
        String instruction = getCurrentInstruction();
    
        if (instruction == null || instruction.isEmpty()) {
            throw new IllegalStateException("No current instruction to process.");
        }
        instruction = instruction.split("//")[0].trim(); // Keep only the part before "//" and trim it
        // Check if the instruction type is C_POP or C_PUSH
       if (this.instructionType().equals("C_POP") || this.instructionType().equals("C_PUSH")|| this.instructionType().equals("C_FUNCTION")|| this.instructionType().equals("C_CALL")) {
            String[] words = instruction.split(" ");
    
            // Ensure that there's a third word (i.e., words[2])
            if (words.length > 2) {
                try {
                    return Integer.parseInt(words[2]);  // Parse the third word as an integer
                } 
                catch (NumberFormatException e) {
                    throw new IllegalStateException("Invalid number format in instruction: "+instruction+" word: " + words[2]);
                }
            }
       }
    
        // Return a default value if the instruction type is not C_POP or C_PUSH, or if the third word is missing
        System.out.println("Invalid argument or wrong instruction type for instruction: "+instruction);
        return -1;  // Always return an int
    }

}





