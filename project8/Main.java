public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: VMTranslator <file.vm or directory>");
            return;
        }

        VMTranslator_class translator = new VMTranslator_class();
        String result = translator.translator(args[0]);
        if (result != null) {
            System.out.println(result);
        }
    }
 }

