package tokenize;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Pattern;

public final class TokenizerFactory<T extends Enum<T> & TokenType> {

    private final T eof;
    private final T skip;
    private final List<T> types;
    private final Pattern pattern;

    public TokenizerFactory(Class<T> type, T eof, T skip) {
        EnumSet<T> set = EnumSet.allOf(type);
        set.remove(eof);
        this.eof = eof;
        this.skip = skip;
        this.types = toTypes(set);
        this.pattern = Pattern.compile(joinRegexs(toRegexs(set)));
    }

    public TokenizerFactory(Class<T> type, T eof) {
        this(type, eof, null);
    }

    public Tokenizer<T> createTokenizer(String input) {
        return new Tokenizer<>(eof, skip, types, pattern.matcher(input));
    }

    T getEof() {
        return eof;
    }

    private static <T extends Enum<T> & TokenType> List<String> toRegexs(EnumSet<T> set) {
        List<String> regexs = new ArrayList<>();
        for (T type : set) {
            // foo(bar)baz\(qux) -> (foo(?:bar)baz\(qux))
            regexs.add("(" + type.getRegex().replaceAll("(?<!\\\\)\\(", "(?:") + ")");
        }
        regexs.add("(.)"); // Illegal characters.
        return regexs;
    }

    // [foo, bar, baz] -> foo|bar|baz
    // Compatible with Java 7.
    private static String joinRegexs(List<String> regexs) {
        StringBuilder sb = new StringBuilder();
        boolean next = false;
        for (String regex : regexs) {
            if (next) {
                sb.append("|");
            } else {
                next = true;
            }
            sb.append(regex);
        }
        return sb.toString();
    }

    private static <T extends Enum<T> & TokenType> List<T> toTypes(EnumSet<T> set) {
        List<T> types = new ArrayList<>();
        types.add(null); // Matcher#group(int) starts at 1.
        types.addAll(set);
        return types;
    }
}
