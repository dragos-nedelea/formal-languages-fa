# Lab 6: Parser & Building an Abstract Syntax Tree

**Course:** Formal Languages & Finite Automata  
**Author:** Nedelea Dragoș

---

## Theory

Parsing, or syntactic analysis, is the process of interpreting a sequence of tokens to determine its grammatical structure with respect to a given formal grammar. In this laboratory work, I focused on building an Abstract Syntax Tree (AST), which is a simplified, tree-based representation of the source code. Unlike a concrete syntax tree, the AST abstracts away certain details like parentheses and semicolons, focusing purely on the structural relationships between operations and operands. This structure is essential for later stages of a compiler, such as semantic analysis and code generation.

---

## Objectives

1. Get familiar with parsing, what it is and how it can be programmed.
2. Get familiar with the concept of AST.
3. Implement a `TokenType` enum to categorize tokens during lexical analysis.
4. Use regular expressions to identify and extract tokens from input text.
5. Implement a robust AST structure to represent mathematical expressions hierarchically.
6. Build a recursive descent parser to convert a stream of tokens into an AST.

---

## Implementation Description

### Lexical Analysis (Lexer)
The Lexer is responsible for breaking down the input string into a list of `Token` objects. It utilizes a single, complex regular expression with named groups to identify digits, identifiers, operators, and functions like `sin` or `cos`. This approach ensures that the tokenization process is both efficient and easy to maintain as the grammar grows.

### Abstract Syntax Tree (AST)
The AST is implemented using a class hierarchy where each node type inherits from a base `ASTNode` class. Specific nodes like `BinaryOpNode` and `FunctionNode` store references to their children, effectively capturing the order of operations. This hierarchical organization allows for recursive traversal, which is useful for tasks like expression evaluation or code optimization.

### Recursive Descent Parser
The Parser transforms the flat list of tokens into a tree structure by following the rules of the grammar. It uses recursive methods that correspond to different levels of operator precedence, such as `expression()` for addition and `term()` for multiplication. This top-down approach naturally handles nested expressions and ensures that the tree correctly reflects the intended mathematical logic.

---

## Code Snippets

### Token Identification (Regex)
```java
private static final Pattern TOKEN_PATTERNS = Pattern.compile(
        "(?<NUMBER>\\d+(\\.\\d+)?)|" +
        "(?<SIN>sin)|" +
        "(?<COS>cos)|" +
        "(?<IDENTIFIER>[a-zA-Z_][a-zA-Z0-9_]*)|" +
        "(?<PLUS>\\+)|" +
        "(?<MINUS>-)|" +
        "(?<MULTIPLY>\\*)|" +
        "(?<DIVIDE>/)|" +
        "(?<LPAREN>\\()|" +
        "(?<RPAREN>\\))|" +
        "(?<WHITESPACE>\\s+)"
);
```

### Main Execution Logic
```java
public static void main(String[] args) {
    String input = "sin(3.14 / 2) + cos(0)";
    Lexer lexer = new Lexer(input);
    List<Token> tokens = lexer.tokenize();
    
    Parser parser = new Parser(tokens);
    ASTNode ast = parser.parse();
    
    System.out.println("AST Output:\n" + ast.toString(0));
}
```

---

## Results

The program successfully parses complex mathematical expressions and generates a visual representation of the AST. Below is an example output for the input `sin(3.14 / 2) + cos(0)`:

```text
Input: sin(3.14 / 2) + cos(0)
Tokens: [Token(SIN, 'sin'), Token(LPAREN, '('), Token(NUMBER, '3.14'), Token(DIVIDE, '/'), Token(NUMBER, '2'), Token(RPAREN, ')'), Token(PLUS, '+'), Token(COS, 'cos'), Token(LPAREN, '('), Token(NUMBER, '0'), Token(RPAREN, ')'), Token(EOF, '')]
AST:
BinaryOpNode(+)
  FunctionNode(sin)
    BinaryOpNode(/)
      NumberNode(3.14)
      NumberNode(2)
  FunctionNode(cos)
    NumberNode(0)
```

---

## References

1. [Parsing Wiki](https://en.wikipedia.org/wiki/Parsing)
2. [Abstract Syntax Tree Wiki](https://en.wikipedia.org/wiki/Abstract_syntax_tree)
3. Cretu Dumitru – *Formal Languages & Finite Automata Course Materials*
4. Vasile Drumea, Irina Cojuhari – *Lecture Notes*
