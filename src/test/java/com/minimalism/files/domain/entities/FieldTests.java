package com.minimalism.files.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Locale;

import javax.json.JsonObject;

import com.minimalism.shared.common.AllEnums.DataTypes;

import org.junit.jupiter.api.Test;

class FieldTests {
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
        assertEquals(iut, other);
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
        
        assertEquals(iut1, iut2);
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
    void testSetValue_boolean_true() {
        InputField iut = new InputField();
        iut.setType(DataTypes.BOOLEAN.getTypeName());
        iut.setValue(Boolean.valueOf(true));
        assertEquals(Boolean.class.getSimpleName(), iut.getTypeName());
        assertEquals(Boolean.valueOf(true), iut.getValue());
    }
    @Test
    void testSetValue_boolean_false() {
        InputField iut = new InputField();
        iut.setType(DataTypes.BOOLEAN.getTypeName());
        iut.setValue(Boolean.valueOf(false));
        assertEquals(Boolean.class.getSimpleName(), iut.getTypeName());
        assertEquals(Boolean.valueOf(false), iut.getValue());
    }
    @Test
    void testSetValue_currency_input_as_string() {
        InputField iut = new InputField();
        iut.setType(DataTypes.CURRENCY.getTypeName());
        iut.setValue("200.83");
        assertEquals(Currency.class.getSimpleName(), iut.getTypeName());
        assertEquals(200.83, ((BigDecimal)iut.getValue()).doubleValue());
    }
    @Test
    void testSetValue_currency_input_as_big_decimal() {
        InputField iut = new InputField();
        iut.setType(DataTypes.CURRENCY.getTypeName());
        iut.setValue(new BigDecimal("200.83"));
        assertEquals(Currency.class.getSimpleName(), iut.getTypeName());
        assertEquals(200.83, ((BigDecimal)iut.getValue()).doubleValue());
    }
    @Test
    void testSetValue_currency_input_as_double() {
        InputField iut = new InputField();
        iut.setType(DataTypes.CURRENCY.getTypeName());
        iut.setValue(200.83d);
        assertEquals(Currency.class.getSimpleName(), iut.getTypeName());
        assertEquals(200.83, ((BigDecimal)iut.getValue()).doubleValue());
    }
    @Test
    void testSetValue_currency_input_as_float() {
        InputField iut = new InputField();
        iut.setType(DataTypes.CURRENCY.getTypeName());
        iut.setValue(200.83f);
        assertEquals(Currency.class.getSimpleName(), iut.getTypeName());
        assertEquals(200.83, ((BigDecimal)iut.getValue()).doubleValue());
    }
    @Test
    void testSetValue_local_date_input_as_string() {
        InputField iut = new InputField();
        iut.setType(DataTypes.LOCALDATE.getTypeName());
        iut.setValue("2020-12-14");
        assertEquals(LocalDate.class.getSimpleName(), iut.getTypeName());
        assertEquals("2020-12-14", iut.getValue());
    }
    @Test
    void testIsValid() {

    }
}
