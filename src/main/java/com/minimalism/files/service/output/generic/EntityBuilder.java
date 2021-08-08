package com.minimalism.files.service.output.generic;

import java.util.List;

import com.minimalism.files.domain.entities.Entity;
import com.minimalism.files.domain.entities.Field;
import com.minimalism.files.domain.input.FieldDescriptor;
import com.minimalism.files.domain.input.RecordDescriptor;
/**
 * @author R Jairam Iyer
 * 
 */
public class EntityBuilder {

    private EntityBuilder() {}
    /** 
     * @param inputRecord
     * @param recordDescriptor
     * @return Entity
     */
    public static Entity build(String inputRecord, RecordDescriptor recordDescriptor) {
        var domainEntity = new Entity(recordDescriptor.getEntityClassName(),
        recordDescriptor.getTargetDomainClassName(), recordDescriptor.getFieldDescriptors().size());
        
        List<FieldDescriptor> fieldDescriptions = recordDescriptor.getFieldDescriptors();
        
        for(int i = 0; i < fieldDescriptions.size(); i++ ) {
            domainEntity.addField(buildField(fieldDescriptions.get(i)));
        }
        
        String[] fieldValues = inputRecord.split(recordDescriptor.getFieldSeparatorAsString().substring(0, 1));
        for(short i = 0; i < fieldValues.length; i++) {
            domainEntity.getField(i).setValue(fieldValues[i]);
        }
        return domainEntity;
    }

    
    /** 
     * @param fieldDescriptor
     * @return Field
     */
    private static Field buildField(FieldDescriptor fieldDescriptor) {
        var field = new Field();
        field.setName(fieldDescriptor.getFieldName());
        field.setPosition(fieldDescriptor.getPosition());
        field.setType(fieldDescriptor.getDatatype().getTypeName());
        field.setNullable(fieldDescriptor.isNullAllowed());
        field.setMinimumLength(fieldDescriptor.getMinimumLength());
        field.setMaximumLength(fieldDescriptor.getMaximumLength());

        return field;
    }
}
