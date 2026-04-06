public class Main {
    public static void main(String[] args) {
        int studentVariant = 2; // Change to 1, 2, 3, or 4
        String[] expressions = VariantManager.getVariant(studentVariant);
        RegexEngine engine = new RegexEngine();

        System.out.println("Running Lab 4: Regular Expressions - Variant " + studentVariant);
        System.out.println("======================================================");

        for (String regex : expressions) {
            System.out.println("\nProcessing Regex: " + regex);
            String result = engine.generate(regex);
            engine.printHistory();
            System.out.println("FINAL WORD: " + result);
            System.out.println("------------------------------------------------------");
        }
    }
}