package com.minimalism.files.domain;

import java.util.Objects;

import javax.json.Json;
import javax.json.JsonObject;

import com.minimalism.common.AllEnums.DataTypes;

public class FieldDescriptor {
    private String fieldName;
    private int position;
    private DataTypes dataType;
    private int minimumLength;
    private int maximumLength;
    private boolean nullAllowed;

    public FieldDescriptor() {}

    public FieldDescriptor(String fieldName, int position, String dataType, int minimumLength, int maximumLength, boolean nullAllowed) {
        this.fieldName = fieldName;
        this.position = position;
        this.dataType = Enum.valueOf(DataTypes.class, dataType);
        this.minimumLength = minimumLength;
        this.maximumLength = maximumLength;
        this.nullAllowed = nullAllowed;
    }

    public FieldDescriptor(String fieldName, int position, DataTypes dataType, int minimumLength, int maximumLength, boolean nullAllowed) {
        this.fieldName = fieldName;
        this.position = position;
        this.dataType = dataType;
        this.minimumLength = minimumLength;
        this.maximumLength = maximumLength;
        this.nullAllowed = nullAllowed;
    }
    
    public FieldDescriptor(String fieldName, int position, DataTypes dataType, int minimumLength, int maximumLength) {
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
    public int getPosition() {
        return position;
    }
    
    /** 
     * @param position
     */
    public void setPosition(int position) {
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
    public int getMinimumLength() {
        return minimumLength;
    }
    
    /** 
     * @param minimumLength
     */
    public void setMinimumLength(int minimumLength) {
        this.minimumLength = minimumLength;
    }
    
    /** 
     * @return int
     */
    public int getMaximumLength() {
        return maximumLength;
    }
    
    /** 
     * @param maximumLength
     */
    public void setMaximumLength(int maximumLength) {
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
        JsonObject me = Json.createObjectBuilder()
        .add("fieldName", this.getFieldName())
        .add("dataType", this.getDatatype().name().toLowerCase())
        .build();

        return me;
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
