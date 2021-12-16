package com.minimalism.files.domain.entities;

import java.math.BigDecimal;
import java.util.BitSet;
import java.util.Objects;

import javax.json.Json;
import javax.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.minimalism.shared.common.AllEnums.DataTypes;
/**
 * The <em>InputField</em> describes a field in the <em>InputRecord</em>. It allows 
 * for basic validation of the field - the type of the value is as indicated, the 
 * string representation of value is between min and max (string) length and a non-nullable
 * field does not have a null value. 
 */
public class InputField {
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

    /** 
     * @return String
     */
    public String getName() {
        return name;
        
    }
    
    /** 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /** 
     * @return String
     */
    public String getTypeName() {
        return typeName;
    }
    
    /** 
     * @param typeName
     */
    public void setType(String typeName) {
        this.typeName = typeName;
    }
    
    /** 
     * @return short
     */
    public short getPosition() {
        return position;
    }
    
    /** 
     * @param position
     */
    public void setPosition(short position) {
        this.position = position;
    }
    
    /** 
     * @return boolean
     */
    public boolean isNullable() {
        return nullable;
    }
    
    /** 
     * @param nullable
     */
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
    
    /** 
     * @return short
     */
    public short getMinimumLength() {
        return minimumLength;
    }
    
    /** 
     * @param minimumLength
     */
    public void setMinimumLength(short minimumLength) {
        this.minimumLength = minimumLength;
    }
    
    /** 
     * @return short
     */
    public short getMaximumLength() {
        return maximumLength;
    }
    
    /** 
     * @param maximumLength
     */
    public void setMaximumLength(short maximumLength) {
        this.maximumLength = maximumLength;
    }
    
    /** 
     * @return Object
     */
    public Object getValue() {
        return this.value;
    }
    
    /** 
     * @param value
     */
    public void setValue(Object value) throws ClassCastException, NumberFormatException {
        flags.set(TYPE_BIT, value.getClass().getTypeName().equals(this.typeName));
        flags.set(MIN_LENGTH_BIT, value.toString().length() >= this.minimumLength);
        flags.set(MAX_LENGTH_BIT, value.toString().length() <= this.maximumLength);
        flags.set(NULL_VALUE_BIT, value == null);

        Class<?> targetType = Enum.valueOf(DataTypes.class, this.getTypeName().toUpperCase()).getType();
        var sValue = value.toString();
        
        switch(targetType.getSimpleName()) {
            case "Boolean":
                this.value = Boolean.valueOf(sValue);
            break;
            case "Currency":
                this.value = new BigDecimal(sValue);
            break;
            case "LocalDate":
                // DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
                // if(sValue.contains("/")) {
                //     formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                // } else if(sValue.contains(" ")) {
                //     formatter = DateTimeFormatter.ofPattern("M d yyyy");
                // }
                // this.value = LocalDate.parse(sValue, formatter);
                this.value = sValue;
            break;
            case "String":
                this.value = sValue;
            break;
            case "Float":
                this.value = (Float)value;
            break;
            case "Integer":
                this.value = Integer.valueOf(sValue);
            break;
            case "Long":
                this.value = Long.valueOf(sValue);
            break;
            case "Double":
                this.value = Double.valueOf(sValue);
                break;
            case "LocalTime":
                // DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("h:m:s a", Locale.US);
                // this.value = LocalTime.parse(sValue, timeformatter);
                this.value= sValue;
            break;
            default:
                this.value = value;
            break;
        }
    }

    public JsonObject forAvroSchema() {
        String avroRulesName;
        String avroRulesTypeName;
        if(this.getName().contains("-")) {
            avroRulesName = this.getName().replace("-", "_");
        } else {
            avroRulesName = this.getName();
        }
        if(this.getTypeName().equals("Integer")) {
            avroRulesTypeName = "int";
        } else if(this.getTypeName().equals("Currency")) {
            avroRulesTypeName = "string";
        } else if(this.getTypeName().equals("LocalDate")) {
            avroRulesTypeName = "string";
        } else if(this.getTypeName().equals("LocalTime")) {
            avroRulesTypeName = "string";
        } else {
            avroRulesTypeName = this.getTypeName().toLowerCase();
        }
       
        return Json.createObjectBuilder()
        .add("name", avroRulesName)
        .add("type", avroRulesTypeName)
        .build();
    }

    public String asJson() {
        String returnValue = null;
        JsonMapper mapper = new JsonMapper();
        try {
            returnValue = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    /** 
     * @return boolean
     */
    @JsonIgnore
    public boolean isValid() {
        if(this.nullable)
            return flags.cardinality() == 3 || flags.cardinality() == 4;
        else
            return flags.cardinality() == 4;

    }
    
    /** 
     * @return int
     */
    @Override
    public int hashCode(){
        return Objects.hash(this.name, this.typeName, this.value.toString());
    }
    
    /** 
     * @param other
     * @return boolean
     */
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof InputField)) {
            return false;
        }
        return other.hashCode() == this.hashCode();
    } 
    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        return this.value.toString();
    }
}
