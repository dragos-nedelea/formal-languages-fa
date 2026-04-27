package lfa.parser;

import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public ASTNode parse() {
        return expression();
    }

    private ASTNode expression() {
        ASTNode node = term();

        while (match(TokenType.PLUS, TokenType.MINUS)) {
            String operator = previous().value;
            ASTNode right = term();
            node = new BinaryOpNode(node, operator, right);
        }

        return node;
    }

    private ASTNode term() {
        ASTNode node = factor();

        while (match(TokenType.MULTIPLY, TokenType.DIVIDE)) {
            String operator = previous().value;
            ASTNode right = factor();
            node = new BinaryOpNode(node, operator, right);
        }

        return node;
    }

    private ASTNode factor() {
        if (match(TokenType.PLUS)) {
            return factor(); // Unary plus, ignore
        }
        if (match(TokenType.MINUS)) {
            return new UnaryOpNode("-", factor());
        }

        return primary();
    }

    private ASTNode primary() {
        if (match(TokenType.NUMBER)) {
            return new NumberNode(previous().value);
        }

        if (match(TokenType.IDENTIFIER)) {
            return new VariableNode(previous().value);
        }

        if (match(TokenType.SIN, TokenType.COS)) {
            String func = previous().value;
            consume(TokenType.LPAREN, "Expect '(' after function name.");
            ASTNode arg = expression();
            consume(TokenType.RPAREN, "Expect ')' after function argument.");
            return new FunctionNode(func, arg);
        }

        if (match(TokenType.LPAREN)) {
            ASTNode node = expression();
            consume(TokenType.RPAREN, "Expect ')' after expression.");
            return node;
        }

        throw new RuntimeException("Unexpected token: " + peek());
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw new RuntimeException(message + " Found: " + peek());
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }
}
