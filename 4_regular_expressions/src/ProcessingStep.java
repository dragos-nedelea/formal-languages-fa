
public class ProcessingStep {
    private final String type;
    private final String input;
    private final String result;

    public ProcessingStep(String type, String input, String result) {
        this.type = type;
        this.input = input;
        this.result = result;
    }

    @Override
    public String toString() {
        return String.format("[%-12s] Expression: '%-10s' -> Produced: '%s'", type, input, result);
    }
}