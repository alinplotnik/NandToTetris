import java.io.*;

public class Parser {
    private BufferedReader reader;
    private String currentInstruction;

    /**
     * Constructor: Opens the source text file and prepares to parse it.
     *
     * @param filePath the name of the text file containing the Hack assembly code.
     * @throws IOException if the file cannot be opened.
     */
    public Parser(String filePath) throws IOException {
        this.reader = new BufferedReader(new FileReader(filePath));
        this.currentInstruction = null;
    }

    /**
     * Checks if there are more lines to process in the file.
     *
     * @return true if there are more lines to read, false otherwise.
     * @throws IOException if an I/O error occurs.
     */
    public boolean hasMoreLines() throws IOException {
        return this.reader.ready();
    }

    /**
     * Reads the next instruction from the file and makes it the current instruction.
     *
     * @return the next instruction as a string, or null if no more instructions are available.
     * @throws IOException if an I/O error occurs.
     */
    public String advance() throws IOException {
        while (this.reader.ready()) {
            String line = this.reader.readLine();
            if (line != null && !line.trim().isEmpty()) {
                this.currentInstruction = line;
                return this.currentInstruction;
            }
        }
        this.currentInstruction = null;
        return null;
    }

    public String instructionType() {
        String instruction = getCurrentInstruction();
        
        if (instruction == null || instruction.isEmpty()) {
            throw new IllegalStateException("No current instruction to process.");
        }
        
        char firstChar = instruction.charAt(0);
        
        if (firstChar == '@') {
            return "A_INSTRUCTION";
        } else if (firstChar == '(') {
            return "L_INSTRUCTION";
        } else if (firstChar != '/') {
            return "C_INSTRUCTION";
        }
        else if (firstChar == '/') {
            return "comment";
        }

        return null;
    }
    

    public String symbol() {
        String instruction = getCurrentInstruction();
        
        if (instruction == null || instruction.isEmpty()) {
            throw new IllegalStateException("No current instruction to process.");
        }
    
        // Get the instruction type
        String type = this.instructionType();
    
        if (type.equals("A_INSTRUCTION")) {
            return instruction.substring(1);  // Return everything after '@'
        }
    
        if (type.equals("L_INSTRUCTION")) {
            return instruction.substring(1, instruction.length() - 1);  // Remove '(' and ')'
        }
    
        // Return null or throw an exception for unsupported instruction types
        return null;  // Or throw new IllegalStateException("Unsupported instruction type");
    }
    
    public String dest() {
        String instruction = getCurrentInstruction();
        
        if (instruction == null || instruction.isEmpty()) {
            throw new IllegalStateException("No current instruction to process.");
        }
        
        // Get the instruction type
        String type = this.instructionType();
    
        if (type.equals("C_INSTRUCTION")) {
            int index = instruction.indexOf('=');
            if(index != -1)
            return instruction.substring(0,index);  
        }

        return null;
    } 
    
    public String jump() {
        String instruction = getCurrentInstruction();
        
        if (instruction == null || instruction.isEmpty()) {
            throw new IllegalStateException("No current instruction to process.");
        }
    
        // Get the instruction type
        String type = this.instructionType();
    
        if (type.equals("C_INSTRUCTION")) {
            int index = instruction.indexOf(';');
            if(index != -1)
            return instruction.substring(index+1);  
        }

        return null;
    } 

    public String comp() {
        String instruction = getCurrentInstruction();
        
        if (instruction == null || instruction.isEmpty()) {
            throw new IllegalStateException("No current instruction to process.");
        }
        // Get the instruction type
        String type = this.instructionType();
    
        if (type.equals("C_INSTRUCTION")) {
            int end_index = instruction.length();            //save the last index in case there is no jump
            int dest_index = instruction.indexOf('=');    //save the start whis is 0 or 1 after dest
            int jump_index = instruction.indexOf(';');    //save the jump index 
               if (jump_index != -1 )                        //if there is a jump-
                  end_index = jump_index;                    //its index is the end of comp
            return instruction.substring(dest_index+1, end_index);
         }
      return null;
    }

    


    /**
     * Gets the current instruction.
     *
     * @return the current instruction as a string, or null if no instruction is set.
     */
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

}
