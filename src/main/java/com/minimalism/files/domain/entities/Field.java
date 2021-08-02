package com.minimalism.files.domain.entities;

import java.util.BitSet;
import java.util.Objects;

public class Field {
    private static short TYPE_BIT = 3;
    private static short MIN_LENGTH_BIT = 2;
    private static short MAX_LENGTH_BIT = 1;
    private static short NULL_VALUE_BIT = 0;
    
    private String name;
    private String typeName;
    private short position;
    private boolean nullable;
    private short minimumLength;
    private short maximumLength;
    private Object value;
    private BitSet flags = new BitSet(4);

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setType(String typeName) {
        this.typeName = typeName;
    }
    public short getPosition() {
        return position;
    }
    public void setPosition(short position) {
        this.position = position;
    }
    public boolean isNullable() {
        return nullable;
    }
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
    public short getMinimumLength() {
        return minimumLength;
    }
    public void setMinimumLength(short minimumLength) {
        this.minimumLength = minimumLength;
    }
    public short getMaximumLength() {
        return maximumLength;
    }
    public void setMaximumLength(short maximumLength) {
        this.maximumLength = maximumLength;
    }
    public Object getValue() {
        return this.value;
    }
    public void setValue(Object value) {
        flags.set(TYPE_BIT, value.getClass().getTypeName().equals(this.typeName));
        flags.set(MIN_LENGTH_BIT, value.toString().length() >= this.minimumLength);
        flags.set(MAX_LENGTH_BIT, value.toString().length() <= this.maximumLength);
        flags.set(NULL_VALUE_BIT, !this.nullable ? (value != null) : value == null);

        this.value = value;
    }
    public boolean isValid() {
        if(this.nullable)
            return flags.cardinality() == 3 || flags.cardinality() == 4;
        else
            return flags.cardinality() == 4;

    }
    @Override
    public int hashCode(){
        return Objects.hash(this.name, this.typeName, this.value.toString());
    }
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Field)) {
            return false;
        }
        return other.hashCode() == this.hashCode();
    } 
    @Override
    public String toString() {
        return this.value.toString();
    }
}
