import java.util.*;

public class FiniteAutomaton {

    private Set<String> states;
    private Set<String> alphabet;
    private Map<String, Map<String, Set<String>>> transitions;
    private String startState;
    private Set<String> finalStates;

    public FiniteAutomaton(Set<String> states,
                           Set<String> alphabet,
                           String startState,
                           Set<String> finalStates) {

        this.states = states;
        this.alphabet = alphabet;
        this.startState = startState;
        this.finalStates = finalStates;
        this.transitions = new HashMap<>();

        for (String state : states) {
            transitions.put(state, new HashMap<>());
        }
    }

    public void addTransition(String from, String symbol, String to) {
        transitions.putIfAbsent(from, new HashMap<>());
        transitions.get(from).putIfAbsent(symbol, new HashSet<>());
        transitions.get(from).get(symbol).add(to);
    }

    public boolean isDeterministic() {
        for (String state : states) {
            Map<String, Set<String>> map = transitions.get(state);
            for (String symbol : map.keySet()) {
                if (map.get(symbol).size() > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public Grammar toRegularGrammar() {

        Set<String> nonTerminals = new HashSet<>(states);
        Set<String> terminals = new HashSet<>(alphabet);
        Map<String, List<String>> productions = new HashMap<>();

        for (String state : states) {
            productions.put(state, new ArrayList<>());
        }

        for (String from : states) {
            Map<String, Set<String>> map = transitions.get(from);
            for (String symbol : map.keySet()) {
                for (String to : map.get(symbol)) {
                    productions.get(from).add(symbol + to);
                }
            }
        }

        for (String finalState : finalStates) {
            productions.get(finalState).add("ε");
        }

        return new Grammar(nonTerminals, terminals, productions, startState);
    }

    public FiniteAutomaton convertToDFA() {

        Set<String> dfaStates = new HashSet<>();
        Set<String> dfaFinalStates = new HashSet<>();
        Map<String, Map<String, Set<String>>> dfaTransitions = new HashMap<>();

        Queue<Set<String>> queue = new LinkedList<>();
        Set<String> startSet = new HashSet<>();
        startSet.add(startState);

        queue.add(startSet);

        while (!queue.isEmpty()) {

            Set<String> currentSet = queue.poll();
            String stateName = currentSet.toString();

            dfaStates.add(stateName);
            dfaTransitions.putIfAbsent(stateName, new HashMap<>());

            for (String symbol : alphabet) {

                Set<String> newSet = new HashSet<>();

                for (String state : currentSet) {
                    Map<String, Set<String>> map = transitions.get(state);
                    if (map.containsKey(symbol)) {
                        newSet.addAll(map.get(symbol));
                    }
                }

                if (!newSet.isEmpty()) {

                    String newStateName = newSet.toString();
                    dfaTransitions.get(stateName)
                            .put(symbol, Set.of(newStateName));

                    if (!dfaStates.contains(newStateName)) {
                        queue.add(newSet);
                    }
                }
            }
        }

        for (String state : dfaStates) {
            for (String finalState : finalStates) {
                if (state.contains(finalState)) {
                    dfaFinalStates.add(state);
                }
            }
        }

        FiniteAutomaton dfa = new FiniteAutomaton(
                dfaStates,
                alphabet,
                startSet.toString(),
                dfaFinalStates
        );

        dfa.transitions = dfaTransitions;
        return dfa;
    }

    public void printTransitions() {
        for (String from : transitions.keySet()) {
            for (String symbol : transitions.get(from).keySet()) {
                System.out.println("δ(" + from + "," + symbol + ") = "
                        + transitions.get(from).get(symbol));
            }
        }
    }
}