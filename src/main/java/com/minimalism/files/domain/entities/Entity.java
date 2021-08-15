package com.minimalism.files.domain.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 * @author R Jairam Iyer
 * The <em>Entity</em> class represents a domain entity. It is a generic representation of the instances
 * of the domain entity instances input in the file. It encapsulates a List of <em>Field</em> instance,
 * which represent the attributes of the domain entity. The <em>Entity</em> class has the following attributes:
 * @param String name - a name that represents the nature of information in the input records;  like 'HrData' or 'Wages' or 'Products'.
 * @param String targetDomainClassName - The name of the class in the consumer service.
 * 
 * The <em>Entity</em> class provides basic validation of the data, after it is read from the input file.
 * Validation is limited to:
 * <ol>
 * <li>Data type - input conforms to the specified data type - String, number, boolean, date, time
 * <li>Minimum Length - number of characters in the input (input is a String!) is greater or equal to specified minimum
 * <li>Maximum Length - number of characters in the input (input is a String!) is less than or equal to specified maximum
 * <li>Nullable - values is not null if specified as not-nullable
 * </ol>
 */
public class Entity implements IValidation {
    private String name;
    private String targetDomainClassName;
    List<Field> fields;

    public Entity(String name, String domainClassName, int fieldCount) {
        this.name = name;
        this.targetDomainClassName = domainClassName;
        fields = new ArrayList<>(fieldCount);
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
     * Returns a <em>List</em> of attributes of the domain entity as specified in the Record Descriptor.
     * @return List<Field>
     */
    public List<Field> getFields() {
        return fields;
    }
    
    /** 
     * Sets the attributes of the domain entity as specified in the Record Descriptor.
     * @param fields
     */
    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
    
    /**
     * Conveience meothod to add an attribute to the list of fields. The Record Descriptor indicates the 
     * position of the field in the input record.
     * @param field - instance of <em>Field</em> class that is instantoated by the <em>InputRecordFormatter</em>.
     */
    public void addField(Field field) {
        this.fields.add(field.getPosition(), field);
    }
    
    /** 
     * Retries a <em>Field</em> instance at the requested position,
     * @param position
     * @return Field
     */
    public Field getField(short position) {
        return this.fields.get(position);
    }
    
    /** 
     * Retrieves a <em>Field</em> with the requested name.
     * @param fieldName
     * @return Field
     */
    public Field getField(String fieldName) {
        return this.fields.stream().filter(f -> f.getName().equals(fieldName)).findFirst().orElse(null);
    }

    public JsonObject forAvroSchema() {
        JsonArrayBuilder fieldsArrayItemsBuilder = Json.createArrayBuilder();
        for(Field f : this.fields) {
            fieldsArrayItemsBuilder.add(f.forAvroSchema());
        }

        return Json.createObjectBuilder()
        .add("namespace", "com.minimalism.files.domain.entities")
        .add("name", this.getClass().getSimpleName())
        .add("type", "record")
        .add("fields", fieldsArrayItemsBuilder)
        .build();
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
    public boolean isValid() {
        return this.fields.stream()
                            .filter(Field::isValid)
                            .collect(Collectors.counting()) == this.fields.size();
    }
    
    /** 
     * @return List<Field>
     */
    @Override
    public List<Field> invalids() {
        return this.fields.stream()
                            .filter(f -> !f.isValid())
                            .collect(Collectors.toList());
    }
    
}
