package org.as2.model;

public class JValueBoolean extends JValue {

    public final Boolean bool;

    public JValueBoolean(Boolean bool) {
        this.bool = bool;
    }

    @Override
    public Object value() {
        return bool;
    }
}