package lfa.parser;

public abstract class ASTNode {
    public abstract String toString(int indent);

    protected String getIndent(int indent) {
        return "  ".repeat(indent);
    }
}

class NumberNode extends ASTNode {
    private final String value;

    public NumberNode(String value) {
        this.value = value;
    }

    @Override
    public String toString(int indent) {
        return getIndent(indent) + "NumberNode(" + value + ")";
    }
}

class VariableNode extends ASTNode {
    private final String name;

    public VariableNode(String name) {
        this.name = name;
    }

    @Override
    public String toString(int indent) {
        return getIndent(indent) + "VariableNode(" + name + ")";
    }
}

class BinaryOpNode extends ASTNode {
    private final ASTNode left;
    private final String operator;
    private final ASTNode right;

    public BinaryOpNode(ASTNode left, String operator, ASTNode right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String toString(int indent) {
        return getIndent(indent) + "BinaryOpNode(" + operator + ")\n" +
               left.toString(indent + 1) + "\n" +
               right.toString(indent + 1);
    }
}

class UnaryOpNode extends ASTNode {
    private final String operator;
    private final ASTNode operand;

    public UnaryOpNode(String operator, ASTNode operand) {
        this.operator = operator;
        this.operand = operand;
    }

    @Override
    public String toString(int indent) {
        return getIndent(indent) + "UnaryOpNode(" + operator + ")\n" +
               operand.toString(indent + 1);
    }
}

class FunctionNode extends ASTNode {
    private final String functionName;
    private final ASTNode argument;

    public FunctionNode(String functionName, ASTNode argument) {
        this.functionName = functionName;
        this.argument = argument;
    }

    @Override
    public String toString(int indent) {
        return getIndent(indent) + "FunctionNode(" + functionName + ")\n" +
               argument.toString(indent + 1);
    }
}
