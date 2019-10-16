# tokenize

Java tokenization library. Compatible with Java 7.

## Example

```java
enum ArithmeticTokenType implements tokenize.TokenType {

    ADD("[+\\-]"),
    MULTIPLY("[*/]"),
    INTEGER("0|[1-9][0-9]*"),
    OPEN("\\("),
    CLOSE("\\)"),
    WHITE_SPACE("[ ]+"),
    EOF(null);
    private final String regex;

    private ArithmeticTokenType(String regex) {
        this.regex = regex;
    }

    @Override
    public String getRegex() {
        return regex;
    }
}
```

```java
import tokenize.Tokenizer;
import tokenize.TokenizerFactory;

final class Main {

    public static void main(String[] args) {
        TokenizerFactory<ArithmeticTokenType> factory = new TokenizerFactory<>(
            ArithmeticTokenType.class,
            ArithmeticTokenType.EOF, // Mandatory
            ArithmeticTokenType.WHITE_SPACE // Skip (optional)
        );
        Tokenizer<ArithmeticTokenType> tokenizer = factory.createTokenizer("(1 + 2) * 3");
        System.out.println(tokenizer.next()); // Token{type=OPEN, value=(}
        System.out.println(tokenizer.next()); // Token{type=INTEGER, value=1}
        System.out.println(tokenizer.next()); // Token{type=ADD, value=+}
        System.out.println(tokenizer.next()); // Token{type=INTEGER, value=2}
        System.out.println(tokenizer.next()); // Token{type=CLOSE, value=)}
        System.out.println(tokenizer.next()); // Token{type=MULTIPLY, value=*}
        System.out.println(tokenizer.next()); // Token{type=INTEGER, value=3}
        System.out.println(tokenizer.next()); // Token{type=EOF, value=null}
        System.out.println(tokenizer.next()); // Token{type=EOF, value=null}
    }
}
```
