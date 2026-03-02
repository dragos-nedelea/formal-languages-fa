import java.util.*;

public class Main {

    public static void main(String[] args) {

        Set<String> states = Set.of("q0","q1","q2","q3");
        Set<String> alphabet = Set.of("a","b","c");
        Set<String> finalStates = Set.of("q3");

        FiniteAutomaton fa = new FiniteAutomaton(
                states,
                alphabet,
                "q0",
                finalStates
        );

        fa.addTransition("q0","a","q0");
        fa.addTransition("q0","a","q1");
        fa.addTransition("q1","b","q2");
        fa.addTransition("q2","a","q2");
        fa.addTransition("q2","b","q3");
        fa.addTransition("q3","a","q3");

        System.out.println("Transitions:");
        fa.printTransitions();

        System.out.println("\nIs Deterministic?");
        System.out.println(fa.isDeterministic());

        Grammar grammar = fa.toRegularGrammar();

        System.out.println("\nRegular Grammar:");
        grammar.printGrammar();

        System.out.println("\nChomsky Classification:");
        System.out.println(grammar.classifyChomsky());

        FiniteAutomaton dfa = fa.convertToDFA();

        System.out.println("\nDFA Transitions:");
        dfa.printTransitions();
    }
}