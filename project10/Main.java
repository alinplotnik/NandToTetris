import java.io.IOException;

public class Main {
    public static void main(String[] args) {
       if (args.length != 1) {
            System.err.println("Usage: java Main <input-file-or-directory>");
           System.exit(1);
        }

        String inputPath = args[0];
        try {
            new JackAnalyzer_class(inputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}