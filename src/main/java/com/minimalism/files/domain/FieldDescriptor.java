package com.minimalism.files.domain;

import java.util.Objects;

import javax.json.Json;
import javax.json.JsonObject;

import com.minimalism.common.AllEnums.DataTypes;

public class FieldDescriptor {
    private String fieldName;
    private short position;
    private DataTypes dataType;
    private short minimumLength;
    private short maximumLength;
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
     * @return String
     */
    public String getFieldName() {
        return fieldName;
    }
    
    /** 
     * @param fieldName
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
    /** 
     * @return int
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
     * @return DataTypes
     */
    public DataTypes getDatatype() {
        return dataType;
    }
    
    /** 
     * @param dataType
     */
    public void setDatatype(DataTypes dataType) {
        this.dataType = dataType;
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
