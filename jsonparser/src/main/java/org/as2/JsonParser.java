package org.as2;

import json.model.JArray;
import json.model.JMember;
import json.model.JObject;
import json.model.JValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    private final Lexer lexer;

    public JsonParser(Lexer lexer) {
        this.lexer = lexer;
    }

    public JObject parse() throws JsonParserException {
        try {
            return this.parseObject(null);
        } catch (IOException e) {
            throw new JsonParserException(e.getMessage(), e);
        }
    }

    private JArray parseArray(Lexer.TokenValue nextToken) throws IOException, JsonParserException {
        if (nextToken == null) { nextToken = this.nextToken(); }
        var object = new JArray();
        this.tokenAssert(nextToken, Lexer.Token.OPEN_ARRAY);
        nextToken = this.nextToken();
        if (nextToken.token != Lexer.Token.CLOSE_ARRAY) {
            var values = new ArrayList<JValue>();
            nextToken = this.parseArrayValues(nextToken, values);
            object.addValues(values);
            this.tokenAssert(nextToken, Lexer.Token.CLOSE_ARRAY);    
        }
        return object;
    }

    private Lexer.TokenValue parseArrayValues(Lexer.TokenValue nextToken, List<JValue> values) throws IOException, JsonParserException {
        if (nextToken == null) { nextToken = this.nextToken(); }
        values.add(this.parseValue(nextToken));
        nextToken = this.nextToken();
        while (nextToken.token == Lexer.Token.COMMA) {
            nextToken = this.parseArrayValues(null, values);
        }    
        return nextToken;
    }

    private JObject parseObject(Lexer.TokenValue nextToken) throws IOException, JsonParserException {
        if (nextToken == null) { nextToken = this.nextToken(); }
        var object = new JObject();
        this.tokenAssert(nextToken, Lexer.Token.OPEN_OBJECT);
        nextToken = this.nextToken();
        if (nextToken.token != Lexer.Token.CLOSE_OBJECT) {
            var values = new ArrayList<JMember>();
            nextToken = this.parseMembers(nextToken, values);
            object.addMembers(values);
            this.tokenAssert(nextToken, Lexer.Token.CLOSE_OBJECT);    
        }
        return object;
    }

    private Lexer.TokenValue parseMembers(Lexer.TokenValue nextToken, List<JMember> values) throws IOException, JsonParserException {
        if (nextToken == null) { nextToken = this.nextToken(); }
        values.add(this.parseMember(nextToken));
        nextToken = this.nextToken();
        while (nextToken.token == Lexer.Token.COMMA) {
            nextToken = this.parseMembers(null, values);
        }    
        return nextToken;
    }

    private JMember parseMember(Lexer.TokenValue nextToken) throws JsonParserException, IOException {
        if (nextToken == null) { nextToken = this.nextToken(); }
        var string = tokenAssert(nextToken, Lexer.Token.STRING);
        this.nextAssert(Lexer.Token.COLON);
        var value = this.parseValue(null);
        return new JMember(string.string, value);
    }

    public JValue parseValue() throws IOException, JsonParserException {
        return this.parseValue(null);
    }
    public JValue parseValue(Lexer.TokenValue nextToken) throws IOException, JsonParserException {
        if (nextToken == null) { nextToken = this.nextToken(); }
        return switch (nextToken.token) {
            case OPEN_OBJECT -> this.parseObject(nextToken);
            case OPEN_ARRAY -> this.parseArray(nextToken);
            case STRING -> JValue.String(nextToken.string);
            case NUMBER -> JValue.Number((Number) nextToken.value);
            case FALSE -> JValue.False();
            case TRUE -> JValue.True();
            case NULL -> JValue.Null();
            default -> throw new JsonParserException("Token " + nextToken.token + " is not expected as a value");
        };
    }

    private Lexer.TokenValue tokenAssert(Lexer.TokenValue nextToken, Lexer.Token assertToken) throws JsonParserException {
        if (nextToken.token == assertToken) {
            return nextToken;
        }
        throw new JsonParserException("Token "+assertToken+" expected but got "+nextToken);
    }

    private Lexer.TokenValue nextAssert(Lexer.Token assertToken) throws JsonParserException, IOException {
        return tokenAssert(this.nextToken(), assertToken);
    }

    private Lexer.TokenValue nextToken() throws IOException {
        return this.lexer.next();
    }

}