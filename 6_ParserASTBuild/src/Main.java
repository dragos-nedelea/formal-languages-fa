package lfa.parser;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] inputs = {
            "3 + 5 * 2",
            "(3 + 5) * 2",
            "sin(3.14 / 2) + cos(0)",
            "x * y - 10 / 2",
            "-5 + (3 * 4)"
        };

        for (String input : inputs) {
            System.out.println("Input: " + input);
            try {
                Lexer lexer = new Lexer(input);
                List<Token> tokens = lexer.tokenize();
                
                System.out.println("Tokens: " + tokens);
                
                Parser parser = new Parser(tokens);
                ASTNode ast = parser.parse();
                
                System.out.println("AST:");
                System.out.println(ast.toString(0));
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
            System.out.println("--------------------------------------------------");
        }
    }
}
