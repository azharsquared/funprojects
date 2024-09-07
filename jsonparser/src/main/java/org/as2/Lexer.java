package org.as2;
import java.io.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private static final String validNumberRegexp = "^-?(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[eE][+-]?\\d+)?$";

    private final Reader reader;
    private final LinkedList<Character> nextCharQueue;
    private final Pattern validNumberPattern;

    public Lexer(File file) throws FileNotFoundException {
        this(new FileReader(file));
    }
    public Lexer(Reader reader) {
        this.reader = reader;
        this.nextCharQueue = new LinkedList<>();
        this.validNumberPattern = Pattern.compile(validNumberRegexp);
    }
    
    public enum Token {
        OPEN_OBJECT,
        CLOSE_OBJECT,
        TRUE,
        FALSE,
        STRING,
        WHITESPACE,
        COMMA,
        COLON,
        OPEN_ARRAY,
        CLOSE_ARRAY,
        CHAR,
        HEX,
        DIGIT,
        ZERO,
        ONENINE,
        MINUS,
        NUMBER,
        INTEGER,
        FRACTION,
        EXPONENT,
        SIGN,
        NULL,
        EOF
    }

    public static class TokenValue {
        public final Token token;
        public final Object value;
        public final String string;

        public TokenValue(Token token) {
            this(token, null);
        }
        public TokenValue(Token token, Object value) {
            this.token = token;
            this.value = value;
            this.string = value == null ? "" : value.toString();
        }
    }

    public TokenValue next() throws IOException {
        return this.parseNextToken();
    }

    private Character readNext() throws IOException {
        if (this.nextCharQueue.isEmpty()) {
            var ch = this.reader.read();
            if (ch < 0) return null;
            return (char)ch;
        } else {
            return this.nextCharQueue.poll();
        }
    }
    private Character peekNext() throws IOException {
        if (this.nextCharQueue.isEmpty()) {
            var ch = this.reader.read();
            if (ch < 0) return null;
            this.nextCharQueue.add((char)ch);
        }
        return this.nextCharQueue.element();
    }
    private Character peekNext(int pos) throws IOException {
        if (pos == 0) {
            return this.peekNext();
        }
        while (pos > 0) {
            if (this.nextCharQueue.size() <= pos) {
                this.nextCharQueue.get(pos);
            }
        }
        return null;
    }

    private TokenValue parseNextToken() throws IOException {
        var ch = this.peekNext();
        if (ch == null) {
            return new TokenValue(Token.EOF);
        }
        switch (ch) {
            case '{' -> {
                this.readNext();
                return new TokenValue(Token.OPEN_OBJECT);
            }
            case '}' -> {
                this.readNext();
                return new TokenValue(Token.CLOSE_OBJECT);
            }
            case '[' -> {
                this.readNext();
                return new TokenValue(Token.OPEN_ARRAY);
            }
            case ']' -> {
                this.readNext();
                return new TokenValue(Token.CLOSE_ARRAY);
            }
            case ':' -> {
                this.readNext();
                return new TokenValue(Token.COLON);
            }
            case ',' -> {
                this.readNext();
                return new TokenValue(Token.COMMA);
            }
            case '"' -> {
                this.readNext();
                var str = this.readString(ch);
                return new TokenValue(Token.STRING, str);
            }
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-' -> {
                this.readNext();
                var number = this.parseNumber(ch);
                return new TokenValue(Token.NUMBER, number);
            }
            case 't' -> {
                this.readNext();
                this.parseConstant(ch, "true");
                return new TokenValue(Token.TRUE);
            }
            case 'f' -> {
                this.readNext();
                this.parseConstant(ch, "false");
                return new TokenValue(Token.FALSE);
            }
            case 'n' -> {
                this.readNext();
                this.parseConstant(ch, "null");
                return new TokenValue(Token.NULL);
            }
            case ' ', '\n', '\r', '\t' -> {
                this.readNext();
                this.readWhitespace(ch);
                return this.parseNextToken();
            }
            default -> throw new IOException("invalid character parsing '" + ch + "'");
        }
    }

    private boolean isControlCharacter(Character ch){
        return ch != null && "\"\\/bfnrt".contains(ch.toString());
    }

    private void parseConstant(Character ch, String constant) throws IOException {
        String foundConstant = this.readAnyChars(ch, constant);
        if (!constant.equals(foundConstant)) {
            throw new IOException("found invalid token '"+foundConstant+"' instead of '"+constant+"'");
        }
    }

    private Number parseNumber(Character ch) throws IOException {
        String nextNumberString = this.readAnyChars(ch, "0123456789.+-eE");
        try {
            Matcher matcher = validNumberPattern.matcher(nextNumberString);
            if (matcher.matches()) {
                var number = NumberFormat.getInstance().parse(nextNumberString);
                if (number.getClass().equals(Double.class) && Double.isInfinite(number.doubleValue())) {
                    return new BigDecimal(nextNumberString);
                }
                return number;
            } else {
                throw new IOException("invalid number format '"+nextNumberString+"'");
            }
        } catch (ParseException e) {
            throw new IOException(e.getMessage());
        }
    }


    private String readString(Character quotes) throws IOException {
        var str = new StringBuilder();
        // dont add quotes
        var ch = this.peekNext();
        while (ch != null && !ch.equals(quotes)) {
            if (ch == '\\') { // is escaped
                str.append(ch);
                readNext();
                ch = peekNext();
                if (this.isControlCharacter(ch)) {
                    str.append(ch);
                    readNext();
                    ch = this.peekNext();
                }
            } else {
                str.append(ch);
                readNext();
                ch = this.peekNext();
            }
        }
        if (ch == null) {
            throw new IOException("Illegal string parsing - so far='"+str+"'");
        }
        // dont add quotes
        readNext();
        return str.toString();
    }

    private String readWhitespace(Character whitespace) throws IOException {
        return this.readAnyChars(whitespace, " \n\r\t");
    }

    private String readAnyChars(Character any, String nextChars) throws IOException {
        var str = new StringBuilder();
        str.append(any);
        var ch = this.peekNext();
        while (ch != null && nextChars.contains(ch.toString())) {
            str.append(ch);
            readNext();
            ch = this.peekNext();
        }
        if (ch == null) {
            return str.toString();
        }
        return str.toString();
    }

}