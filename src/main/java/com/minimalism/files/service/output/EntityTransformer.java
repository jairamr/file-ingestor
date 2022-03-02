package com.minimalism.files.service.output;

import com.minimalism.files.domain.entities.InputEntity;
import com.minimalism.shared.domain.Entity;
import com.minimalism.shared.domain.Field;

public class EntityTransformer {
    private EntityTransformer() {}

    public static Entity transform(InputEntity inputEntity) {
        Entity returnValue = new Entity();
        returnValue.setName(inputEntity.getName());
        returnValue.setTargetDomainName(inputEntity.getTargetDomainClassName());
        returnValue.setFields(inputEntity.getFields().stream().map(ifield -> {
            Field f = new Field();
            f.setName(ifield.getName());
            f.setDataType(ifield.getDataType());
            f.setValue(ifield.getValue().toString());
            return f;
        }).toList());
        return returnValue;
    }
}
