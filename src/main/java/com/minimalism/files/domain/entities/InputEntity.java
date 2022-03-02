package com.minimalism.files.domain.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author R Jairam Iyer
 * The <b>InputEntity</b> class represents a domain entity. It is a generic representation of the instances
 * of the domain entity instances input in the file. It encapsulates a List of <b>InputField</b> instance,
 * which represent the attributes of the domain entity. The <b>InputEntity</b> class has the following attributes:
 * @param String name - a name that represents the nature of information in the input records;  like 'HrData' or 'Wages' or 'Products'.
 * @param String targetDomainClassName - The name of the class in the consumer service.
 * 
 * The <b>InputEntity</b> class provides basic validation of the data, after it is read from the input file.
 * Validation is limited to:
 * <ol>
 * <li>Data type - input conforms to the specified data type - String, number, boolean, date, time
 * <li>Minimum Length - number of characters in the input (input is a String!) is greater or equal to specified minimum
 * <li>Maximum Length - number of characters in the input (input is a String!) is less than or equal to specified maximum
 * <li>Nullable - value is not null if specified as not-nullable
 * </ol>
 */
public class InputEntity implements IValidation {
    private String name;
    private String targetDomainClassName;
    List<InputField> inputFields;

    public InputEntity(String name, String domainClassName, int fieldCount) {
        this.name = name;
        this.targetDomainClassName = domainClassName;
        inputFields = new ArrayList<>(fieldCount);
    }
    
    /** 
     * Returns the name of the domain entity as specified in the Record Descriptor.
     * @return String
     * 
     */
    public String getName() {
        return name;
    }
    
    /** 
     * Sets the name of the domain entity as specified in the Record Descriptor.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /** 
     * Restuns the name of the class in the consumer service, as specified in the Record Descriptor.
     * @return String
     */
    public String getTargetDomainClassName() {
        return targetDomainClassName;
    }
    
    /** 
     * Sets the name of the class in the consumer service, as specified in the Record Descriptor.
     * @param targetDomainClassName
     */
    public void setTargetDomainClassName(String targetDomainClassName) {
        this.targetDomainClassName = targetDomainClassName;
    }
    
    /** 
     * Returns a <b>List</b> of attributes (fields) of the domain entity as specified in the Record Descriptor.
     * @return List<Field>
     */
    public List<InputField> getFields() {
        return inputFields;
    }
    
    /** 
     * Sets the attributes (fields) of the domain entity as specified in the Record Descriptor.
     * @param fields
     */
    public void setFields(List<InputField> fields) {
        this.inputFields = fields;
    }
    
    /**
     * Conveience method to add an attribute to the list of fields. The Record Descriptor indicates the 
     * position of the field in the input record.
     * @param field - instance of <b>InputField</b> class that is instantiated by the <b>InputRecordFormatter</b>.
     */
    public void addField(InputField field) {
        this.inputFields.add(field.getPosition(), field);
    }
    
    /** 
     * Retrieves an <em>InputField</em> instance at the requested position,
     * @param position
     * @return Field
     */
    public InputField getField(short position) {
        return this.inputFields.get(position);
    }
    
    /** 
     * Retrieves a <b>InputField</b> with the requested name.
     * @param fieldName
     * @return Field
     */
    public InputField getField(String fieldName) {
        return this.inputFields.stream().filter(f -> f.getName().equals(fieldName)).findFirst().orElse(null);
    }

    public String asJson() {
        String returnValue = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            returnValue = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return returnValue;
    }
    /** 
     * 
     * @param propertyName
     * @return boolean
     */
    @Override
    public boolean validate(String propertyName) {
        return this.getField(propertyName).isValid();
    }
    
    /** 
     * @param propertyPosition
     * @return boolean
     */
    @Override
    public boolean validate(short propertyPosition) {
        return this.getField(propertyPosition).isValid();
    }
    
    /** 
     * @return boolean
     */
    @Override
    @JsonIgnore
    public boolean isValid() {
        return this.inputFields.stream()
                            .filter(InputField::isValid)
                            .collect(Collectors.counting()) == this.inputFields.size();
    }
    
    /** 
     * @return List<Field>
     */
    @Override
    public List<InputField> invalids() {
        return this.inputFields.stream()
                            .filter(f -> !f.isValid())
                            .toList();
    }
    
}
