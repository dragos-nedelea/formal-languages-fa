package lfa.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private final String input;
    private final List<Token> tokens = new ArrayList<>();

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

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        Matcher matcher = TOKEN_PATTERNS.matcher(input);
        int lastEnd = 0;

        while (matcher.find()) {
            if (matcher.start() > lastEnd) {
                throw new RuntimeException("Unexpected character at index " + lastEnd + ": " + input.substring(lastEnd, matcher.start()));
            }

            if (matcher.group("NUMBER") != null) {
                tokens.add(new Token(TokenType.NUMBER, matcher.group("NUMBER")));
            } else if (matcher.group("SIN") != null) {
                tokens.add(new Token(TokenType.SIN, matcher.group("SIN")));
            } else if (matcher.group("COS") != null) {
                tokens.add(new Token(TokenType.COS, matcher.group("COS")));
            } else if (matcher.group("IDENTIFIER") != null) {
                tokens.add(new Token(TokenType.IDENTIFIER, matcher.group("IDENTIFIER")));
            } else if (matcher.group("PLUS") != null) {
                tokens.add(new Token(TokenType.PLUS, matcher.group("PLUS")));
            } else if (matcher.group("MINUS") != null) {
                tokens.add(new Token(TokenType.MINUS, matcher.group("MINUS")));
            } else if (matcher.group("MULTIPLY") != null) {
                tokens.add(new Token(TokenType.MULTIPLY, matcher.group("MULTIPLY")));
            } else if (matcher.group("DIVIDE") != null) {
                tokens.add(new Token(TokenType.DIVIDE, matcher.group("DIVIDE")));
            } else if (matcher.group("LPAREN") != null) {
                tokens.add(new Token(TokenType.LPAREN, matcher.group("LPAREN")));
            } else if (matcher.group("RPAREN") != null) {
                tokens.add(new Token(TokenType.RPAREN, matcher.group("RPAREN")));
            }
            // Whitespace is ignored

            lastEnd = matcher.end();
        }

        if (lastEnd < input.length()) {
            throw new RuntimeException("Unexpected character at index " + lastEnd + ": " + input.substring(lastEnd));
        }

        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }
}
