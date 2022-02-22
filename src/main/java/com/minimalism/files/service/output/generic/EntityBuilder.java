package com.minimalism.files.service.output.generic;

import java.util.List;

import com.minimalism.files.domain.entities.InputEntity;
import com.minimalism.files.domain.entities.InputField;
import com.minimalism.files.domain.input.FieldDescriptor;
import com.minimalism.files.domain.input.RecordDescriptor;
import com.minimalism.files.service.output.EntityTransformer;
import com.minimalism.shared.domain.Entity;
/**
 * @author R Jairam Iyer
 * 
 */
public class EntityBuilder {

    private EntityBuilder() {}
    /** 
     * @param inputRecord
     * @param recordDescriptor
     * @return InputEntity
     */
    public static InputEntity build(String inputRecord, RecordDescriptor recordDescriptor) {
        var domainEntity = new InputEntity(recordDescriptor.getEntityClassName(),
        recordDescriptor.getTargetDomainClassName(), recordDescriptor.getFieldDescriptors().size());

        List<FieldDescriptor> fieldDescriptions = recordDescriptor.getFieldDescriptors();
        for(var i = 0; i < fieldDescriptions.size(); i++ ) {
            domainEntity.addField(buildField(fieldDescriptions.get(i), recordDescriptor.getDateFormat(), recordDescriptor.getTimeFormat()));
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
            domainEntity.addField(buildField(fieldDescriptions.get(i), recordDescriptor.getDateFormat(), recordDescriptor.getTimeFormat()));
        }
        
        return domainEntity;
    }

    public static Entity buildEntity(RecordDescriptor recordDescriptor) {
        return EntityTransformer.transform(build(recordDescriptor));
    }

    /** 
     * @param fieldDescriptor
     * @return Field
     */
    private static InputField buildField(FieldDescriptor fieldDescriptor, List<String> dateFormat, List<String> timeFormat) {
        var inputField = new InputField(dateFormat, timeFormat);
        inputField.setName(fieldDescriptor.getFieldName());
        inputField.setPosition(fieldDescriptor.getPosition());
        inputField.setDataType(fieldDescriptor.getDatatype());
        inputField.setNullable(fieldDescriptor.isNullAllowed());
        inputField.setMinimumLength(fieldDescriptor.getMinimumLength());
        inputField.setMaximumLength(fieldDescriptor.getMaximumLength());

        return inputField;
    }


}
