import java.io.*;

public class JackAnalyzer_class {

    public JackAnalyzer_class(String inputPath) throws IOException {
        File inputFile = new File(inputPath);
        if (inputFile.isDirectory()) {
            // Process all .jack files in the directory
            File[] files = inputFile.listFiles((dir, name) -> name.endsWith(".jack"));
            if (files != null) {
                for (File file : files) {
                    // Call CompilationEngine for each .jack file
                    new CompilationEngine(file.getAbsolutePath());
                }
            }
        } else if (inputFile.isFile() && inputPath.endsWith(".jack")) {
            // Process a single .jack file
            new CompilationEngine(inputFile.getAbsolutePath());
        } else {
            throw new IllegalArgumentException("Invalid input path: " + inputPath);
        }
    }
    



   /*  public static void main(String[] args) {
         if (args.length != 1) {
            System.err.println("Usage: java JackAnalyzer <input-file-or-directory>");
            System.exit(1);
        }

        String inputPath = args[0];
        try {
            new JackAnalyzer(inputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
