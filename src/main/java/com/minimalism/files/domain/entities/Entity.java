package com.minimalism.files.domain.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Entity implements IValidation {
    private String name;
    private String targetDomainClassName;
    //Map<Short, Field> fields = new HashMap<>();
    List<Field> fields;

    public Entity(String name, String domainClassName, int fieldCount) {
        this.name = name;
        this.targetDomainClassName = domainClassName;
        fields = new ArrayList<>(fieldCount);
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTargetDomainClassName() {
        return targetDomainClassName;
    }
    public void setTargetDomainClassName(String targetDomainClassName) {
        this.targetDomainClassName = targetDomainClassName;
    }
    public List<Field> getFields() {
        return fields;
    }
    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
    public void addField(Field field) {
        this.fields.add(field.getPosition(), field);
    }
    public Field getField(short position) {
        return this.fields.get(position);
    }
    public Field getField(String fieldName) {
        return this.fields.stream().filter(f -> f.getName().equals(fieldName)).findFirst().orElse(null);
    }
    @Override
    public boolean validate(String propertyName) {
        return this.getField(propertyName).isValid();
    }
    @Override
    public boolean validate(short propertyPosition) {
        return this.getField(propertyPosition).isValid();
    }
    @Override
    public boolean isValid() {
        return this.fields.stream()
                            .filter(f -> f.isValid())
                            .collect(Collectors.counting()) == this.fields.size();
    }
    @Override
    public List<Field> invalids() {
        return this.fields.stream()
                            .filter(f -> !f.isValid())
                            .collect(Collectors.toList());
    }
    
}
