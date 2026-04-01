package src;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Set<String> VN = new HashSet<>(Arrays.asList("S", "A", "B", "C"));
        Set<String> VT = new HashSet<>(Arrays.asList("a", "b"));

        Map<String, List<String>> P = new HashMap<>();
        P.put("S", Arrays.asList("aA", "aB"));
        P.put("A", Arrays.asList("bS"));
        P.put("B", Arrays.asList("aC"));
        P.put("C", Arrays.asList("a", "bS"));

        Grammar grammar = new Grammar(VN, VT, P, "S");

        System.out.println("Generated strings:");
        for (int i = 0; i < 5; i++) {
            String s = grammar.generateString();
            System.out.println("  " + s);
        }

        FiniteAutomaton fa = grammar.toFiniteAutomaton();

        System.out.println("\nValidation using Finite Automaton:");
        String[] tests = {"aa", "aba", "aab", "abab", "bbb"};

        for (String t : tests) {
            System.out.println(t + " → " + fa.stringBelongToLanguage(t));
        }
    }
}
 