package com.minimalism.files.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.minimalism.shared.common.AllEnums.DataTypes;
/**
 * The <b>InputField</b> describes a field in the <em>InputRecord</em>. It allows 
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
    private DataTypes dataType;
    private short position;
    private boolean nullable;
    private short minimumLength;
    private short maximumLength;
    private Object value;
    private BitSet flags = new BitSet(4);
    private List<String> dateFormats;
    private List<String> timeFormats;

    public InputField() {
        this.dateFormats = new ArrayList<>();
        this.dateFormats.add("yyyy-MM-dd");
        this.timeFormats = new ArrayList<>();
        this.timeFormats.add("HH:mm:ss");
    }

    public InputField(List<String> dateFormat, List<String> timeFormat) {
        if(dateFormat != null && !dateFormat.isEmpty()) {
            this.dateFormats = dateFormat;
        } else {
            this.dateFormats = new ArrayList<>();
            this.dateFormats.add("yyyy-MM-dd");
        }
        if(timeFormat != null && !timeFormat.isEmpty()) {
            this.timeFormats = timeFormat;
        } else {
            this.timeFormats = new ArrayList<>();
            this.timeFormats.add("HH:mm:ss");
        }
    }
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

    public DataTypes getDataType() {
        return this.dataType;
    }

    public void setDataType(DataTypes dataType) {
        this.dataType = dataType;
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
    public void setValue(Object value) throws NullPointerException, ClassCastException, NumberFormatException {
        if(value == null) {
            this.value = value;
        } else {
            var sValue = value.toString();
            
            switch(this.getDataType()) {
                case BOOLEAN:
                    if(!(sValue.equalsIgnoreCase("true") || sValue.equalsIgnoreCase("false"))) {
                        throw new NumberFormatException(String.format("Input value: %s is not a boolean value; only 'true' or 'false' are accepted.", sValue));
                    }
                    this.value = Boolean.valueOf(sValue);
                break;
                case BIG_DECIMAL:
                    this.value = new BigDecimal(sValue);
                break;
                case CHARACTER:
                    this.value = sValue.charAt(0);
                break;
                case DOUBLE:
                    this.value = Double.valueOf(sValue);
                break;
                case FLOAT:
                    this.value = Float.valueOf(sValue);
                break;
                case INTEGER:
                    this.value = Integer.valueOf(sValue);
                break;
                case LOCAL_DATE:
                    //DateTimeFormatter df = DateTimeFormatter.ofPattern(this.dateFormat);
                    this.value = parseDate(sValue);
                break;
                case LOCAL_TIME:
                    //DateTimeFormatter tf = DateTimeFormatter.ofPattern(this.timeFormat);
                    this.value = parseTime(sValue);
                break;
                case LONG:
                    this.value = Long.valueOf(sValue);
                break;
                default:
                    this.value = sValue;
                break;
            }
        }
        setValidationStatus();
    }

    /** 
     * @return boolean
     */
    @JsonIgnore
    public boolean isValid() {
        return flags.cardinality() == 4;

    }
    
    /** 
     * @return int
     */
    @Override
    public int hashCode(){
        return Objects.hash(this.name, this.dataType, this.value.toString());
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

    private LocalDate parseDate(String date) {
        LocalDate returnValue = null;
        for(String dateFormat : this.dateFormats) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
            try{
                returnValue = LocalDate.parse(date, dtf);
                break;
            } catch(DateTimeParseException e){
                // do nothing! try the next format...
            }
        }
        return returnValue;
    }

    private LocalTime parseTime(String time) {
        LocalTime returnValue = null;
        for(String timeFormat : this.timeFormats) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(timeFormat);
            try{
                returnValue = LocalTime.parse(time, dtf);
            } catch(DateTimeParseException e) {
                // do nothing! try the next format
            }
        }
        return returnValue;
    }

    private void setValidationStatus() {
        if(this.value == null) {
            if(nullable) {
                flags.set(TYPE_BIT, true);
                flags.set(MIN_LENGTH_BIT, true);
                flags.set(MAX_LENGTH_BIT, true);
                flags.set(NULL_VALUE_BIT, true);
            } else {
                flags.set(TYPE_BIT, true);
                flags.set(MIN_LENGTH_BIT, true);
                flags.set(MAX_LENGTH_BIT, true);
                flags.set(NULL_VALUE_BIT, false);
            }
        } else {
            flags.set(TYPE_BIT, this.value.getClass().getSimpleName().equals(this.dataType.getTypeName()));
            flags.set(MIN_LENGTH_BIT, this.value.toString().length() >= this.minimumLength);
            flags.set(MAX_LENGTH_BIT, this.value.toString().length() <= this.maximumLength);
            flags.set(NULL_VALUE_BIT, this.value != null);
        }
    }
}
