import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RegexEngine {
    private static final Random RANDOM = new Random();
    private static final int MAX_LIMIT = 5;
    private final List<ProcessingStep> history = new ArrayList<>();

    public String generate(String regex) {
        history.clear();
        return process(regex);
    }

    private String process(String regex) {
        StringBuilder sb = new StringBuilder();
        int i = 0;

        while (i < regex.length()) {
            char c = regex.charAt(i);

            if (c == '(') {
                int closeIdx = findClosingBracket(regex, i);
                String groupContent = regex.substring(i + 1, closeIdx);
                i = closeIdx + 1;

                int count = 1;
                String quantStr = "";
                if (i < regex.length()) {
                    if (isQuantifier(regex.charAt(i))) {
                        char q = regex.charAt(i);
                        quantStr = String.valueOf(q);
                        if (q == '^') {
                            count = Character.getNumericValue(regex.charAt(i + 1));
                            quantStr += regex.charAt(i + 1);
                            i += 2;
                        } else {
                            count = getRandomCount(q);
                            i++;
                        }
                    }
                }

                StringBuilder groupResult = new StringBuilder();
                for (int j = 0; j < count; j++) {
                    groupResult.append(resolveAlternation(groupContent));
                }

                history.add(new ProcessingStep("GROUP", "(" + groupContent + ")" + quantStr, groupResult.toString()));
                sb.append(groupResult);

            } else if (Character.isLetterOrDigit(c)) {
                String literal = String.valueOf(c);
                i++;
                int count = 1;
                String quantStr = "";

                if (i < regex.length() && (isQuantifier(regex.charAt(i)) || regex.charAt(i) == '^')) {
                    char q = regex.charAt(i);
                    quantStr = String.valueOf(q);
                    if (q == '^') {
                        count = Character.getNumericValue(regex.charAt(i + 1));
                        quantStr += regex.charAt(i + 1);
                        i += 2;
                    } else {
                        count = getRandomCount(q);
                        i++;
                    }
                }

                String res = literal.repeat(count);
                if (count != 1 || !quantStr.isEmpty()) {
                    history.add(new ProcessingStep("QUANTIFIER", literal + quantStr, res));
                } else {
                    history.add(new ProcessingStep("LITERAL", literal, res));
                }
                sb.append(res);
            } else {
                i++; // Skip spaces or unknown chars
            }
        }
        return sb.toString();
    }

    private String resolveAlternation(String content) {
        // Split by | but only at the top level of this group
        String[] parts = content.split("\\|");
        return process(parts[RANDOM.nextInt(parts.length)]);
    }

    private int getRandomCount(char q) {
        return switch (q) {
            case '*' -> RANDOM.nextInt(MAX_LIMIT + 1);
            case '+' -> RANDOM.nextInt(MAX_LIMIT) + 1;
            case '?' -> RANDOM.nextBoolean() ? 1 : 0;
            default -> 1;
        };
    }

    private boolean isQuantifier(char c) {
        return c == '*' || c == '+' || c == '?' || c == '^';
    }

    private int findClosingBracket(String str, int start) {
        int level = 1;
        for (int i = start + 1; i < str.length(); i++) {
            if (str.charAt(i) == '(')
                level++;
            else if (str.charAt(i) == ')')
                level--;
            if (level == 0)
                return i;
        }
        return -1;
    }

    public void printHistory() {
        System.out.println("Sequence of Processing:");
        history.forEach(System.out::println);
    }
}