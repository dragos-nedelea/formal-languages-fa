package src;

import java.util.*;

public class Grammar {
    private Set<String> nonTerminals;
    private Set<String> terminals;
    private Map<String, List<String>> productions;
    private String startSymbol;

    public Grammar(Set<String> nonTerminals, Set<String> terminals, Map<String, List<String>> productions, String startSymbol) {
        this.nonTerminals = new HashSet<>(nonTerminals);
        this.terminals = new HashSet<>(terminals);
        this.productions = new HashMap<>();
        for (String nt : productions.keySet()) {
            this.productions.put(nt, new ArrayList<>(productions.get(nt)));
        }
        this.startSymbol = startSymbol;
    }

    public Grammar(Grammar other) {
        this(other.nonTerminals, other.terminals, other.productions, other.startSymbol);
    }

    public Set<String> getNonTerminals() { return nonTerminals; }
    public Set<String> getTerminals() { return terminals; }
    public Map<String, List<String>> getProductions() { return productions; }
    public String getStartSymbol() { return startSymbol; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("G = (V_N, V_T, P, S)\n");
        sb.append("V_N = ").append(nonTerminals).append("\n");
        sb.append("V_T = ").append(terminals).append("\n");
        sb.append("Start = ").append(startSymbol).append("\n");
        sb.append("Productions:\n");
        for (String nt : nonTerminals) {
            if (productions.containsKey(nt)) {
                sb.append("  ").append(nt).append(" -> ").append(String.join(" | ", productions.get(nt))).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * 1. Eliminate epsilon productions
     */
    public void eliminateEpsilon() {
        Set<String> nullable = new HashSet<>();
        boolean changed = true;
        
        // Find nullable symbols
        while (changed) {
            changed = false;
            for (String nt : nonTerminals) {
                if (nullable.contains(nt)) continue;
                List<String> rules = productions.getOrDefault(nt, Collections.emptyList());
                for (String rule : rules) {
                    if (rule.equals("\u03b5") || rule.isEmpty()) {
                        if (nullable.add(nt)) changed = true;
                        break;
                    }
                    boolean allNullable = true;
                    for (int i = 0; i < rule.length(); i++) {
                        String symbol = String.valueOf(rule.charAt(i));
                        if (!nullable.contains(symbol)) {
                            allNullable = false;
                            break;
                        }
                    }
                    if (allNullable && !rule.isEmpty()) {
                        if (nullable.add(nt)) changed = true;
                        break;
                    }
                }
            }
        }

        Map<String, List<String>> newProductions = new HashMap<>();
        for (String nt : nonTerminals) {
            List<String> rules = productions.getOrDefault(nt, Collections.emptyList());
            Set<String> newRules = new HashSet<>();
            for (String rule : rules) {
                if (rule.equals("\u03b5") || rule.isEmpty()) continue;
                generateCombinations(rule, nullable, 0, "", newRules);
            }
            if (!newRules.isEmpty()) {
                newProductions.put(nt, new ArrayList<>(newRules));
            }
        }
        this.productions = newProductions;
    }

    private void generateCombinations(String rule, Set<String> nullable, int index, String current, Set<String> result) {
        if (index == rule.length()) {
            if (!current.isEmpty()) {
                result.add(current);
            }
            return;
        }

        String symbol = String.valueOf(rule.charAt(index));
        // Option 1: Include the symbol
        generateCombinations(rule, nullable, index + 1, current + symbol, result);
        
        // Option 2: If symbol is nullable, we can exclude it
        if (nullable.contains(symbol)) {
            generateCombinations(rule, nullable, index + 1, current, result);
        }
    }

    /**
     * 2. Eliminate Unit Productions (Renaming)
     */
    public void eliminateUnitProductions() {
        boolean changed = true;
        while (changed) {
            changed = false;
            Map<String, List<String>> currentProductions = new HashMap<>();
            for (String nt : productions.keySet()) {
                currentProductions.put(nt, new ArrayList<>(productions.get(nt)));
            }

            for (String nt : nonTerminals) {
                List<String> rules = currentProductions.getOrDefault(nt, new ArrayList<>());
                List<String> toAdd = new ArrayList<>();
                List<String> toRemove = new ArrayList<>();

                for (String rule : rules) {
                    if (rule.length() == 1 && nonTerminals.contains(rule)) {
                        toRemove.add(rule);
                        List<String> targetRules = currentProductions.getOrDefault(rule, Collections.emptyList());
                        for (String targetRule : targetRules) {
                            if (!rules.contains(targetRule) && !toAdd.contains(targetRule)) {
                                toAdd.add(targetRule);
                                changed = true;
                            }
                        }
                    }
                }
                rules.removeAll(toRemove);
                rules.addAll(toAdd);
                if (!rules.isEmpty()) {
                    productions.put(nt, rules);
                } else {
                    productions.remove(nt);
                }
            }
        }
    }

    /**
     * 3. Eliminate Inaccessible Symbols
     */
    public void eliminateInaccessibleSymbols() {
        Set<String> reachable = new HashSet<>();
        reachable.add(startSymbol);
        
        boolean changed = true;
        while (changed) {
            changed = false;
            Set<String> toAdd = new HashSet<>();
            for (String nt : reachable) {
                if (nonTerminals.contains(nt)) {
                    List<String> rules = productions.getOrDefault(nt, Collections.emptyList());
                    for (String rule : rules) {
                        for (int i = 0; i < rule.length(); i++) {
                            String symbol = String.valueOf(rule.charAt(i));
                            if (!reachable.contains(symbol)) {
                                toAdd.add(symbol);
                                changed = true;
                            }
                        }
                    }
                }
            }
            reachable.addAll(toAdd);
        }

        nonTerminals.retainAll(reachable);
        terminals.retainAll(reachable);
        productions.keySet().retainAll(reachable);
    }

    /**
     * 4. Eliminate Non-Productive Symbols
     */
    public void eliminateNonProductiveSymbols() {
        Set<String> productive = new HashSet<>(terminals);
        
        boolean changed = true;
        while (changed) {
            changed = false;
            for (String nt : nonTerminals) {
                if (productive.contains(nt)) continue;
                List<String> rules = productions.getOrDefault(nt, Collections.emptyList());
                for (String rule : rules) {
                    boolean ruleProductive = true;
                    for (int i = 0; i < rule.length(); i++) {
                        String symbol = String.valueOf(rule.charAt(i));
                        if (!productive.contains(symbol)) {
                            ruleProductive = false;
                            break;
                        }
                    }
                    if (ruleProductive) {
                        if (productive.add(nt)) changed = true;
                        break;
                    }
                }
            }
        }

        nonTerminals.retainAll(productive);
        productions.keySet().retainAll(productive);
        
        // Also remove rules that use non-productive symbols
        for (String nt : productions.keySet()) {
            List<String> rules = productions.get(nt);
            List<String> toRemove = new ArrayList<>();
            for (String rule : rules) {
                for (int i = 0; i < rule.length(); i++) {
                    String symbol = String.valueOf(rule.charAt(i));
                    if (!productive.contains(symbol)) {
                        toRemove.add(rule);
                        break;
                    }
                }
            }
            rules.removeAll(toRemove);
        }
        
        // Remove non-terminals with no rules left
        nonTerminals.removeIf(nt -> !productions.containsKey(nt) || productions.get(nt).isEmpty());
        productions.keySet().retainAll(nonTerminals);
    }

    /**
     * 5. Convert to Chomsky Normal Form
     */
    public void toCNF() {
        // First, handle terminal substitution in rules with length > 1
        Map<String, String> terminalToNonTerminal = new HashMap<>();
        int nextId = 0;

        Map<String, List<String>> newProductions = new HashMap<>();
        
        for (String nt : productions.keySet()) {
            List<String> rules = productions.get(nt);
            List<String> processedRules = new ArrayList<>();
            for (String rule : rules) {
                if (rule.length() > 1) {
                    StringBuilder newRule = new StringBuilder();
                    for (int i = 0; i < rule.length(); i++) {
                        String symbol = String.valueOf(rule.charAt(i));
                        if (terminals.contains(symbol)) {
                            if (!terminalToNonTerminal.containsKey(symbol)) {
                                String newNT = findUnusedNT("T" + symbol.toUpperCase());
                                terminalToNonTerminal.put(symbol, newNT);
                            }
                            newRule.append(terminalToNonTerminal.get(symbol));
                        } else {
                            newRule.append(symbol);
                        }
                    }
                    processedRules.add(newRule.toString());
                } else {
                    processedRules.add(rule);
                }
            }
            newProductions.put(nt, processedRules);
        }

        // Add rules for terminal non-terminals
        for (Map.Entry<String, String> entry : terminalToNonTerminal.entrySet()) {
            nonTerminals.add(entry.getValue());
            newProductions.put(entry.getValue(), new ArrayList<>(Collections.singletonList(entry.getKey())));
        }

        this.productions = newProductions;

        // Binarization: handle rules with length > 2
        Map<String, List<String>> finalProductions = new HashMap<>();
        for (String nt : productions.keySet()) {
            List<String> rules = productions.get(nt);
            for (String rule : rules) {
                // Rule length is calculated by number of nonterminals/symbols. 
                // Since our NTs can be more than one char now (e.g. TA), we need to split it carefully.
                // However, I simplified NTs in this code to strings. 
                // Wait, if I use "T_a", rule "T_aS" has 2 symbols.
                
                List<String> symbols = splitSymbols(rule);
                if (symbols.size() > 2) {
                    String currentNT = nt;
                    for (int i = 0; i < symbols.size() - 2; i++) {
                        String newNT = findUnusedNT("X");
                        nonTerminals.add(newNT);
                        
                        List<String> r = finalProductions.getOrDefault(currentNT, new ArrayList<>());
                        r.add(symbols.get(i) + newNT);
                        finalProductions.put(currentNT, r);
                        
                        currentNT = newNT;
                    }
                    List<String> r = finalProductions.getOrDefault(currentNT, new ArrayList<>());
                    r.add(symbols.get(symbols.size() - 2) + symbols.get(symbols.size() - 1));
                    finalProductions.put(currentNT, r);
                } else {
                    List<String> r = finalProductions.getOrDefault(nt, new ArrayList<>());
                    r.add(rule);
                    finalProductions.put(nt, r);
                }
            }
        }
        this.productions = finalProductions;
    }

    private List<String> splitSymbols(String rule) {
        List<String> symbols = new ArrayList<>();
        int i = 0;
        while (i < rule.length()) {
            boolean found = false;
            // Check longest match for non-terminal
            for (int len = 3; len >= 1; len--) { // Assuming NTs are short
                if (i + len <= rule.length()) {
                    String sub = rule.substring(i, i + len);
                    if (nonTerminals.contains(sub) || terminals.contains(sub)) {
                        symbols.add(sub);
                        i += len;
                        found = true;
                        break;
                    }
                }
            }
            if (!found) { // Should not happen if grammar is valid
                symbols.add(String.valueOf(rule.charAt(i)));
                i++;
            }
        }
        return symbols;
    }

    private String findUnusedNT(String prefix) {
        if (!nonTerminals.contains(prefix)) return prefix;
        int i = 1;
        while (nonTerminals.contains(prefix + i)) {
            i++;
        }
        return prefix + i;
    }
}
