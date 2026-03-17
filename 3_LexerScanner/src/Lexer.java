import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    public enum TokenType {
        NUMBER_INT, NUMBER_FLOAT, TRIG_FUNC, OPERATOR, LPAREN, RPAREN, IDENTIFIER, WHITESPACE
    }

    public static class Token {
        public final TokenType type;
        public final String data;

        public Token(TokenType type, String data) {
            this.type = type;
            this.data = data;
        }

        @Override
        public String toString() {
            return String.format("Token[%-12s | %s]", type, data);
        }
    }

    public static List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();

        StringBuilder patternBuilder = new StringBuilder();
        patternBuilder.append("(?<FLOAT>\\d*\\.?\\d+)");
        patternBuilder.append("|(?<INT>\\d+)");
        patternBuilder.append("|(?<TRIG>sin|cos)");
        patternBuilder.append("|(?<OP>[+\\-*/])");
        patternBuilder.append("|(?<LPAREN>\\()");
        patternBuilder.append("|(?<RPAREN>\\))");
        patternBuilder.append("|(?<ID>[a-zA-Z_][a-zA-Z0-9_]*)");
        patternBuilder.append("|(?<SKIP>\\s+)");

        Pattern pattern = Pattern.compile(patternBuilder.toString());
        Matcher matcher = pattern.matcher(input);

        int lastEnd = 0;
        while (matcher.find()) {
            if (matcher.start() > lastEnd) {
                String unknown = input.substring(lastEnd, matcher.start());
                System.err.println("Lexical Error: Unknown sequence '" + unknown + "'");
            }

            if (matcher.group("FLOAT") != null) {
                tokens.add(new Token(TokenType.NUMBER_FLOAT, matcher.group("FLOAT")));
            } else if (matcher.group("INT") != null) {
                tokens.add(new Token(TokenType.NUMBER_INT, matcher.group("INT")));
            } else if (matcher.group("TRIG") != null) {
                tokens.add(new Token(TokenType.TRIG_FUNC, matcher.group("TRIG")));
            } else if (matcher.group("OP") != null) {
                tokens.add(new Token(TokenType.OPERATOR, matcher.group("OP")));
            } else if (matcher.group("LPAREN") != null) {
                tokens.add(new Token(TokenType.LPAREN, matcher.group("LPAREN")));
            } else if (matcher.group("RPAREN") != null) {
                tokens.add(new Token(TokenType.RPAREN, matcher.group("RPAREN")));
            } else if (matcher.group("ID") != null) {
                tokens.add(new Token(TokenType.IDENTIFIER, matcher.group("ID")));
            }

            lastEnd = matcher.end();
        }

        if (lastEnd < input.length()) {
            String unknown = input.substring(lastEnd);
            System.err.println("Lexical Error: Unknown sequence '" + unknown + "'");
        }

        return tokens;
    }

    public static void main(String[] args) {
        String testInput = "cos(3.14) + sin(90) * 2";

        System.out.println("Input String: " + testInput);
        System.out.println("-----------------------------------------");

        List<Token> tokens = tokenize(testInput);

        for (Token t : tokens) {
            System.out.println(t);
        }
    }
}