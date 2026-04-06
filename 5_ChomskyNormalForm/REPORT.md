# Laboratory Work 5
## Chomsky Normal Form

**Course:** Formal Languages & Finite Automata  
**Author:** Nedelea Dragoș

---

## Theory

Chomsky Normal Form (CNF) is a simplified form for context-free grammars (CFGs). A grammar is in CNF if all its production rules are of the form:
1. $A \rightarrow BC$ (A non-terminal deriving exactly two non-terminals)
2. $A \rightarrow a$ (A non-terminal deriving exactly one terminal)
3. $S \rightarrow \epsilon$ (Only if $S$ is the start symbol and it does not appear on the right-hand side of any rule)

To convert a CFG to CNF, a series of transformation steps must be performed:
- **Eliminating $\epsilon$-productions**: Removing rules that derive the empty string, while ensuring the language remains the same.
- **Eliminating unit productions (Renaming)**: Rules of the form $A \rightarrow B$ are replaced by the rules of $B$.
- **Eliminating inaccessible symbols**: Removing symbols that cannot be reached from the start symbol.
- **Eliminating non-productive symbols**: Removing symbols that cannot derive a string of terminals.
- **Binarization and terminal substitution**: Breaking down long rules into pairs of non-terminals and substituting terminals in complex rules with new non-terminals.

---

## Objectives

1. Understand the rules and significance of Chomsky Normal Form.
2. Implement a system for normalizing an input grammar.
3. Follow the sequence: eliminate $\epsilon$, eliminate unit productions, eliminate inaccessible symbols, eliminate non-productive symbols.
4. Provide a generic solution that can handle any grammar (Bonus point).
5. Document the results and the implementation details.

---

## Grammar Definition (Variant 18)

- **$V_N$** = {S, A, B, C, D}  
- **$V_T$** = {a, b}  
- **Start symbol:** S  

**Productions:**
1. $S \rightarrow aB$
2. $S \rightarrow bA$
3. $S \rightarrow B$
4. $A \rightarrow b$
5. $A \rightarrow aD$
6. $A \rightarrow AS$
7. $A \rightarrow bAB$
8. $A \rightarrow \epsilon$
9. $B \rightarrow a$
10. $B \rightarrow bS$
11. $C \rightarrow AB$
12. $D \rightarrow BB$

---

## Implementation Description

The implementation is written in Java and consists of two main parts: the `Grammar` class which contains the normalization logic, and the `Main` class for testing and demonstration.

### 1. `Grammar` Class
The `Grammar` class stores the grammar's components ($V_N, V_T, P, S$). It implements the following key methods:
- `eliminateEpsilon()`: Finds all nullable non-terminals and generates all possible combinations of rules by including or excluding nullable symbols.
- `eliminateUnitProductions()`: Recursively replaces unit rules ($A \rightarrow B$) with the right-hand sides of $B$ until no unit rules remain.
- `eliminateInaccessibleSymbols()`: Uses a BFS-like approach starting from $S$ to mark reachable symbols and discards the rest.
- `eliminateNonProductiveSymbols()`: Identifies symbols that can derive a terminal string and removes those that cannot.
- `toCNF()`: Performs terminal substitution for rules with length $> 1$ and binarization for rules with more than two non-terminals.

### 2. `toCNF` Logic
The conversion process uses a naming convention for new non-terminals:
- `T_symbol` for substituted terminals (e.g., `TA`, `TB`).
- `X_i` for new non-terminals created during binarization.
The implementation uses a `splitSymbols` method to correctly handle multi-character non-terminals in rule strings.

### 3. Bonus Requirement
The implementation accepts a `Map<String, List<String>>` of productions and sets of symbols, making it capable of processing any context-free grammar. A separate test case is included in `Main` to verify this.

---

## Code Snippets

### Epsilon Elimination Combination Generation

```java
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
```

### CNF Binarization

```java
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
}
```

---

## Results

Execution output for Variant 18:

```
--- Step 1: Eliminate Epsilon Productions ---
A -> AS | bB | b | bAB | aD | aB | bA | bS | S
B -> a | bS
S -> aB | b | bA | B
D -> BB
C -> AB | B

--- Step 2: Eliminate Renaming (Unit Productions) ---
A -> b | AS | bAB | aD | aB | bA | a | bS
...
S -> aB | b | bA | a | bS
...

--- Step 3: Eliminate Inaccessible Symbols ---
(Note: C is removed)

--- Step 5: Convert to Chomsky Normal Form ---
A -> TBB | b | AS | TBX | TAD | TAB | TBA | a | TBS
B -> a | TBS
S -> TAB | b | TBA | a | TBS
D -> BB
X -> AB
TA -> a
TB -> b
```

The bonus test also passed, proving the generic nature of the implementation.

---

## Conclusions

The laboratory work successfully implemented the conversion from a context-free grammar to Chomsky Normal Form. By following the 5-step process (epsilon elimination, unit production removal, accessibility and productivity checks, and final binarization), the grammar was simplified into a standard form that is easier to use in algorithms like the CYK parser. 

Key takeaways include:
- Mastery of CFG transformation algorithms.
- Understanding the importance of standardizing grammars.
- Practical experience in Java implementation of recursive and set-based algorithms for formal languages.

---

## References

- Cretu Dumitru – *Formal Languages & Finite Automata Course Materials*  
- [Chomsky Normal Form Wiki](https://en.wikipedia.org/wiki/Chomsky_normal_form)
- Vasile Drumea, Irina Cojuhari – *Lecture Notes*
