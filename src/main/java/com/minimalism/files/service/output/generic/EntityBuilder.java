package com.minimalism.files.service.output.generic;

import java.util.List;

import com.minimalism.files.domain.entities.Employee;
import com.minimalism.files.domain.entities.InputEntity;
import com.minimalism.files.domain.entities.InputField;
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
    public static InputEntity build(String inputRecord, RecordDescriptor recordDescriptor) {
        var domainEntity = new InputEntity(recordDescriptor.getEntityClassName(),
        recordDescriptor.getTargetDomainClassName(), recordDescriptor.getFieldDescriptors().size());
        
        List<FieldDescriptor> fieldDescriptions = recordDescriptor.getFieldDescriptors();
        for(var i = 0; i < fieldDescriptions.size(); i++ ) {
            domainEntity.addField(buildField(fieldDescriptions.get(i)));
        }
        
        String[] fieldValues = inputRecord.split(recordDescriptor.getFieldSeparatorAsString().substring(0, 1));
        for(short i = 0; i < fieldValues.length; i++) {
            domainEntity.getField(i).setValue(fieldValues[i]);
        }
        return domainEntity;
    }

    public static InputEntity build(RecordDescriptor recordDescriptor) {
        var domainEntity = new InputEntity(recordDescriptor.getEntityClassName(),
        recordDescriptor.getTargetDomainClassName(), recordDescriptor.getFieldDescriptors().size());
        
        List<FieldDescriptor> fieldDescriptions = recordDescriptor.getFieldDescriptors();
        for(var i = 0; i < fieldDescriptions.size(); i++ ) {
            domainEntity.addField(buildField(fieldDescriptions.get(i)));
        }
        
        return domainEntity;
    }

    public static Employee buildDomainObject(String inputRecord, RecordDescriptor recordDescriptor) {
        return new Employee(recordDescriptor.getFieldSeparatorAsString(), inputRecord);
    }
    
    /** 
     * @param fieldDescriptor
     * @return Field
     */
    private static InputField buildField(FieldDescriptor fieldDescriptor) {
        var field = new InputField();
        field.setName(fieldDescriptor.getFieldName());
        field.setPosition(fieldDescriptor.getPosition());
        field.setType(fieldDescriptor.getDatatype().getTypeName());
        field.setNullable(fieldDescriptor.isNullAllowed());
        field.setMinimumLength(fieldDescriptor.getMinimumLength());
        field.setMaximumLength(fieldDescriptor.getMaximumLength());

        return field;
    }


}
