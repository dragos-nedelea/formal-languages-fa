package src;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Set<String> V_N = new HashSet<>(Arrays.asList("S", "A", "B", "C", "D"));
        Set<String> V_T = new HashSet<>(Arrays.asList("a", "b"));
        String startSymbol = "S";

        Map<String, List<String>> P = new HashMap<>();
        P.put("S", new ArrayList<>(Arrays.asList("aB", "bA", "B")));
        P.put("A", new ArrayList<>(Arrays.asList("b", "aD", "AS", "bAB", "\u03b5")));
        P.put("B", new ArrayList<>(Arrays.asList("a", "bS")));
        P.put("C", new ArrayList<>(Arrays.asList("AB")));
        P.put("D", new ArrayList<>(Arrays.asList("BB")));

        Grammar grammar = new Grammar(V_N, V_T, P, startSymbol);

        System.out.println("Original Grammar:");
        System.out.println(grammar);

        System.out.println("--- Step 1: Eliminate Epsilon Productions ---");
        grammar.eliminateEpsilon();
        System.out.println(grammar);

        System.out.println("--- Step 2: Eliminate Renaming (Unit Productions) ---");
        grammar.eliminateUnitProductions();
        System.out.println(grammar);

        System.out.println("--- Step 3: Eliminate Inaccessible Symbols ---");
        grammar.eliminateInaccessibleSymbols();
        System.out.println(grammar);

        System.out.println("--- Step 4: Eliminate Non-Productive Symbols ---");
        grammar.eliminateNonProductiveSymbols();
        System.out.println(grammar);

        System.out.println("--- Step 5: Convert to Chomsky Normal Form ---");
        grammar.toCNF();
        System.out.println(grammar);

        // Verification of bonus: Accepts any grammar
        testBonus();
    }

    private static void testBonus() {
        System.out.println("\n--- Testing Bonus: Another Grammar ---");
        Set<String> V_N = new HashSet<>(Arrays.asList("X", "Y", "Z"));
        Set<String> V_T = new HashSet<>(Arrays.asList("0", "1"));
        String startSymbol = "X";

        Map<String, List<String>> P = new HashMap<>();
        P.put("X", new ArrayList<>(Arrays.asList("0Y", "1Z")));
        P.put("Y", new ArrayList<>(Arrays.asList("0", "X0")));
        P.put("Z", new ArrayList<>(Arrays.asList("1", "\u03b5")));

        Grammar grammar = new Grammar(V_N, V_T, P, startSymbol);
        System.out.println("Original Bonus Grammar:");
        System.out.println(grammar);

        grammar.eliminateEpsilon();
        grammar.eliminateUnitProductions();
        grammar.eliminateInaccessibleSymbols();
        grammar.eliminateNonProductiveSymbols();
        grammar.toCNF();

        System.out.println("CNF Version of Bonus Grammar:");
        System.out.println(grammar);
    }
}
