# Laboratory Work 2
## Determinism in Finite Automata. NDFA to DFA. Chomsky Hierarchy

Author: Cretu Dumitru  
Variant: 18

---

## 1. Theory

A finite automaton (FA) is a computational model used to recognize regular languages. It consists of:
- A finite set of states
- An alphabet
- A transition function
- A start state
- A set of final states

If for every (state, symbol) pair there is exactly one transition, the automaton is deterministic (DFA).  
If multiple transitions are possible, it is non-deterministic (NDFA).

According to the Chomsky hierarchy:
- Type 3 – Regular
- Type 2 – Context-Free
- Type 1 – Context-Sensitive
- Type 0 – Unrestricted

---

## 2. Given Automaton (Variant 18)

Q = {q0,q1,q2,q3}  
Σ = {a,b,c}  
F = {q3}

Transitions:

δ(q0,a) = q0  
δ(q0,a) = q1  
δ(q1,b) = q2  
δ(q2,a) = q2  
δ(q2,b) = q3  
δ(q3,a) = q3  

The automaton is Non-Deterministic because from q0 with input a, two states are reachable.

---

## 3. FA to Regular Grammar

Q0 → aQ0 | aQ1  
Q1 → bQ2  
Q2 → aQ2 | bQ3  
Q3 → aQ3 | ε  

This grammar is right-linear → Type 3 (Regular Grammar).

---

## 4. NDFA to DFA Conversion

Using subset construction:

New states:

{q0}  
{q0,q1}  
{q2}  
{q3}  

Final DFA state:
Any state containing q3.

The resulting DFA is fully deterministic.

---

## 5. Conclusions

- The given automaton is non-deterministic.
- It was successfully converted to a regular grammar.
- The grammar is Type 3 according to Chomsky hierarchy.
- Using subset construction, an equivalent DFA was obtained.
- Determinism simplifies execution but may increase number of states.