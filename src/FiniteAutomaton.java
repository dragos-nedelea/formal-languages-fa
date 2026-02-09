package src;
import java.util.*;

public class FiniteAutomaton {

    private Set<String> states;       
    private Set<String> alphabet;     
    private Map<String, Map<Character, String>> transitions;
    private String initialState;      
    private Set<String> finalStates;  

    public FiniteAutomaton(Set<String> states,
                           Set<String> alphabet,
                           Map<String, Map<Character, String>> transitions,
                           String initialState,
                           Set<String> finalStates) {

        this.states = states;
        this.alphabet = alphabet;
        this.transitions = transitions;
        this.initialState = initialState;
        this.finalStates = finalStates;
    }

    public boolean stringBelongToLanguage(String input) {

        String currentState = initialState;

        for (char c : input.toCharArray()) {

            if (!transitions.containsKey(currentState))
                return false;

            if (!transitions.get(currentState).containsKey(c))
                return false;

            currentState = transitions.get(currentState).get(c);
        }

        return finalStates.contains(currentState);
    }
}
