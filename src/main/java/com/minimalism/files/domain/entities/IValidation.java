package com.minimalism.files.domain.entities;

import java.util.List;

public interface IValidation {
    boolean validate(String propertyName);
    boolean validate(short propertyPosition);
    boolean isValid();
    List<InputField> invalids();
}
