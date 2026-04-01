# Laboratory Work 1  
## Introduction to Formal Languages. Regular Grammars. Finite Automata

**Course:** Formal Languages & Finite Automata  
**Author:** Nedelea Dragoș

---

## Theory

A formal language is a mathematically defined way of describing valid strings formed from a finite set of symbols called an alphabet. Unlike natural languages, formal languages are precise and unambiguous, which makes them suitable for use in computer science, especially in compilers, automata theory, and programming language design.

A formal grammar defines how strings in a language are constructed using production rules. Regular grammars are a restricted class of grammars that can be directly converted into finite automata. A finite automaton is an abstract computational model that processes strings symbol by symbol and decides whether they belong to a given language.

The strong relationship between regular grammars and finite automata allows us to move from a rule-based description of a language to a state-based model that can recognize strings efficiently.

---

## Objectives

* Understand the concept of a formal language and its components.
* Implement a regular grammar using a programming language.
* Generate valid strings belonging to the defined language.
* Convert a regular grammar into a finite automaton.
* Validate strings using the finite automaton.

---

## Grammar Definition (Variant 18)

* **VN** = {S, A, B, C}  
* **VT** = {a, b}  
* **Start symbol:** S  

**Productions:**
1. $S \to aA$  
2. $S \to aB$  
3. $A \to bS$  
4. $B \to aC$  
5. $C \to a$  
6. $C \to bS$  

---

## Implementation Description

The project is implemented using **Java** and is structured around two main classes: `Grammar` and `FiniteAutomaton`.

The `Grammar` class stores the non-terminals, terminals, production rules, and the start symbol. It also contains logic for generating valid strings by repeatedly applying production rules until only terminal symbols remain. Additionally, it includes a method for converting the grammar into an equivalent finite automaton.

The `FiniteAutomaton` class represents the automaton using states, an alphabet, transition functions, an initial state, and final states. It provides functionality to check whether a given input string belongs to the language by simulating state transitions.

A `Main` class is used to demonstrate the execution of the program by generating strings, converting the grammar to a finite automaton, and validating multiple test strings.

---

## Code Snippets

### Grammar → Finite Automaton Conversion

```java
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
                transitions.get(nt).put(terminal, rule.substring(1));
            }
        }
    }

    return new FiniteAutomaton(states, terminals, transitions, startSymbol, finalStates);
}
```
### Finite Automaton String Validation

```java
public boolean validateString(String input) {
    String currentState = initialState;
    for (char symbol : input.toCharArray()) {
        if (!transitions.containsKey   (currentState) || !transitions.get(currentState).containsKey(symbol)) {
            return false; // No valid transition
        }
        currentState = transitions.get(currentState).get(symbol);
    }
    return finalStates.contains(currentState);
```

## Conclusions / Results

The implemented solution successfully demonstrates the relationship between regular grammars and finite automata. The grammar correctly generates valid strings according to the given production rules (e.g., strings like `aba`, `aab`, etc.), and the finite automaton accurately validates whether strings belong to the language.

This laboratory work helped reinforce the theoretical concepts of formal languages by applying them in a practical programming context. The implementation can be extended in future labs to support more complex grammar types or NFA to DFA conversions.

---

## References

* **Cretu Dumitru** – *Formal Languages & Finite Automata Course Materials*
* **Vasile Drumea, Irina Cojuhari** – *Lecture Notes*
* **Hopcroft, Motwani, Ullman** – *Introduction to Automata Theory, Languages, and Computation*