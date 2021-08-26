package com.minimalism.files.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.json.JsonObject;

import org.junit.jupiter.api.Test;

public class FieldTests {
    @Test
    void testEquals_for_same_instance() {
        InputField iut = new InputField();
        iut.setName("EmpId");
        iut.setType("String");
        iut.setMaximumLength((short)10);
        iut.setMaximumLength((short)6);
        iut.setNullable(false);
        iut.setPosition((short)0);
        iut.setValue("20362");
        InputField other = iut;
        assertTrue(iut.equals(other));
    }
    @Test
    void testEquals_fails_for_same_other_instance() {
        InputField iut = new InputField();
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
        InputField iut1 = new InputField();
        iut1.setName("EmpId");
        iut1.setType("String");
        iut1.setMaximumLength((short)10);
        iut1.setMaximumLength((short)6);
        iut1.setNullable(false);
        iut1.setPosition((short)0);
        iut1.setValue("20362");

        InputField iut2 = new InputField();
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
        InputField iut1 = new InputField();
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
    void textTimeFormat_am_pm() {
        String time = "11:32:56 PM";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:m:s a", Locale.US);
        LocalTime lt = LocalTime.parse(time, formatter);
        assertEquals(23, lt.getHour());
        
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
