package com.minimalism.files.domain.entities;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.json.JsonObject;

import org.junit.jupiter.api.Test;

public class FieldTests {
    @Test
    void testEquals_for_same_instance() {
        Field iut = new Field();
        iut.setName("EmpId");
        iut.setType("String");
        iut.setMaximumLength((short)10);
        iut.setMaximumLength((short)6);
        iut.setNullable(false);
        iut.setPosition((short)0);
        iut.setValue("20362");
        Field other = iut;
        assertTrue(iut.equals(other));
    }
    @Test
    void testEquals_fails_for_same_other_instance() {
        Field iut = new Field();
        iut.setName("EmpId");
        iut.setType("String");
        iut.setMaximumLength((short)10);
        iut.setMaximumLength((short)6);
        iut.setNullable(false);
        iut.setPosition((short)0);
        iut.setValue("20362");
        String other = "This is a sample string";
        assertFalse(iut.equals(other));
    }
    @Test
    void testEquals_for_same_values() {
        Field iut1 = new Field();
        iut1.setName("EmpId");
        iut1.setType("String");
        iut1.setMaximumLength((short)10);
        iut1.setMaximumLength((short)6);
        iut1.setNullable(false);
        iut1.setPosition((short)0);
        iut1.setValue("20362");

        Field iut2 = new Field();
        iut2.setName("EmpId");
        iut2.setType("String");
        iut2.setMaximumLength((short)10);
        iut2.setMaximumLength((short)6);
        iut2.setNullable(false);
        iut2.setPosition((short)0);
        iut2.setValue("20362");
        
        assertTrue(iut1.equals(iut2));
    }

    @Test
    void testForAvroSchema() {
        Field iut1 = new Field();
        iut1.setName("EmpId");
        iut1.setType("String");
        iut1.setMaximumLength((short)10);
        iut1.setMaximumLength((short)6);
        iut1.setNullable(false);
        iut1.setPosition((short)0);
        iut1.setValue("20362");
        JsonObject result = iut1.forAvroSchema();
        assertNotNull(result);
        System.out.println(result.toString());
    }

    @Test
    void testGetMaximumLength() {

    }

    @Test
    void testGetMinimumLength() {

    }

    @Test
    void testGetName() {

    }

    @Test
    void testGetPosition() {

    }

    @Test
    void testGetTypeName() {

    }

    @Test
    void testGetValue() {

    }

    @Test
    void testHashCode() {

    }

    @Test
    void testIsNullable() {

    }

    @Test
    void testIsValid() {

    }

    @Test
    void testSetMaximumLength() {

    }

    @Test
    void testSetMinimumLength() {

    }

    @Test
    void testSetName() {

    }

    @Test
    void testSetNullable() {

    }

    @Test
    void testSetPosition() {

    }

    @Test
    void testSetType() {

    }

    @Test
    void testSetValue() {

    }

    @Test
    void testToString() {

    }
}
