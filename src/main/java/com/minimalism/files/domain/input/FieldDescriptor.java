package com.minimalism.files.domain.input;

import java.util.Objects;
import javax.json.Json;
import javax.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.minimalism.shared.common.AllEnums.DataTypes;

/**
 * The <b>FieldDescriptor</b> class describes an input field in an input record. The FieldDescriptor 
 * facilitates basic (<b><u>not business</b></u>) validation of each field (and therefore, record) read
 * from the input file. 
 * While parsing the input file, the data type specified in the FieldDescriptor is used to 'box' the value
 * read from the file. The supported data types are:
 * <ol>
 *  <li>Boolean</li>
 *  <li>BigDecimal</li>
 *  <li>Double</li>
 *  <li>Float</li>
 *  <li>Integer</li>
 *  <li>LocalDate</li>
 *  </li>LocalTime</li>
 *  <li>Long</li>
 *  <li>String</li>
 * </ol>
 */
public class FieldDescriptor {
    @JsonProperty("field-name")
    private String fieldName;
    private short position;
    @JsonProperty("data-type")
    private DataTypes dataType;
    @JsonProperty("minimum-length")
    private short minimumLength;
    @JsonProperty("maximum-length")
    private short maximumLength;
    @JsonProperty("null-allowed")
    private boolean nullAllowed;

    public FieldDescriptor() {}

    public FieldDescriptor(String fieldName, short position, String dataType, short minimumLength, short maximumLength, boolean nullAllowed) {
        this.fieldName = fieldName;
        this.position = position;
        this.dataType = Enum.valueOf(DataTypes.class, dataType);
        this.minimumLength = minimumLength;
        this.maximumLength = maximumLength;
        this.nullAllowed = nullAllowed;
    }

    public FieldDescriptor(String fieldName, short position, DataTypes dataType, short minimumLength, short maximumLength, boolean nullAllowed) {
        this.fieldName = fieldName;
        this.position = position;
        this.dataType = dataType;
        this.minimumLength = minimumLength;
        this.maximumLength = maximumLength;
        this.nullAllowed = nullAllowed;
    }
    
    public FieldDescriptor(String fieldName, short position, DataTypes dataType, short minimumLength, short maximumLength) {
        this(fieldName, position, dataType, minimumLength, maximumLength, false);
    }

    
    /** 
     * Gets the name of the field in the input reord. 
     * @return String
     */
    public String getFieldName() {
        return fieldName;
    }
    
    /** 
     * Sets the name of the field in the input record.
     * @param fieldName
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
    /** 
     * Gets the position of the field in the input record.
     * @return int
     */
    public short getPosition() {
        return position;
    }
    
    /** 
     * Sets the position of the field in the input record.
     * @param position
     */
    public void setPosition(short position) {
        this.position = position;
    }
    
    /** 
     * Gets the data type of the field. Primitive types - boolean, double, float, long, integer and some
     * non-primitive types - string, big decimal, local date and local time are supported.
     * @return DataTypes
     */
    public DataTypes getDatatype() {
        return dataType;
    }
    
    /** 
     * Sets the data type of the field.
     * @param dataType
     */
    public void setDatatype(DataTypes dataType) {
        this.dataType = dataType;
    }

    /**
     * 
     * @param dataType
     */
    @JsonSetter("data-type")
    public void setDatatype(String dataType) {
        this.dataType = Enum.valueOf(DataTypes.class, dataType);
    }
    
    /** 
     * @return int
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
     * @return int
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
     * @return boolean
     */
    public boolean isNullAllowed() {
        return nullAllowed;
    }
    
    /** 
     * @param nullAllowed
     */
    public void setNullAllowed(boolean nullAllowed) {
        this.nullAllowed = nullAllowed;
    }

    
    /** 
     * @return int
     */
    @Override
    public int hashCode() {
       return Objects.hash(this.fieldName, this.dataType, this.position, 
                                this.minimumLength, this.maximumLength, this.nullAllowed);
    }

    
    /** 
     * @return String
     */
    public String getFieldNameForAvro() {
        return this.fieldName.replaceAll("[ -]", "_");
    }
    
    /** 
     * @param other
     * @return boolean
     */
    @Override
    public boolean equals(Object other) {
        if(other == null)
            return false;
        if(!(other instanceof FieldDescriptor))
            return false;
        if (other == this)
            return true;
        FieldDescriptor otherObject = (FieldDescriptor) other;
        return this.hashCode() == otherObject.hashCode();
    }

    
    /** 
     * @return JsonObject
     */
    public JsonObject asJson() {
        return Json.createObjectBuilder()
        .add("name", this.getFieldNameForAvro())
        .add("type", "string")
        .build();
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("fieldName:");
        sb.append(this.getFieldName());
        sb.append(",");
        sb.append("position:");
        sb.append(this.getPosition());
        sb.append(",");
        sb.append("dataType:");
        sb.append(this.getDatatype().name().toLowerCase());
        sb.append(",");
        sb.append("minimumLength:");
        sb.append(this.getMinimumLength());
        sb.append(",");
        sb.append("maximumLength:");
        sb.append(this.getMaximumLength());
        sb.append(",");
        sb.append("nullAllowed:");
        sb.append(this.isNullAllowed() ? "true" : "false");
        sb.append("}");
        
        return sb.toString();
    }
}
