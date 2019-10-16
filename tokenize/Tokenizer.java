package tokenize;

import java.util.List;
import java.util.regex.Matcher;

public final class Tokenizer<T extends Enum<T> & TokenType> {

    private final T eof;
    private final T skip;
    private final List<T> types;
    private final Matcher matcher;

    Tokenizer(T eof, T skip, List<T> types, Matcher matcher) {
        this.eof = eof;
        this.skip = skip;
        this.types = types;
        this.matcher = matcher;
    }

    public Token<T> next() {
        if (!matcher.find()) {
            return new Token<>(eof, null);
        }
        for (int i = 1; i <= matcher.groupCount(); i++) {
            String value = matcher.group(i);
            if (value != null) {
                if (i == types.size()) {
                    throw new IllegalArgumentException("Illegal character '" + quote(value) + "'.");
                }
                T type = types.get(i);
                if (type == skip) {
                    return next();
                }
                return new Token<>(type, value);
            }
        }
        throw new UnknownError();
    }

    private static String quote(String value) {
        char c = value.charAt(0);
        switch (c) {
            case '\'':
                return "\\'";
            case '\t':
                return "\\t";
            case '\r':
                return "\\r";
            case '\n':
                return "\\n";
            case '\b':
                return "\\b";
            default:
                return value;
        }
    }
}
