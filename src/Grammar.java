package src;
import java.util.*;

public class Grammar {

    private Set<String> nonTerminals;   // VN
    private Set<String> terminals;      // VT
    private Map<String, List<String>> productions; // P
    private String startSymbol;         // S

    public Grammar(Set<String> nonTerminals,
                   Set<String> terminals,
                   Map<String, List<String>> productions,
                   String startSymbol) {

        this.nonTerminals = nonTerminals;
        this.terminals = terminals;
        this.productions = productions;
        this.startSymbol = startSymbol;
    }

    // Generates ONE valid string
    public String generateString() {
        Random random = new Random();
        String current = startSymbol;

        while (containsNonTerminal(current)) {
            for (String nt : nonTerminals) {
                if (current.contains(nt)) {
                    List<String> rules = productions.get(nt);
                    String chosenRule = rules.get(random.nextInt(rules.size()));
                    current = current.replaceFirst(nt, chosenRule);
                    break;
                }
            }
        }
        return current;
    }

    private boolean containsNonTerminal(String s) {
        for (String nt : nonTerminals) {
            if (s.contains(nt)) return true;
        }
        return false;
    }

    // Grammar → Finite Automaton
    public FiniteAutomaton toFiniteAutomaton() {

        Set<String> states = new HashSet<>(nonTerminals);
        String finalState = "F";
        states.add(finalState);

        Map<String, Map<Character, String>> transitions = new HashMap<>();
        Set<String> finalStates = new HashSet<>();

        for (String nt : nonTerminals) {
            transitions.put(nt, new HashMap<>());
        }

        for (String nt : productions.keySet()) {
            for (String rule : productions.get(nt)) {

                char terminal = rule.charAt(0);

                if (rule.length() == 1) {
                    transitions.get(nt).put(terminal, finalState);
                    finalStates.add(finalState);
                } else {
                    String nextState = rule.substring(1);
                    transitions.get(nt).put(terminal, nextState);
                }
            }
        }

        return new FiniteAutomaton(
                states,
                terminals,
                transitions,
                startSymbol,
                finalStates
        );
    }
}
