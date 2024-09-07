package org.as2.model;

import json.JsonParserException;

import java.util.ArrayList;
import java.util.List;

public class JObject extends JValue {

    private final List<JMember> members = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JObject other = (JObject) obj;
        return other.toString().equals(this.toString());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public JsonBuilder serialize(JsonBuilder builder) {
        builder.append('{').indentPlus();
        var second = false;
        for (JMember jMember : members) {
            if (second) { builder.append(", ").newLineIfNotCompact(); }
            jMember.serialize(builder);
            second = true;
        }
        builder.indentMinus().append('}');
        return builder;
    }

    public List<JMember> addMember(JMember newMember) throws JsonParserException {
        this.addMeber(newMember);
        return this.members;
    }

    public List<JMember> addMembers(List<JMember> newMembers) throws JsonParserException {
        for (JMember jMember : newMembers) {
            this.addMeber(jMember);
        }
        return this.members;
    }

    private JMember addMeber(JMember member) throws JsonParserException {
        for (JMember jMember : members) {
            if (jMember.getKey().equals(member.getKey())) {
                throw new JsonParserException("'"+member.getKey() + "' already exists in the members of this object");
            }
        }
        this.members.add(member);
        return member;
    }

    @Override
    public Object value() {
        return this;
    }

    @Override
    public JValue get(int index) {
        return this.get(String.valueOf(index));
    }

    @Override
    public JValue get(String key) {
        var member = this.members.stream().filter(x -> x.getKey().equals(key)).findAny();
        return member.map(JMember::getValue).orElse(null);
    }
}