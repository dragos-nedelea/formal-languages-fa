import java.util.*;

public class Grammar {

    private Set<String> nonTerminals;
    private Set<String> terminals;
    private Map<String, List<String>> productions;
    private String startSymbol;

    public Grammar(Set<String> nonTerminals,
                   Set<String> terminals,
                   Map<String, List<String>> productions,
                   String startSymbol) {

        this.nonTerminals = nonTerminals;
        this.terminals = terminals;
        this.productions = productions;
        this.startSymbol = startSymbol;
    }

    public String classifyChomsky() {

        boolean isRegular = true;

        for (String left : productions.keySet()) {
            for (String right : productions.get(left)) {

                if (right.equals("ε")) continue;

                if (right.length() == 2) {
                    char t = right.charAt(0);
                    String nt = right.substring(1);

                    if (!terminals.contains(String.valueOf(t))
                            || !nonTerminals.contains(nt)) {
                        isRegular = false;
                    }
                }
                else {
                    isRegular = false;
                }
            }
        }

        if (isRegular) return "Type 3 - Regular Grammar";
        return "Not Regular (higher type)";
    }

    public void printGrammar() {
        for (String left : productions.keySet()) {
            System.out.println(left + " -> " + productions.get(left));
        }
    }
}