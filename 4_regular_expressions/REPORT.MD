# Laboratory Work №4: Regular Expressions
**Course:** Formal Languages & Finite Automata  
**Author:** Nedelea Dragoș 
**Variant:** 2
**Professor:** Cretu Dumitru

---

## 1. Introduction
Regular expressions (regex) are algebraic notations used to describe regular languages. They consist of constants (symbols from an alphabet) and operators (alternation, concatenation, and Kleene closures). In computer science, they are the backbone of lexical analysis, used to define tokens like identifiers, keywords, and literals.

The goal of this laboratory work is to implement a dynamic engine that can interpret a given regular expression and generate a valid string (word) that belongs to the language defined by that expression.

## 2. Objectives
1. Understand the theoretical background of Regular Expressions.
2. Implement a dynamic parser that interprets regex symbols (`*`, `+`, `?`, `|`, `()`, `^n`).
3. Ensure the generator is not hardcoded and can process any valid variant of the provided expressions.
4. Limit undefined repetitions (like `*` or `+`) to a maximum of 5 occurrences to avoid infinitely long strings.
5. **Bonus:** Implement a mechanism to track and display the sequence of processing for each expression.

## 3. Implementation Details

### 3.1 Project Structure
The project is organized following a modular approach to ensure scalability and clean code:
- `RegexEngine.java`: The core logic that parses the string and applies rules.
- `VariantManager.java`: A configuration class storing the regex strings for all 4 variants.
- `ProcessingStep.java`: A model used to satisfy the bonus objective by logging each transformation step.
- `Main.java`: The entry point that executes the generation process.

### 3.2 Algorithm Logic
The engine uses a **Recursive Descent** approach. As the string is parsed character by character:
- **Groups `()`**: When an opening parenthesis is found, the engine identifies the matching closing bracket and recursively processes the content inside.
- **Alternation `|`**: Inside groups, the string is split by the pipe symbol, and the `java.util.Random` library is used to pick one path.
- **Quantifiers**: After a literal or a group, the engine checks for:
  - `*`: 0 to 5 repetitions.
  - `+`: 1 to 5 repetitions.
  - `?`: 0 or 1 repetition.
  - `^n`: Exactly `n` repetitions (mapping the superscript notation from the task).

## 4. Execution Results

### Example Output for Variant 2:
```text
Processing Regex: M?N^2(O|P)^3Q*R+
Sequence of Processing:
[QUANTIFIER  ] Expression: 'M?        ' -> Produced: ''
[QUANTIFIER  ] Expression: 'N^2       ' -> Produced: 'NN'
[LITERAL     ] Expression: 'O         ' -> Produced: 'O'
[LITERAL     ] Expression: 'P         ' -> Produced: 'P'
[LITERAL     ] Expression: 'O         ' -> Produced: 'O'
[GROUP       ] Expression: '(O|P)^3   ' -> Produced: 'OPO'
[QUANTIFIER  ] Expression: 'Q*        ' -> Produced: 'QQQ'
[QUANTIFIER  ] Expression: 'R+        ' -> Produced: 'R'
FINAL WORD: NNOPOQQQR
------------------------------------------------------

Processing Regex: (X|Y|Z)^3 8+(9|0)
Sequence of Processing:
[LITERAL     ] Expression: 'Y         ' -> Produced: 'Y'
[LITERAL     ] Expression: 'X         ' -> Produced: 'X'
[LITERAL     ] Expression: 'Z         ' -> Produced: 'Z'
[GROUP       ] Expression: '(X|Y|Z)^3 ' -> Produced: 'YXZ'
[QUANTIFIER  ] Expression: '8+        ' -> Produced: '88'
[LITERAL     ] Expression: '0         ' -> Produced: '0'
[GROUP       ] Expression: '(9|0)     ' -> Produced: '0'
FINAL WORD: YXZ880
------------------------------------------------------

Processing Regex: (H|i)(J|K)L*N?
Sequence of Processing:
[LITERAL     ] Expression: 'H         ' -> Produced: 'H'
[GROUP       ] Expression: '(H|i)     ' -> Produced: 'H'
[LITERAL     ] Expression: 'J         ' -> Produced: 'J'
[GROUP       ] Expression: '(J|K)     ' -> Produced: 'J'
[QUANTIFIER  ] Expression: 'L*        ' -> Produced: 'LLLL'
[QUANTIFIER  ] Expression: 'N?        ' -> Produced: 'N'
FINAL WORD: HJLLLLN
------------------------------------------------------