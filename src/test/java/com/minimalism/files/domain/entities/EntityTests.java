package com.minimalism.files.domain.entities;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class EntityTests {
    @Test
    void testForAvroSchema() {
        InputEntity iut = new InputEntity("Employee", "com.minimalism.domain.Employee", 3);
        InputField empId = new InputField();
        empId.setName("EmpId");
        empId.setType("String");
        empId.setMaximumLength((short)10);
        empId.setMinimumLength((short)6);
        empId.setNullable(false);
        empId.setPosition((short)0);
        empId.setValue("20362");
        iut.addField(empId);

        InputField namePrefix = new InputField();
        namePrefix.setName("namePrefix");
        namePrefix.setType("String");
        namePrefix.setMaximumLength((short)5);
        namePrefix.setMinimumLength((short)2);
        namePrefix.setNullable(false);
        namePrefix.setPosition((short)1);
        namePrefix.setValue("Mr.");
        iut.addField(namePrefix);

        InputField firstName = new InputField();
        firstName.setName("firstName");
        firstName.setType("String");
        firstName.setMaximumLength((short)10);
        firstName.setMinimumLength((short)50);
        firstName.setNullable(false);
        firstName.setPosition((short)2);
        firstName.setValue("John");
        iut.addField(firstName);

        var result = iut.forAvroSchema();
        assertNotNull(result);
        System.out.println(result.toString());
    }
}
