package tokenize;

public final class Token<T extends Enum<T> & TokenType> {

    private final T type;
    private final String value;

    Token(T type, String value) {
        this.type = type;
        this.value = value;
    }

    public T getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Token{" + "type=" + type + ", value=" + value + '}';
    }
}
