package com.minimalism.files.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.minimalism.shared.common.AllEnums.DataTypes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class InputEntityTests {

    @Test
    void testAddField() {
        InputEntity iut = new InputEntity("Employee", "com.minimalism.domain.Employee", 1);
        InputField empId = new InputField();
        empId.setName("EmpId");
        empId.setDataType(DataTypes.STRING);
        empId.setMaximumLength((short)10);
        empId.setMinimumLength((short)6);
        empId.setNullable(false);
        empId.setPosition((short)0);
        empId.setValue("20362");
        iut.addField(empId);
        assertEquals(1, iut.getFields().size());
    }

    @Test
    void testAsJson() {
        InputEntity iut = new InputEntity("Employee", "com.minimalism.domain.Employee", 1);
        InputField empId = new InputField();
        empId.setName("EmpId");
        empId.setDataType(DataTypes.STRING);
        empId.setMaximumLength((short)10);
        empId.setMinimumLength((short)6);
        empId.setNullable(false);
        empId.setPosition((short)0);
        empId.setValue("20362");
        iut.addField(empId);
        System.out.println(iut.asJson());
        assertEquals("{\"name\":\"Employee\",\"targetDomainClassName\":\"com.minimalism.domain.Employee\"," + 
        "\"fields\":[{\"name\":\"EmpId\",\"dataType\":\"STRING\",\"position\":0,\"nullable\":false," + 
        "\"minimumLength\":6,\"maximumLength\":10,\"value\":\"20362\"}]}", iut.asJson());
    }

    @Test
    void testGetField() {
        InputEntity iut = new InputEntity("Employee", "com.minimalism.domain.Employee", 1);
        InputField empId = new InputField();
        empId.setName("EmpId");
        empId.setDataType(DataTypes.STRING);
        empId.setMaximumLength((short)10);
        empId.setMinimumLength((short)6);
        empId.setNullable(false);
        empId.setPosition((short)0);
        empId.setValue("20362");
        iut.addField(empId);
        assertNotNull(iut.getField("EmpId"));
    }

    @Test
    void testGetField_with_position() {
        InputEntity iut = new InputEntity("Employee", "com.minimalism.domain.Employee", 1);
        InputField empId = new InputField();
        empId.setName("EmpId");
        empId.setDataType(DataTypes.STRING);
        empId.setMaximumLength((short)10);
        empId.setMinimumLength((short)6);
        empId.setNullable(false);
        empId.setPosition((short)0);
        empId.setValue("20362");
        iut.addField(empId);
        assertNotNull(iut.getField((short)0));
    }

    @Test
    void testGetFields() {
        InputEntity iut = new InputEntity("Employee", "com.minimalism.domain.Employee", 1);
        InputField empId = new InputField();
        empId.setName("EmpId");
        empId.setDataType(DataTypes.STRING);
        empId.setMaximumLength((short)10);
        empId.setMinimumLength((short)6);
        empId.setNullable(false);
        empId.setPosition((short)0);
        empId.setValue("20362");
        iut.addField(empId);
        assertEquals(1, iut.getFields().size());
    }

    @Test
    void testGetName() {
        InputEntity iut = new InputEntity("Employee", "com.minimalism.domain.Employee", 1);
        InputField empId = new InputField();
        empId.setName("EmpId");
        empId.setDataType(DataTypes.STRING);
        empId.setMaximumLength((short)10);
        empId.setMinimumLength((short)6);
        empId.setNullable(false);
        empId.setPosition((short)0);
        empId.setValue("20362");
        iut.addField(empId);
        assertEquals("Employee", iut.getName());
    }

    @Test
    void testGetTargetDomainClassName() {
        InputEntity iut = new InputEntity("Employee", "com.minimalism.domain.Employee", 1);
        InputField empId = new InputField();
        empId.setName("EmpId");
        empId.setDataType(DataTypes.STRING);
        empId.setMaximumLength((short)10);
        empId.setMinimumLength((short)6);
        empId.setNullable(false);
        empId.setPosition((short)0);
        empId.setValue("20362");
        iut.addField(empId);
        assertEquals("com.minimalism.domain.Employee", iut.getTargetDomainClassName());
    }

    @Test
    void testInvalids_one_field_wrong_datatype() {
        InputEntity iut = new InputEntity("Employee", "com.minimalism.domain.Employee", 3);
        InputField empId = new InputField();
        empId.setName("EmpId");
        empId.setDataType(DataTypes.STRING);
        empId.setMaximumLength((short)10);
        empId.setMinimumLength((short)5);
        empId.setNullable(false);
        empId.setPosition((short)0);
        empId.setValue("20362");
        iut.addField(empId);

        InputField namePrefix = new InputField();
        namePrefix.setName("namePrefix");
        namePrefix.setDataType(DataTypes.STRING);
        namePrefix.setMaximumLength((short)5);
        namePrefix.setMinimumLength((short)2);
        namePrefix.setNullable(false);
        namePrefix.setPosition((short)1);
        namePrefix.setValue(7);
        iut.addField(namePrefix);

        InputField firstName = new InputField();
        firstName.setName("firstName");
        firstName.setDataType(DataTypes.STRING);
        firstName.setMaximumLength((short)50);
        firstName.setMinimumLength((short)2);
        firstName.setNullable(false);
        firstName.setPosition((short)2);
        firstName.setValue("John");
        iut.addField(firstName);
        assertEquals(1, iut.invalids().size());
    }

    @ParameterizedTest
    @ValueSource(strings = {"20362", "too big a number", ""})
    void testInvalids_one_field_minimum_length_check_fails(String value) {
        InputEntity iut = new InputEntity("Employee", "com.minimalism.domain.Employee", 3);
        InputField empId = new InputField();
        empId.setName("EmpId");
        empId.setDataType(DataTypes.STRING);
        empId.setMaximumLength((short)10);
        empId.setMinimumLength((short)6);
        empId.setNullable(false);
        empId.setPosition((short)0);
        empId.setValue(value);
        iut.addField(empId);

        InputField namePrefix = new InputField();
        namePrefix.setName("namePrefix");
        namePrefix.setDataType(DataTypes.STRING);
        namePrefix.setMaximumLength((short)5);
        namePrefix.setMinimumLength((short)2);
        namePrefix.setNullable(false);
        namePrefix.setPosition((short)1);
        namePrefix.setValue("Mr.");
        iut.addField(namePrefix);

        InputField firstName = new InputField();
        firstName.setName("firstName");
        firstName.setDataType(DataTypes.STRING);
        firstName.setMaximumLength((short)50);
        firstName.setMinimumLength((short)2);
        firstName.setNullable(false);
        firstName.setPosition((short)2);
        firstName.setValue("John");
        iut.addField(firstName);
        assertEquals(1, iut.invalids().size());
    }

    @Test
    void testIsValid() {
        InputEntity iut = new InputEntity("Employee", "com.minimalism.domain.Employee", 3);
        InputField empId = new InputField();
        empId.setName("EmpId");
        empId.setDataType(DataTypes.STRING);
        empId.setMaximumLength((short)10);
        empId.setMinimumLength((short)5);
        empId.setNullable(false);
        empId.setPosition((short)0);
        empId.setValue("20362");
        iut.addField(empId);

        InputField namePrefix = new InputField();
        namePrefix.setName("namePrefix");
        namePrefix.setDataType(DataTypes.STRING);
        namePrefix.setMaximumLength((short)5);
        namePrefix.setMinimumLength((short)2);
        namePrefix.setNullable(false);
        namePrefix.setPosition((short)1);
        namePrefix.setValue("Mr.");
        iut.addField(namePrefix);

        InputField firstName = new InputField();
        firstName.setName("firstName");
        firstName.setDataType(DataTypes.STRING);
        firstName.setMaximumLength((short)50);
        firstName.setMinimumLength((short)2);
        firstName.setNullable(false);
        firstName.setPosition((short)2);
        firstName.setValue("John");
        iut.addField(firstName);
        assertEquals(Boolean.TRUE, iut.isValid());
    }

    @Test
    void testValidate() {
        
    }

    @Test
    void testValidate2() {
        
    }
}
